package com.pau101.wallpaper.server.world;

import com.pau101.wallpaper.client.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WallpaperEventHandlerClient {
	@SubscribeEvent
	public void onTextureStitchPre(TextureStitchEvent.Pre e) {
		if (e.getMap() == Minecraft.getMinecraft().getTextureMapBlocks()) {
			ClientProxy.onTextureStitchPre(e);
		}
	}

	@SubscribeEvent
	public void onTextureStitchPost(TextureStitchEvent.Post e) {
		if (e.getMap() == Minecraft.getMinecraft().getTextureMapBlocks()) {
			ClientProxy.onTextureStitchPost(e);
		}
	}
}
