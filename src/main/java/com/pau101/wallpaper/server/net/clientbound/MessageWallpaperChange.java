package com.pau101.wallpaper.server.net.clientbound;

import com.pau101.wallpaper.client.ClientProxy;
import com.pau101.wallpaper.server.net.Message;
import com.pau101.wallpaper.server.world.WallpaperData;
import com.pau101.wallpaper.server.world.WallpaperType;
import com.pau101.wallpaper.util.Util;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public final class MessageWallpaperChange extends Message {
	private BlockPos pos;

	private WallpaperData data;

	private EnumFacing face;

	public MessageWallpaperChange() {}

	public MessageWallpaperChange(BlockPos pos, WallpaperData data, EnumFacing face) {
		this.pos = pos;
		this.data = data;
		this.face = face;
	}

	public BlockPos getPos() {
		return pos;
	}

	public WallpaperData getData() {
		return data;
	}

	public EnumFacing getFace() {
		return face;
	}

	@Override
	public void serialize(PacketBuffer buffer) {
		buffer.writeBlockPos(pos);
		writeWallpaperData(buffer, data);
		buffer.writeInt(face == null ? -1 : face.ordinal());
	}

	@Override
	public void deserialize(PacketBuffer buffer) {
		pos = buffer.readBlockPos();
		data = readWallpaperData(buffer);
		int faceOrdinal = buffer.readInt();
		face = faceOrdinal == -1 ? null : Util.safeEnumGet(EnumFacing.class, faceOrdinal);
	}

	@Override
	public void process(MessageContext ctx) {
		ClientProxy.onWallpaperPlace(this, ctx);
	}

	static void writeWallpaperData(PacketBuffer buffer, WallpaperData wallpaperData) {
		WallpaperType type = wallpaperData.getType();
		buffer.writeInt(type == null ? -1 : type.ordinal());
		buffer.writeInt(wallpaperData.getPart());
	}

	static WallpaperData readWallpaperData(PacketBuffer buffer) {
		int typeInt = buffer.readInt();
		WallpaperType type = typeInt == -1 ? null : WallpaperType.getWallpaper(typeInt);
		int part = buffer.readInt();
		WallpaperData wallpaper = new WallpaperData(type, part);
		if (wallpaper.equals(WallpaperData.NONE)) {
			wallpaper = WallpaperData.NONE;
		}
		return wallpaper;
	}
}
