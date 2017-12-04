package com.pau101.wallpaper.client;

import com.pau101.wallpaper.Wallpaper;
import com.pau101.wallpaper.server.item.WallpaperItems;
import com.pau101.wallpaper.server.world.WallpaperType;
import com.pau101.wallpaper.util.Util;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Wallpaper.ID)
public final class WallpaperModels {
	private WallpaperModels() {}

	@SubscribeEvent
	public static void register(ModelRegistryEvent event) {
		WallpaperType.stream().forEach(t -> registerItemState(WallpaperItems.WALLPAPER, t.ordinal(), "type=" + t.getFileName()));
		register(WallpaperItems.WALLPAPER_SCRAPER);
	}

	private static void register(Block block) {
		register(ForgeRegistries.ITEMS.getValue(block.getRegistryName()));
	}

	private static void register(Item item) {
		register(item, 0, "inventory");
	}

	private static void register(Item item, int meta, String variant) {
		register(item, meta, Util.getName(item).toString(), variant);
	}

	private static void registerItemState(Item item, int meta, String variant) {
		ResourceLocation name = Util.getName(item);
		register(item, meta, name.getResourceDomain() + ":item/" + name.getResourcePath(), variant);
	}

	private static void register(Item item, int meta, String location, String variant) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(location, variant));
	}
}
