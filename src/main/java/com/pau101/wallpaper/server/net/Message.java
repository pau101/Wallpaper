package com.pau101.wallpaper.server.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class Message implements IMessage {
	@Override
	public void toBytes(ByteBuf buf) {
		serialize(new PacketBuffer(buf));
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		deserialize(new PacketBuffer(buf));
	}

	protected abstract void serialize(PacketBuffer buf);

	protected abstract void deserialize(PacketBuffer buf);

	protected abstract void process(MessageContext ctx);
}
