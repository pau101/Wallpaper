package com.pau101.wallpaper.capability;

import java.util.Objects;
import javax.annotation.Nullable;

import com.pau101.wallpaper.Wallpaper;
import com.pau101.wallpaper.server.net.clientbound.MessageChunkWallpapers;
import com.pau101.wallpaper.system.StandardWorldWallpaper;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class WorldWallpaperCapability {
	private WorldWallpaperCapability() {}

	private static final ResourceLocation WORLD_WALLPAPER_ID = new ResourceLocation(Wallpaper.ID, "wallpaper_system");

	@CapabilityInject(WorldWallpaper.class)
	private static final Capability<WorldWallpaper> CAPABILITY = null;

	public static Capability<WorldWallpaper> capability() {
		//noinspection ConstantConditions
		return Objects.requireNonNull(CAPABILITY, "CAPABILITY");
	}

	public static void register() {
		CapabilityManager.INSTANCE.register(WorldWallpaper.class, new WallpaperSystemStorage(), StandardWorldWallpaper::new);
	}

	public static WorldWallpaper get(World world) {
		WorldWallpaper cap = world.getCapability(capability(), null);
		if (cap == null) {
			throw new IllegalStateException(String.format("Missing capability: %s/%s", world.getWorldInfo().getWorldName(), world.provider.getDimensionType().getName()));	
		}
		return cap;
	}

	@SubscribeEvent
	public static void onAttachCapabilities(AttachCapabilitiesEvent<World> event) {
		event.addCapability(WORLD_WALLPAPER_ID, new StandardWorldWallpaper());
	}

	@SubscribeEvent
	public static void onChunkWatch(ChunkWatchEvent.Watch event) {
		Wallpaper.instance().getNetwork().sendToServer(new MessageChunkWallpapers());
	}

	@SubscribeEvent
	public static void onChunkSave(ChunkDataEvent.Save event) {
		event.getData().setTag(Wallpaper.ID, get(event.getWorld()).serializeChunk(event.getChunk().getPos()));
	}

	@SubscribeEvent
	public static void onChunkLoad(ChunkDataEvent.Load event) {
		get(event.getWorld()).deserializeChunk(event.getChunk().getPos(), event.getData().getCompoundTag(Wallpaper.ID));
	}

	private static final class WallpaperSystemStorage implements Capability.IStorage<WorldWallpaper> {
		@Nullable
		@Override
		public NBTBase writeNBT(Capability<WorldWallpaper> capability, WorldWallpaper instance, EnumFacing side) {
			return null;
		}

		@Override
		public void readNBT(Capability<WorldWallpaper> capability, WorldWallpaper instance, EnumFacing side, NBTBase nbt) {}
	}
}
