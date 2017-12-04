package com.pau101.wallpaper.server.world;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class WallpaperManager {
	private final Map<Chunk, Map<BlockPos, WallpaperBlockData>> customData = new WeakHashMap<>();

	public void put(Chunk chunk, Map<BlockPos, WallpaperBlockData> wallpapers) {
		customData.put(chunk, wallpapers);
		chunk.setModified(true);
	}

	public void remove(Chunk chunk) {
		customData.remove(chunk);
	}

	public boolean containsChunk(Chunk chunk) {
		return customData.containsKey(chunk);
	}

	public Map<BlockPos, WallpaperBlockData> getWallpapers(Chunk chunk) {
		return customData.get(chunk);
	}

	public boolean removeWallpapers(World world, BlockPos pos) {
		Chunk chunk = world.getChunkFromBlockCoords(pos);
		if (customData.containsKey(chunk)) {
			Map<BlockPos, WallpaperBlockData> wallpapers = customData.get(chunk);
			if (wallpapers.containsKey(pos)) {
				wallpapers.remove(pos);
				if (wallpapers.size() == 0) {
					customData.remove(chunk);
				}
				chunk.setModified(true);
				return true;
			}
		}
		return false;
	}

	public void setWallpaper(World world, BlockPos pos, EnumFacing face, WallpaperData wallpaper) {
		Chunk chunk = world.getChunkFromBlockCoords(pos);
		Map<BlockPos, WallpaperBlockData> wallpapers;
		if (customData.containsKey(chunk)) {
			wallpapers = customData.get(chunk);
		} else {
			wallpapers = new HashMap<>();
			customData.put(chunk, wallpapers);
		}
		WallpaperBlockData wallpaperBlockData;
		if (wallpapers.containsKey(pos)) {
			wallpaperBlockData = wallpapers.get(pos);
		} else {
			wallpaperBlockData = new WallpaperBlockData();
		}
		if (wallpaper == WallpaperData.NONE) {
			wallpaperBlockData.removeWallpaper(face);
			if (wallpaperBlockData.getWallpaperCount() == 0) {
				wallpapers.remove(pos);
				if (wallpapers.size() == 0) {
					customData.remove(chunk);
				}
			}
		} else {
			wallpaperBlockData.setWallpaper(face, wallpaper);
			wallpapers.put(pos, wallpaperBlockData);
		}
		chunk.setModified(true);
	}

	public WallpaperBlockData getWallpaperBlockData(World world, BlockPos pos) {
		Chunk chunk = world.getChunkFromBlockCoords(pos);
		if (chunk.isEmpty()) {
			return null;
		}
		if (customData.containsKey(chunk)) {
			return customData.get(chunk).getOrDefault(pos, null);
		}
		return null;
	}

	public WallpaperData getWallpaper(World world, BlockPos pos, EnumFacing face) {
		Chunk chunk = world.getChunkFromBlockCoords(pos);
		if (chunk.isEmpty()) {
			return WallpaperData.NONE;
		}
		if (customData.containsKey(chunk)) {
			Map<BlockPos, WallpaperBlockData> wallpapers = customData.get(chunk);
			if (wallpapers.containsKey(pos)) {
				return wallpapers.get(pos).getWallpapers()[face.ordinal()];
			}
			return WallpaperData.NONE;
		}
		return WallpaperData.NONE;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		int i = 0;
		for (Entry<Chunk, Map<BlockPos, WallpaperBlockData>> entry : customData.entrySet()) {
			Chunk chunk = entry.getKey();
			Map<BlockPos, WallpaperBlockData> wallpapers = entry.getValue();
			stringBuilder.append(String.format("Chunk (%s, %s)\n", chunk.x, chunk.z));
			int n = 0;
			for (Entry<BlockPos, WallpaperBlockData> entry2 : wallpapers.entrySet()) {
				BlockPos pos = entry2.getKey();
				WallpaperBlockData blockData = entry2.getValue();
				stringBuilder.append("  ");
				stringBuilder.append(String.format("Pos (%s, %s, %s) ", pos.getX(), pos.getY(), pos.getZ()));
				stringBuilder.append(blockData.toString());
				if (++n < wallpapers.size()) {
					stringBuilder.append('\n');
				}
			}
			if (++i < customData.size()) {
				stringBuilder.append('\n');
			}
		}
		return stringBuilder.toString();
	}
}
