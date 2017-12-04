package com.pau101.wallpaper.server.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nullable;

import com.pau101.wallpaper.Wallpaper;
import com.pau101.wallpaper.client.ClientProxy;
import com.pau101.wallpaper.server.ServerProxy;
import com.pau101.wallpaper.server.item.ItemWallpaper;
import com.pau101.wallpaper.server.net.clientbound.MessageWallpaperChange;
import com.pau101.wallpaper.server.world.WallpaperBlockData;
import com.pau101.wallpaper.server.world.WallpaperData;
import com.pau101.wallpaper.server.world.WallpaperManager;
import com.pau101.wallpaper.server.world.WallpaperType;
import com.pau101.wallpaper.util.Util;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class CommandSetWallpaper extends CommandBase {
	@Override
	public String getName() {
		return "setwallpaper";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.setwallpaper.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length > 4) {
			BlockPos pos = parseBlockPos(sender, args, 0, false);
			World world = sender.getEntityWorld();
			if (!world.isBlockLoaded(pos)) {
				throw new CommandException("commands.setblock.outOfWorld");
			}
			EnumFacing face = getEnumFacing(args[3]);
			if (face == null) {
				throw new CommandException("commands.setwallpaper.invalidFace", args[3].toLowerCase(Locale.ROOT));
			}
			if (face.getAxis() == EnumFacing.Axis.Y) {
				throw new CommandException("commands.setwallpaper.nonVerticalFace");
			}
			if (!world.getBlockState(pos).getBlock().isOpaqueCube(world.getBlockState(pos))) {
				throw new CommandException("commands.setwallpaper.nonOpaqueCube");
			}
			WallpaperType wallpaper = WallpaperType.byName(args[4]);
			if (wallpaper == null) {
				throw new CommandException("commands.setwallpaper.invalidWallpaper", args[4]);
			}
			boolean remote = world.isRemote;
			WallpaperManager mgr = remote ? ClientProxy.getWallpaperManager() : ServerProxy.getWallpaperManager();
			int part;
			if (args.length > 6) {
				int partX = Util.mod(parseInt(args[5]), Wallpaper.PATTERN_WIDTH);
				int partY = Util.mod(parseInt(args[6]), Wallpaper.PATTERN_HEIGHT);
				part = WallpaperData.combine(partX, partY);
			} else {
				part = ItemWallpaper.fitPuzzle(mgr, world, pos, face);
			}
			WallpaperBlockData wallpapersAtPos = mgr.getWallpaperBlockData(world, pos);
			WallpaperData newWallpaperData = new WallpaperData(wallpaper, part);
			if (wallpapersAtPos == null) {
				setWallpaper(remote, world, pos, face, newWallpaperData);
			} else {
				WallpaperData existingWallpaper = wallpapersAtPos.getWallpaper(face);
				if (existingWallpaper == WallpaperData.NONE) {
					setWallpaper(remote, world, pos, face, newWallpaperData);
				} else {
					if (existingWallpaper.equals(newWallpaperData)) {
						throw new CommandException("commands.setwallpaper.noChange");
					} else {
						setWallpaper(remote, world, pos, face, newWallpaperData);
					}
				}
			}
			notifyCommandListener(sender, this, "commands.setwallpaper.success");
		} else {
			throw new CommandException("commands.setwallpaper.usage");
		}
	}

	public static void setWallpaper(boolean remote, World world, BlockPos pos, EnumFacing face, WallpaperData wallpaper) {
		if (remote) {
			ClientProxy.setWallpaper(world, pos, face, wallpaper);
		} else {
			ServerProxy.setWallpaper(world, pos, face, wallpaper);
			Wallpaper.instance().getNetwork().sendToPlayers(new MessageWallpaperChange(pos, wallpaper, face), (WorldServer) world, pos.getX() >> 4, pos.getZ() >> 4);
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		switch (args.length) {
			default:
				return Collections.emptyList();
			case 4:
				return getPossibleFaceValues();
			case 5:
				return getPossibleWallpaperValues();
		}
	}

	public static EnumFacing getEnumFacing(String name) {
		EnumFacing[] faces = EnumFacing.values();
		for (EnumFacing potentialFace : faces) {
			if (potentialFace.getName().equalsIgnoreCase(name)) {
				return potentialFace;
			}
		}
		return null;
	}

	public static List<String> getPossibleFaceValues() {
		EnumFacing[] faces = EnumFacing.values();
		List<String> possibleFaceValues = new ArrayList<>();
		for (EnumFacing face : faces) {
			if (face.getAxis() != EnumFacing.Axis.Y) {
				possibleFaceValues.add(face.getName());
			}
		}
		return possibleFaceValues;
	}

	private static List<String> getPossibleWallpaperValues() {
		WallpaperType[] wallpapers = WallpaperType.values();
		List<String> possibleWallpaperValues = new ArrayList<>();
		for (WallpaperType wallpaper : wallpapers) {
			if (!wallpaper.isEnabled()) {
				continue;
			}
			possibleWallpaperValues.add(wallpaper.getFileName());
		}
		return possibleWallpaperValues;
	}
}
