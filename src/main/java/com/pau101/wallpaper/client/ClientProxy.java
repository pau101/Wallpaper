package com.pau101.wallpaper.client;

import java.util.HashMap;
import java.util.Map;

import com.pau101.wallpaper.Proxy;
import com.pau101.wallpaper.Wallpaper;
import com.pau101.wallpaper.server.item.WallpaperItems;
import com.pau101.wallpaper.server.net.clientbound.MessageChunkWallpapers;
import com.pau101.wallpaper.server.net.clientbound.MessageWallpaperChange;
import com.pau101.wallpaper.server.world.WallpaperBlockData;
import com.pau101.wallpaper.server.world.WallpaperData;
import com.pau101.wallpaper.server.world.WallpaperEventHandlerClient;
import com.pau101.wallpaper.server.world.WallpaperManager;
import com.pau101.wallpaper.server.world.WallpaperType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public final class ClientProxy extends Proxy {
	private static WallpaperManager wallpaperManager = new WallpaperManager();

	private static final String[] COLUMN_NAMES = { "left", "middle", "right" };

	private static final String[] ROW_NAMES = { "top", "upperMiddle", "lowerMiddle", "bottom" };

	private static Map<WallpaperType, TextureAtlasSprite[]> spriteMap;

	public void initHandlers() {
		MinecraftForge.EVENT_BUS.register(new WallpaperEventHandlerClient());
	}

	public static void unload(Chunk chunk) {
		wallpaperManager.remove(chunk);
	}

	public static void onReturnChunkWallpapers(MessageChunkWallpapers pkt, MessageContext ctx) {
		World world = Minecraft.getMinecraft().world;
		Chunk chunk = world.getChunkFromChunkCoords(pkt.getPos().x, pkt.getPos().z);
		Map<BlockPos, WallpaperBlockData> wallpapers = pkt.getWallpapers();
		if (wallpapers.isEmpty()) {
			return;
		}
		wallpaperManager.put(chunk, wallpapers);
		int minX = 30000000, minY = 256, minZ = 30000000, maxX = -30000000, maxY = 0, maxZ = -30000000;
		for (BlockPos pos : wallpapers.keySet()) {
			int x = pos.getX(), y = pos.getY(), z = pos.getZ();
			if (x < minX) {
				minX = x;
			}
			if (y < minY) {
				minY = y;
			}
			if (z < minZ) {
				minZ = z;
			}
			if (x > maxX) {
				maxX = x;
			}
			if (y > maxY) {
				maxY = y;
			}
			if (z > maxZ) {
				maxZ = z;
			}
		}
		world.markBlockRangeForRenderUpdate(minX, minY, minZ, maxX, maxY, maxZ);
	}

	public static void onWallpaperPlace(MessageWallpaperChange pkt, MessageContext ctx) {
		if (pkt.getFace() == null) {
			removeWallpapers(Minecraft.getMinecraft().world, pkt.getPos());
		} else {
			setWallpaper(Minecraft.getMinecraft().world, pkt.getPos(), pkt.getFace(), pkt.getData());
		}
	}

	public static void onTextureStitchPre(TextureStitchEvent.Pre e) {
		if (e.getMap() != Minecraft.getMinecraft().getTextureMapBlocks()) {
			return;
		}
		WallpaperType.stream()
			.filter(WallpaperType::isEnabled)
			.forEach(wallpaper -> {
				for (String c : COLUMN_NAMES) {
					for (String r : ROW_NAMES) {
						String f = String.format("wallpapers/%s_%s_%s", wallpaper.getFileName(), c, r);
						e.getMap().registerSprite(new ResourceLocation(Wallpaper.ID, f));
					}
				}
			}
		);
	}

	public static void onTextureStitchPost(TextureStitchEvent.Post e) {
		if (e.getMap() != Minecraft.getMinecraft().getTextureMapBlocks()) {
			return;
		}
		spriteMap = new HashMap<>();
		WallpaperType.stream()
			.filter(WallpaperType::isEnabled)
			.forEach(wallpaper -> {
				TextureAtlasSprite[] sprites = new TextureAtlasSprite[COLUMN_NAMES.length * ROW_NAMES.length];
				spriteMap.put(wallpaper, sprites);
				for (int x = 0; x < COLUMN_NAMES.length; x++) {
					String c = COLUMN_NAMES[x];
					for (int y = 0; y < ROW_NAMES.length; y++) {
						String r = ROW_NAMES[y];
						String f = String.format("wallpapers/%s_%s_%s", wallpaper.getFileName(), c, r);
						TextureAtlasSprite sprite = e.getMap().getAtlasSprite(new ResourceLocation(Wallpaper.ID, f).toString());
						sprites[WallpaperData.combine(x, y)] = sprite;
					}
				}
			}
		);
	}

	public static void removeWallpapers(World world, BlockPos pos) {
		wallpaperManager.removeWallpapers(world, pos);
		world.markBlockRangeForRenderUpdate(pos, pos.add(1, 1, 1));
	}

	public static void setWallpaper(World world, BlockPos pos, EnumFacing face, WallpaperData wallpaper) {
		wallpaperManager.setWallpaper(world, pos, face, wallpaper);
		world.markBlockRangeForRenderUpdate(pos, pos.add(1, 1, 1));
	}

	public static WallpaperData getWallpaper(World world, BlockPos pos, EnumFacing face) {
		return wallpaperManager.getWallpaper(world, pos, face);
	}

	public static WallpaperManager getWallpaperManager() {
		return wallpaperManager;
	}

	private static ThreadLocal<TextureAtlasSprite> overrideEntityFXSprite = new ThreadLocal<TextureAtlasSprite>();

	public static WallpaperBlockData setupWallpaperForRender(IBlockAccess blockAccess, BlockPos pos) {
		World world = Minecraft.getMinecraft().world;
		if (world == null) {
			return null;
		} else {
			return wallpaperManager.getWallpaperBlockData(world, pos);
		}
	}

	public static WallpaperData setupFaceForRender(WallpaperBlockData current, EnumFacing face) {
		if (current == null) {
			return null;
		}
		return current.getWallpaper(face);
	}

	public static WallpaperBlockData setupWallpaperForRender(Object blockAccess, Object pos) {
		return setupWallpaperForRender((IBlockAccess) blockAccess, (BlockPos) pos);
	}

	public static WallpaperData setupFaceForRender(Object current, Object face) {
		return setupFaceForRender((WallpaperBlockData) current, (EnumFacing) face);
	}

	public static int[] adjustVertexDataForRender(WallpaperData wallpaperData, int[] vertexData) {
		if (wallpaperData == null || wallpaperData == WallpaperData.NONE) {
			return vertexData;
		}
		TextureAtlasSprite sprite = (TextureAtlasSprite) getSprite(wallpaperData);
		if (sprite == null) {
			return vertexData;
		}
		int[] adjusted = new int[vertexData.length];
		System.arraycopy(vertexData, 0, adjusted, 0, adjusted.length);
		for (int i = 0; i < 4; i++) {
			float u = i < 2 ? sprite.getMinU() : sprite.getMaxU();
			float v = i % 3 == 0 ? sprite.getMinV() : sprite.getMaxV();
			//adjusted[i * Wallpaper.vertexStride + 4] = Float.floatToRawIntBits(u); TODO: vertex data
			//adjusted[i * Wallpaper.vertexStride + 5] = Float.floatToRawIntBits(v);
		}
		return adjusted;
	}

	public static Object getSprite(WallpaperData wallpaperData) {
		TextureAtlasSprite[] sprites = spriteMap.get(wallpaperData.getType());
		if (sprites == null) {
			return null;
		}
		return sprites[wallpaperData.getPart()];
	}

	public static void setupDiggingFXWallpaperData(World world, BlockPos pos, EnumFacing face) {
		WallpaperData wallpaper = wallpaperManager.getWallpaper(world, pos, face);
		if (wallpaper != WallpaperData.NONE) {
			overrideEntityFXSprite.set(spriteMap.get(wallpaper.getType())[wallpaper.getPart()]);
		}
	}

	public static TextureAtlasSprite getSprite(TextureAtlasSprite sprite) {
		if (overrideEntityFXSprite.get() == null) {
			return sprite;
		}
		return overrideEntityFXSprite.get();
	}

	public static void resetOverrideEntityFXSprite() {
		overrideEntityFXSprite.set(null);
	}

	public static boolean onPickBlock(RayTraceResult target, EntityPlayer player, World world) {
		ItemStack result;
		boolean isCreative = player.capabilities.isCreativeMode;
		TileEntity te = null;
		if (target.typeOfHit == RayTraceResult.Type.BLOCK) {
			IBlockState state = world.getBlockState(target.getBlockPos());
			BlockPos pos = target.getBlockPos();
			if (state.getBlock().isAir(state, world, pos)) {
				return false;
			}
			if (isCreative && GuiScreen.isCtrlKeyDown()) {
				te = world.getTileEntity(pos);
			}
			WallpaperData wallpaper = wallpaperManager.getWallpaper(world, pos, target.sideHit);
			if (wallpaper == WallpaperData.NONE) {
				result = state.getBlock().getPickBlock(state, target, world, pos, player);
			} else {
				result = new ItemStack(WallpaperItems.WALLPAPER, 1, wallpaper.getType().ordinal());
			}
		} else {
			if (target.typeOfHit != RayTraceResult.Type.ENTITY || target.entityHit == null || !isCreative) {
				return false;
			}
			result = target.entityHit.getPickedResult(target);
		}
		if (te != null) {
			NBTTagCompound nbt = new NBTTagCompound();
			te.writeToNBT(nbt);
			result.setTagInfo("BlockEntityTag", nbt);
			NBTTagCompound display = new NBTTagCompound();
			result.setTagInfo("display", display);
			NBTTagList lore = new NBTTagList();
			display.setTag("Lore", lore);
			lore.appendTag(new NBTTagString("(+NBT)"));
		}
		for (int x = 0; x < 9; x++) {
			ItemStack stack = player.inventory.getStackInSlot(x);
			if (stack.isItemEqual(result) && ItemStack.areItemStackTagsEqual(stack, result)) {
				player.inventory.currentItem = x;
				return true;
			}
		}
		if (!isCreative) {
			return false;
		}
		int slot = player.inventory.getFirstEmptyStack();
		if (slot < 0 || slot >= 9) {
			slot = player.inventory.currentItem;
		}
		player.inventory.setInventorySlotContents(slot, result);
		player.inventory.currentItem = slot;
		return true;
	}
}
