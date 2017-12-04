package com.pau101.wallpaper.server.item.group;

import com.pau101.wallpaper.Wallpaper;
import com.pau101.wallpaper.server.item.WallpaperItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public final class ItemGroupWallpaper extends CreativeTabs {
	public static final ItemGroupWallpaper INSTANCE = new ItemGroupWallpaper();

	private ItemGroupWallpaper() {
		super(Wallpaper.ID);
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(WallpaperItems.WALLPAPER);
	}
}
