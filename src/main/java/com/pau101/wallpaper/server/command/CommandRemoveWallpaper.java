package com.pau101.wallpaper.server.command;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nullable;

import com.pau101.wallpaper.Wallpaper;
import com.pau101.wallpaper.client.ClientProxy;
import com.pau101.wallpaper.server.ServerProxy;
import com.pau101.wallpaper.server.net.clientbound.MessageWallpaperChange;
import com.pau101.wallpaper.server.world.WallpaperData;
import com.pau101.wallpaper.server.world.WallpaperManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class CommandRemoveWallpaper extends CommandBase {
	@Override
	public String getName() {
		return "removewallpaper";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.removewallpaper.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length > 2) {
			BlockPos pos = parseBlockPos(sender, args, 0, false);
			World world = sender.getEntityWorld();
			if (!world.isBlockLoaded(pos)) {
				throw new CommandException("commands.setblock.outOfWorld");
			}
			EnumFacing face = null;
			if (args.length > 3) {
				face = CommandSetWallpaper.getEnumFacing(args[3]);
				if (face == null) {
					throw new CommandException("commands.setwallpaper.invalidFace", args[3].toLowerCase(Locale.ROOT));
				}
			}
			boolean remote = world.isRemote;
			WallpaperManager mgr = remote ? ClientProxy.getWallpaperManager() : ServerProxy.getWallpaperManager();
			if (mgr.getWallpaperBlockData(world, pos) == null) {
				throw new CommandException("commands.removewallpaper.noWallpaper");
			} else {
				if (face == null) {
					if (mgr.removeWallpapers(world, pos)) {
						if (remote) {
							world.markBlockRangeForRenderUpdate(pos, pos.add(1, 1, 1));
						} else {
							Wallpaper.instance().getNetwork().sendToPlayers(new MessageWallpaperChange(pos, WallpaperData.NONE, null), (WorldServer) world, pos.getX() >> 4, pos.getZ() >> 4);
						}
					}
				} else {
					if (mgr.getWallpaper(world, pos, face) == null) {
						throw new CommandException("commands.removewallpaper.noWallpaper");
					} else {
						CommandSetWallpaper.setWallpaper(remote, world, pos, face, WallpaperData.NONE);
					}
				}
			}
			notifyCommandListener(sender, this, "commands.removewallpaper.success");
		} else {
			throw new CommandException("commands.removewallpaper.usage");
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		if (args.length == 4) {
			return CommandSetWallpaper.getPossibleFaceValues();
		}
		return Collections.emptyList();
	}
}
