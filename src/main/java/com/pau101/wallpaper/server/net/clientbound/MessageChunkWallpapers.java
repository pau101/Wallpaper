package com.pau101.wallpaper.server.net.clientbound;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.pau101.wallpaper.client.ClientProxy;
import com.pau101.wallpaper.server.net.Message;
import com.pau101.wallpaper.server.world.WallpaperBlockData;
import com.pau101.wallpaper.server.world.WallpaperData;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public final class MessageChunkWallpapers extends Message {
	private ChunkPos pos;

	private Map<BlockPos, WallpaperBlockData> wallpapers;

	public MessageChunkWallpapers() {}

	public MessageChunkWallpapers(ChunkPos pos, Map<BlockPos, WallpaperBlockData> wallpapers) {
		this.pos = pos;
		this.wallpapers = wallpapers;
	}

	public ChunkPos getPos() {
		return pos;
	}

	public Map<BlockPos, WallpaperBlockData> getWallpapers() {
		return wallpapers;
	}

	@Override
	public void serialize(PacketBuffer buffer) {
		buffer.writeInt(pos.x);
		buffer.writeInt(pos.z);
		int size = wallpapers.size();
		buffer.writeInt(size);
		for (Entry<BlockPos, WallpaperBlockData> entry : wallpapers.entrySet()) {
			buffer.writeBlockPos(entry.getKey());
			writeWallpaperBlockData(buffer, entry.getValue());
		}
	}

	@Override
	public void deserialize(PacketBuffer buffer) {
		pos = new ChunkPos(buffer.readInt(), buffer.readInt());
		wallpapers = new HashMap<>();
		int size = buffer.readInt();
		while (size --> 0) {
			BlockPos pos = buffer.readBlockPos();
			WallpaperBlockData data = readWallpaperBlockData(buffer);
			wallpapers.put(pos, data);
		}
	}

	private static void writeWallpaperBlockData(PacketBuffer buffer, WallpaperBlockData wallpaperBlockData) {
		WallpaperData[] wallpapers = wallpaperBlockData.getWallpapers();
		int length = wallpapers.length;
		buffer.writeInt(length);
		for (WallpaperData wallpaper : wallpapers) {
			MessageWallpaperChange.writeWallpaperData(buffer, wallpaper);
		}
	}

	private static WallpaperBlockData readWallpaperBlockData(PacketBuffer buffer) {
		WallpaperBlockData wallpaperBlockData = new WallpaperBlockData();
		int length = buffer.readInt();
		for (int i = 0; i < length; i++) {
			wallpaperBlockData.setWallpaper(i, MessageWallpaperChange.readWallpaperData(buffer));
		}
		return wallpaperBlockData;
	}

	@Override
	public void process(MessageContext ctx) {
		ClientProxy.onReturnChunkWallpapers(this, ctx);
	}
}
