package com.pau101.wallpaper.server.world;

import com.pau101.wallpaper.Wallpaper;
import com.pau101.wallpaper.util.Util;
import net.minecraft.nbt.NBTTagCompound;

public final class WallpaperData {
	public static final WallpaperData NONE = new WallpaperData(null, -1);

	private WallpaperType type;
	private int part;

	public WallpaperData(WallpaperType type, int part) {
		this.type = type;
		this.part = part;
	}

	public WallpaperData(WallpaperType type, int partX, int partY) {
		this(type, combine(partX, partY));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + part;
		result = prime * result + (type == null ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof WallpaperData) {
			WallpaperData other = (WallpaperData) obj;
			return part == other.part && type == other.type;
		}
		return false;
	}

	@Override
	public String toString() {
		return "WallpaperData [type=" + type + ", part=" + part + "]";
	}

	public WallpaperType getType() {
		return type;
	}

	public void setType(WallpaperType type) {
		this.type = type;
	}

	public int getPart() {
		return part;
	}

	public void setPart(int part) {
		this.part = part;
	}

	public int getPartX() {
		return getPartX(part);
	}

	public int getPartY() {
		return getPartY(part);
	}

	public static int getPartX(int part) {
		return part % Wallpaper.PATTERN_WIDTH;
	}

	public static int getPartY(int part) {
		return part / Wallpaper.PATTERN_WIDTH;
	}

	public static int combine(int partX, int partY) {
		partX = Util.mod(partX, Wallpaper.PATTERN_WIDTH);
		partY = Util.mod(partY, Wallpaper.PATTERN_HEIGHT);
		return partY * Wallpaper.PATTERN_WIDTH + partX;
	}

	public void writeToNBT(NBTTagCompound tagCompound) {
		tagCompound.setString("type", type == null ? "null" : type.name());
		tagCompound.setInteger("part", part);
	}

	public static WallpaperData readFromNBT(NBTTagCompound tagCompound) {
		String typeString = tagCompound.getString("type");
		WallpaperType type = "null".equals(typeString) ? null : WallpaperType.valueOf(typeString);
		int part = tagCompound.getInteger("part");
		WallpaperData wallpaper = new WallpaperData(type, part);
		if (wallpaper.equals(WallpaperData.NONE)) {
			wallpaper = WallpaperData.NONE;
		}
		return wallpaper;
	}
}
