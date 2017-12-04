package com.pau101.wallpaper.server.item.crafting;

import java.util.function.BooleanSupplier;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.pau101.wallpaper.server.world.WallpaperType;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

public final class WallpaperConditionFactory implements IConditionFactory {
	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json) {
		String wallpaper = JsonUtils.getString(json, "wallpaper");
		WallpaperType type = WallpaperType.byName(wallpaper);
		if (type == null) {
			throw new JsonSyntaxException("Unknown wallpaper: \"" + wallpaper + "\"");
		}
		return type::isEnabled;
	}
}
