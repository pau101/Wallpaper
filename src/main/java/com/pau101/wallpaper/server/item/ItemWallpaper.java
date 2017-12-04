package com.pau101.wallpaper.server.item;

import com.pau101.wallpaper.Wallpaper;
import com.pau101.wallpaper.client.ClientProxy;
import com.pau101.wallpaper.server.ServerProxy;
import com.pau101.wallpaper.server.item.group.ItemGroupWallpaper;
import com.pau101.wallpaper.server.net.clientbound.MessageWallpaperChange;
import com.pau101.wallpaper.server.sound.WallpaperSounds;
import com.pau101.wallpaper.server.world.WallpaperData;
import com.pau101.wallpaper.server.world.WallpaperManager;
import com.pau101.wallpaper.server.world.WallpaperType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public final class ItemWallpaper extends Item {
	public ItemWallpaper() {
		setHasSubtypes(true);
		setCreativeTab(ItemGroupWallpaper.INSTANCE);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (facing.getAxis() == EnumFacing.Axis.Y) {
			return EnumActionResult.PASS;
		}
		IBlockState blockOn = world.getBlockState(pos);
		if (!ServerProxy.validBlock(blockOn)) {
			return EnumActionResult.PASS;
		}
		ItemStack stack = player.getHeldItem(hand);
		WallpaperManager mgr = world.isRemote ? ClientProxy.getWallpaperManager() : ServerProxy.getWallpaperManager();
		WallpaperData wallpaper = new WallpaperData(WallpaperType.getWallpaper(stack.getMetadata()), fitPuzzle(mgr, world, pos, facing));
		if (world.isRemote) {
			WallpaperData existingWallpaper = ClientProxy.getWallpaper(world, pos, facing);
			if (existingWallpaper == WallpaperData.NONE) {
				ClientProxy.setWallpaper(world, pos, facing, wallpaper);
				return EnumActionResult.SUCCESS;
			}
		} else {
			WallpaperData existingWallpaper = ServerProxy.getWallpaper(world, pos, facing);
			if (existingWallpaper == WallpaperData.NONE) {
				ServerProxy.setWallpaper(world, pos, facing, wallpaper);
				Wallpaper.instance().getNetwork().sendToPlayers(new MessageWallpaperChange(pos, wallpaper, facing), (WorldServer) world, pos.getX() >> 4, pos.getZ() >> 4);
				stack.shrink(1);
				world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, WallpaperSounds.WALLPAPER_PLACE, SoundCategory.PLAYERS, 1, world.rand.nextFloat() * 0.3f + 1);
				return EnumActionResult.SUCCESS;
			}
		}
		return EnumActionResult.PASS;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + '.' + WallpaperType.getWallpaper(stack.getMetadata()).getUnlocalizedName();
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		if (isInCreativeTab(tab)) {
			WallpaperType.stream()
				.filter(WallpaperType::isEnabled)
				.forEach(t -> subItems.add(new ItemStack(this, 1, t.ordinal())));
		}
	}

	public static int fitPuzzle(WallpaperManager mgr, World world, BlockPos pos, EnumFacing face) {
		int partX = 0, partY = 0;
		WallpaperData top = mgr.getWallpaper(world, pos.up(), face);
		WallpaperData down = mgr.getWallpaper(world, pos.down(), face);
		WallpaperData left = mgr.getWallpaper(world, pos.offset(face.rotateY()), face);
		WallpaperData right = mgr.getWallpaper(world, pos.offset(face.rotateYCCW()), face);
		if (left != WallpaperData.NONE) {
			partX = left.getPartX() + 1;
			partY = left.getPartY();
		} else if (right != WallpaperData.NONE) {
			partX = right.getPartX() - 1;
			partY = right.getPartY();
		} else if (top != WallpaperData.NONE) {
			partX = top.getPartX();
			partY = top.getPartY() + 1;
		} else if (down != WallpaperData.NONE) {
			partX = down.getPartX();
			partY = down.getPartY() - 1;
		} else {
			WallpaperData cornerLeft = mgr.getWallpaper(world, pos, face.rotateY());
			WallpaperData cornerRight = mgr.getWallpaper(world, pos, face.rotateYCCW());
			if (cornerLeft != WallpaperData.NONE) {
				partX = cornerLeft.getPartX() + 1;
				partY = cornerLeft.getPartY();
			} else if (cornerRight != WallpaperData.NONE) {
				partX = cornerRight.getPartX() - 1;
				partY = cornerRight.getPartY();
			} else {
				WallpaperData sideLeft = mgr.getWallpaper(world, pos.offset(face).offset(face.rotateY()), face.rotateYCCW());
				WallpaperData sideRight = mgr.getWallpaper(world, pos.offset(face).offset(face.rotateYCCW()), face.rotateY());
				if (sideLeft != WallpaperData.NONE) {
					partX = sideLeft.getPartX() + 1;
					partY = sideLeft.getPartY();
				} else if (sideRight != WallpaperData.NONE) {
					partX = sideRight.getPartX() - 1;
					partY = sideRight.getPartY();
				} else if (!world.isAirBlock(pos.offset(face).down())) {
					partY = Wallpaper.PATTERN_HEIGHT - 1;
				}
			}
		}
		return WallpaperData.combine(partX, partY);
	}
}
