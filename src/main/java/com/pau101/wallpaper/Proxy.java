package com.pau101.wallpaper;

import java.util.HashMap;
import java.util.Map;

import com.pau101.wallpaper.server.item.WallpaperItems;
import com.pau101.wallpaper.server.net.Message;
import com.pau101.wallpaper.server.net.Network;
import com.pau101.wallpaper.server.net.clientbound.MessageWallpaperChange;
import com.pau101.wallpaper.server.world.WallpaperBlockData;
import com.pau101.wallpaper.server.world.WallpaperData;
import com.pau101.wallpaper.server.world.WallpaperManager;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import static com.pau101.wallpaper.Wallpaper.ID;

public abstract class Proxy {
	protected final Network network = new Network();

	private static WallpaperManager wallpaperManager = new WallpaperManager();

	/*public static void main(String[] args) {
		String recipeFormat = "{\r\n\t\"type\": \"forge:ore_shaped\",\r\n\t\"conditions\": [{ \"type\": \"wallpaper\", \"wallpaper\": \"%s\" }],\r\n\t\"pattern\": [\r\n\t\t\"PA\",\r\n\t\t\"PB\"\r\n\t],\r\n\t\"key\": {\r\n\t\t\"P\": { \"type\": \"forge:ore_dict\", \"ore\": \"paper\" },\r\n\t\t\"A\": { \"type\": \"forge:ore_dict\", \"ore\": \"%s\" },\r\n\t\t\"B\": { \"type\": \"forge:ore_dict\", \"ore\": \"%s\" }\r\n\t},\r\n\t\"result\": {\r\n\t\t\"item\": \"wallpaper\",\r\n\t\t\"data\": %d\r\n\t}\r\n}";
		Path recipes = Paths.get("C:\\Users\\Paul\\Documents\\Minecraft Mods Workspaces\\Wallpaper\\1.12\\src\\main\\resources\\assets\\wallpaper\\recipes\\wallpaper");
		WallpaperType.stream().forEach(t -> {
			String json = String.format(recipeFormat, t.getFileName(), t.getColorOne().getName(), t.getColorTwo().getName(), t.ordinal());
			try {
				Files.write(recipes.resolve(t.getFileName() + ".json"), Collections.singleton(json), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}*/

	public static boolean validBlock(IBlockState block) {
		return block.isNormalCube();
	}

	public static void onBlockStateChange(World world, BlockPos pos, IBlockState blockState) {
		if (!validBlock(blockState)) {
			dropWallpapers(world, pos);
			if (wallpaperManager.removeWallpapers(world, pos)) {
				Chunk chunk = world.getChunkFromBlockCoords(pos);
				Message msg = new MessageWallpaperChange(pos, WallpaperData.NONE, null);
				Wallpaper.instance().getNetwork().sendToPlayers(msg, (WorldServer) world, chunk.x, chunk.z);
			}
		}
	}

	public static void onChunkUnload(Chunk chunk) {
		wallpaperManager.remove(chunk);
	}

	public static void onChunkDataSave(Chunk chunk, NBTTagCompound tagCompound) {
		if (wallpaperManager.containsChunk(chunk)) {
			Map<BlockPos, WallpaperBlockData> wallpapers = wallpaperManager.getWallpapers(chunk);
			NBTTagList wallpapersTagList = new NBTTagList();
			for (Map.Entry<BlockPos, WallpaperBlockData> entry : wallpapers.entrySet()) {
				BlockPos pos = entry.getKey();
				WallpaperBlockData data = entry.getValue();

				NBTTagCompound posTagCompound = new NBTTagCompound();
				posTagCompound.setInteger("x", pos.getX());
				posTagCompound.setInteger("y", pos.getY());
				posTagCompound.setInteger("z", pos.getZ());

				NBTTagCompound dataTagCompound = new NBTTagCompound();
				data.writeToNBT(dataTagCompound);

				NBTTagCompound wallpaperTagCompound = new NBTTagCompound();
				wallpaperTagCompound.setTag("pos", posTagCompound);
				wallpaperTagCompound.setTag("data", dataTagCompound);

				wallpapersTagList.appendTag(wallpaperTagCompound);
			}
			tagCompound.setTag(ID, wallpapersTagList);
		}
	}

	public static void onChunkDataLoad(Chunk chunk, NBTTagCompound tagCompound) {
		Map<BlockPos, WallpaperBlockData> wallpapers = new HashMap<>();

		NBTTagList wallpapersTagList = tagCompound.getTagList(ID, 10);
		if (wallpapersTagList.tagCount() == 0) {
			return;
		}
		for (int i = 0, length = wallpapersTagList.tagCount(); i < length; i++) {
			NBTTagCompound wallpaperTagCompound = wallpapersTagList.getCompoundTagAt(i);

			NBTTagCompound posTagCompound = wallpaperTagCompound.getCompoundTag("pos");
			int x = posTagCompound.getInteger("x");
			int y = posTagCompound.getInteger("y");
			int z = posTagCompound.getInteger("z");
			BlockPos pos = new BlockPos(x, y, z);

			NBTTagCompound dataTagCompound = wallpaperTagCompound.getCompoundTag("data");
			WallpaperBlockData data = WallpaperBlockData.readFromNBT(dataTagCompound);

			wallpapers.put(pos, data);
		}

		wallpaperManager.put(chunk, wallpapers);
	}

	public static void setWallpaper(World world, BlockPos pos, EnumFacing face, WallpaperData wallpaper) {
		wallpaperManager.setWallpaper(world, pos, face, wallpaper);
	}

	public static WallpaperData getWallpaper(World world, BlockPos pos, EnumFacing face) {
		return wallpaperManager.getWallpaper(world, pos, face);
	}

	public static WallpaperManager getWallpaperManager() {
		return wallpaperManager;
	}

	public static void dropWallpaper(World world, BlockPos pos, EnumFacing face) {
		WallpaperData wallpaper = wallpaperManager.getWallpaper(world, pos, face);
		if (wallpaper != WallpaperData.NONE) {
			ItemStack itemStack = new ItemStack(WallpaperItems.WALLPAPER, 1, wallpaper.getType().ordinal());
			Block.spawnAsEntity(world, pos.offset(face), itemStack);
		}
	}

	public static void dropWallpapers(World world, BlockPos pos) {
		for (EnumFacing face : EnumFacing.values()) {
			dropWallpaper(world, pos, face);
		}
	}

	public final Network getNetwork() {
		return network;
	}
}
