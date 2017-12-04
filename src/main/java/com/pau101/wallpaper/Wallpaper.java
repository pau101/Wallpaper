package com.pau101.wallpaper;

import com.pau101.wallpaper.capability.WorldWallpaperCapability;
import com.pau101.wallpaper.server.command.CommandRemoveWallpaper;
import com.pau101.wallpaper.server.command.CommandSetWallpaper;
import com.pau101.wallpaper.server.net.Network;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

@Mod(modid = Wallpaper.ID, name = Wallpaper.NAME, version = Wallpaper.VERSION)
@Mod.EventBusSubscriber
public final class Wallpaper {
	public static final String ID = "wallpaper";

	public static final String NAME = "Wallpaper";

	public static final String VERSION = "1.0.0";

	public static void main(String[] args) {
		System.out.println(Character.getNumericValue(127236));
	}

	private static final class Holder {
		private static final Wallpaper INSTANCE = new Wallpaper();
	}

	@SidedProxy(clientSide = "com.pau101.wallpaper.client.ClientProxy", serverSide = "com.pau101.wallpaper.server.CommonProxy")
	private static Proxy proxy;

	public static final int PATTERN_HEIGHT = 4;

	public static final int PATTERN_LinkeWIDTH = 3;

	@EventHandler
	public void init(FMLPreInitializationEvent event) {
		WorldWallpaperCapability.register();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		BlockRendererDispatcher dispatcher = mc.getBlockRendererDispatcher();
		BlockModelRenderer delegate = dispatcher.getBlockModelRenderer();
		ReflectionHelper.setPrivateValue(BlockRendererDispatcher.class, dispatcher, new BlockModelRenderer(mc.getBlockColors()) {
			@Override
			public boolean renderModel(IBlockAccess world, IBakedModel model, IBlockState state, BlockPos pos, BufferBuilder buf, boolean checkSides) {
				return delegate.renderModel(world, getWallpapedModel(world, model, pos), state, pos, buf, checkSides);
			}

			@Override
			public boolean renderModel(IBlockAccess world, IBakedModel model, IBlockState state, BlockPos pos, BufferBuilder buf, boolean checkSides, long rand) {
				return delegate.renderModel(world, getWallpapedModel(world, model, pos), state, pos, buf, checkSides, rand);
			}

			@Override
			public boolean renderModelSmooth(IBlockAccess world, IBakedModel model, IBlockState state, BlockPos pos, BufferBuilder buf, boolean checkSides, long rand) {
				return delegate.renderModelSmooth(world, getWallpapedModel(world, model, pos), state, pos, buf, checkSides, rand);
			}

			@Override
			public boolean renderModelFlat(IBlockAccess world, IBakedModel model, IBlockState state, BlockPos pos, BufferBuilder buf, boolean checkSides, long rand) {
				return delegate.renderModelFlat(world, getWallpapedModel(world, model, pos), state, pos, buf, checkSides, rand);
			}

			@Override
			public void renderModelBrightnessColor(IBakedModel model, float brightness, float red, float green, float blue) {
				delegate.renderModelBrightnessColor(model, brightness, red, green, blue);
			}

			@Override
			public void renderModelBrightnessColor(IBlockState state, IBakedModel model, float brightness, float red, float green, float blue) {
				delegate.renderModelBrightnessColor(state, model, brightness, red, green, blue);
			}

			@Override
			public void renderModelBrightness(IBakedModel model, IBlockState state, float brightness, boolean skipColorBrightness) {
				delegate.renderModelBrightness(model, state, brightness, skipColorBrightness);
			}

			private IBakedModel getWallpapedModel(IBlockAccess world, IBakedModel model, BlockPos pos) {
				if (world instanceof ChunkCache) {
					
				} else if (world instanceof World) {
					
				} else {
					
				}
				return model;
			}
		}, "blockModelRenderer");
	}

	@EventHandler
	public void onServerStarting(FMLServerStartingEvent e) {
		e.registerServerCommand(new CommandSetWallpaper());
		e.registerServerCommand(new CommandRemoveWallpaper());
	}

	public Network getNetwork() {
		return proxy.getNetwork();
	}

	@Mod.InstanceFactory
	public static Wallpaper instance() {
		return Holder.INSTANCE;
	}
}
