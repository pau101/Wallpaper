package com.pau101.wallpaper.server.world;

import java.util.Arrays;

import com.pau101.wallpaper.util.Util;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;

public final class WallpaperBlockData {
	private final WallpaperData[] wallpapers;

	public WallpaperBlockData() {
		wallpapers = new WallpaperData[EnumFacing.values().length];
		Arrays.fill(wallpapers, WallpaperData.NONE);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(wallpapers);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof WallpaperBlockData) {
			WallpaperBlockData other = (WallpaperBlockData) obj;
			return Arrays.equals(wallpapers, other.wallpapers);
		}
		return false;
	}

	@Override
	public String toString() {
		return "WallpaperBlockData [wallpapers=" + Arrays.toString(wallpapers) + "]";
	}

	public void setWallpaper(EnumFacing face, WallpaperData wallpaper) {
		setWallpaper(face.ordinal(), wallpaper);
	}

	public void setWallpaper(int face, WallpaperData wallpaper) {
		wallpapers[face] = wallpaper;
	}

	public void removeWallpaper(EnumFacing face) {
		wallpapers[face.ordinal()] = WallpaperData.NONE;
	}

	public WallpaperData getWallpaper(EnumFacing face) {
		return wallpapers[face.ordinal()];
	}

	public WallpaperData[] getWallpapers() {
		return wallpapers;
	}

	public int getWallpaperCount() {
		int count = 0;
		for (WallpaperData wallpaper : wallpapers) {
			if (wallpaper != WallpaperData.NONE) {
				count++;
			}
		}
		return count;
	}

	public void writeToNBT(NBTTagCompound tagCompound) {
		NBTTagList tagList = new NBTTagList();
		for (WallpaperData wallpaper : wallpapers) {
			NBTTagCompound wallpaperTagCompound = new NBTTagCompound();
			wallpaper.writeToNBT(wallpaperTagCompound);
			tagList.appendTag(wallpaperTagCompound);
		}
		tagCompound.setTag("wallpapers", tagList);
	}

	public static WallpaperBlockData readFromNBT(NBTTagCompound tagCompound) {
		WallpaperBlockData wallpaperBlockData = new WallpaperBlockData();
		NBTTagList tagList = tagCompound.getTagList("wallpapers", 10);
		for (int i = 0, size = tagList.tagCount(); i < size; i++) {
			wallpaperBlockData.setWallpaper(Util.safeEnumGet(EnumFacing.class, i), WallpaperData.readFromNBT(tagList.getCompoundTagAt(i)));
		}
		return wallpaperBlockData;
	}
}
