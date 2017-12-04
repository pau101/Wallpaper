package com.pau101.wallpaper.server.net;

import java.util.Optional;

import com.pau101.wallpaper.Wallpaper;
import com.pau101.wallpaper.server.net.clientbound.MessageChunkWallpapers;
import com.pau101.wallpaper.server.net.clientbound.MessageWallpaperChange;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public final class Network implements IMessageHandler<Message, IMessage> {
	private final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(Wallpaper.ID);

	public Network() {
		register(MessageChunkWallpapers.class, 0, Side.CLIENT);
		register(MessageWallpaperChange.class, 1, Side.CLIENT);
	}

	public void sendToServer(IMessage message) {
		network.sendToServer(message);
	}

	public void sendToPlayer(IMessage message, EntityPlayerMP player) {
		network.sendTo(message, player);
	}

	public void sendToPlayers(IMessage message, WorldServer world, int chunkX, int chunkZ) {
		Optional.ofNullable(world.getPlayerChunkMap().getEntry(chunkX, chunkZ)).ifPresent(e -> e.sendPacket(network.getPacketFrom(message)));
	}

	public void sendToPlayers(IMessage message, Entity entity) {
		WorldServer world = (WorldServer) entity.world;
		for (EntityPlayer player : world.getEntityTracker().getTrackingPlayers(entity)) {
			sendToPlayer(message, (EntityPlayerMP) player);
		}
		if (entity instanceof EntityPlayerMP) {
			sendToPlayer(message, (EntityPlayerMP) entity);
		}
	}

	@Override
	public IMessage onMessage(Message message, MessageContext ctx) {
		FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> message.process(ctx));
		return null;
	}

	private void register(Class<? extends Message> cls, int id ,Side side) {
		network.registerMessage(this, cls, id, side);
	}
}
