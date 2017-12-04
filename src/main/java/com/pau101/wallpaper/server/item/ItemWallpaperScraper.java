package com.pau101.wallpaper.server.item;

import com.pau101.wallpaper.Wallpaper;
import com.pau101.wallpaper.client.ClientProxy;
import com.pau101.wallpaper.server.ServerProxy;
import com.pau101.wallpaper.server.item.group.ItemGroupWallpaper;
import com.pau101.wallpaper.server.net.clientbound.MessageWallpaperChange;
import com.pau101.wallpaper.server.sound.WallpaperSounds;
import com.pau101.wallpaper.server.world.WallpaperData;
import com.pau101.wallpaper.server.world.WallpaperManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public final class ItemWallpaperScraper extends Item {
	public ItemWallpaperScraper() {
		setMaxStackSize(1);
		setMaxDamage(238);
		setCreativeTab(ItemGroupWallpaper.INSTANCE);
	}

	@Override
	public boolean isFull3D() {
		return true;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		WallpaperManager mgr = world.isRemote ? ClientProxy.getWallpaperManager() : ServerProxy.getWallpaperManager();
		WallpaperData wallpaper = mgr.getWallpaper(world, pos, facing);
		if (wallpaper != WallpaperData.NONE) {
			if (!world.isRemote && !player.capabilities.isCreativeMode) {
				ServerProxy.dropWallpaper(world, pos, facing);
			}
			ItemStack stack = player.getHeldItem(hand);
			mgr.setWallpaper(world, pos, facing, WallpaperData.NONE);
			stack.damageItem(1, player);
			if (!world.isRemote) {
				Wallpaper.instance().getNetwork().sendToPlayers(new MessageWallpaperChange(pos, WallpaperData.NONE, facing), (WorldServer) world, pos.getX() >> 4, pos.getZ() >> 4);
			}
			world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, WallpaperSounds.WALLPAPER_REMOVE, SoundCategory.PLAYERS, 1, world.rand.nextFloat() * 0.3f + 1);
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}
}
