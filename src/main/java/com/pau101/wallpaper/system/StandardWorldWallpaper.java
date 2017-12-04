package com.pau101.wallpaper.system;

import com.pau101.wallpaper.capability.WorldWallpaper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;

public final class StandardWorldWallpaper implements WorldWallpaper {
	@Override
	public NBTTagCompound serializeChunk(ChunkPos pos) {
		return new NBTTagCompound();
	}

	@Override
	public void deserializeChunk(ChunkPos pos, NBTTagCompound compound) {

	}
}
