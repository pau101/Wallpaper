package com.pau101.wallpaper.server.sound;

import com.pau101.wallpaper.Wallpaper;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import static com.pau101.wallpaper.Wallpaper.ID;

@Mod.EventBusSubscriber(modid = Wallpaper.ID)
public final class WallpaperSounds {
	private WallpaperSounds() {}

	private static final SoundEvent NIL = SoundEvents.ENTITY_PIG_AMBIENT;

	@GameRegistry.ObjectHolder(ID + ":wallpaper.place")
	public static final SoundEvent WALLPAPER_PLACE = NIL;

	@GameRegistry.ObjectHolder(ID + ":wallpaper.remove")
	public static final SoundEvent WALLPAPER_REMOVE = NIL;

	@SubscribeEvent
	public static void register(RegistryEvent.Register<SoundEvent> event) {
		event.getRegistry().registerAll(create("wallpaper.place"), create("wallpaper.remove"));
	}

	private static SoundEvent create(String name) {
		return new SoundEvent(new ResourceLocation(Wallpaper.ID, name)).setRegistryName(name);
	}
}
