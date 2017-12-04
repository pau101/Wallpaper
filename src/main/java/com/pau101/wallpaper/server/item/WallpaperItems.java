package com.pau101.wallpaper.server.item;

import com.pau101.wallpaper.Wallpaper;
import com.pau101.wallpaper.util.Util;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(Wallpaper.ID)
@Mod.EventBusSubscriber(modid = Wallpaper.ID)
public final class WallpaperItems {
	private WallpaperItems() {}

	private static final Item NIL = Items.AIR; 

	public static final Item WALLPAPER = NIL;

	public static final Item WALLPAPER_SCRAPER = NIL;

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(
			Util.name(new ItemWallpaper(), "wallpaper"),
			Util.name(new ItemWallpaperScraper(), "wallpaper_scraper")
		);
	}
}
