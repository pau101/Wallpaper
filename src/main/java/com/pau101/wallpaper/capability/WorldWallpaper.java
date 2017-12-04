package com.pau101.wallpaper.capability;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public interface WorldWallpaper extends ICapabilityProvider {
	NBTTagCompound serializeChunk(ChunkPos pos);

	void deserializeChunk(ChunkPos pos, NBTTagCompound compound);

	@Override
	default boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == WorldWallpaperCapability.capability();
	}

	@Nullable
	@Override
	default <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == WorldWallpaperCapability.capability() ? WorldWallpaperCapability.capability().cast(this) : null;
	}

//	public interface 
}
