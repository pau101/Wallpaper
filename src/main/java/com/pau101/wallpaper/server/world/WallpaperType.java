package com.pau101.wallpaper.server.world;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.pau101.wallpaper.util.WallpaperColor;

public enum WallpaperType {
	ANCIENT("ancient", "ancient", WallpaperColor.YELLOW, WallpaperColor.BROWN, true),
	ARCHED_WINDOW("archedWindow", "arched_window", WallpaperColor.WHITE, WallpaperColor.BROWN),
	BACKGAMMON("backgammon", "backgammon", WallpaperColor.RED, WallpaperColor.WHITE),
	BACKYARD_FENCE("backyardFence", "backyard_fence", WallpaperColor.LIGHT_BLUE, WallpaperColor.WHITE),
	BAMBOO("bamboo", "bamboo", WallpaperColor.WHITE, WallpaperColor.LIME),
	BASEMENT("basement", "basement", WallpaperColor.LIGHT_GRAY, WallpaperColor.GRAY),
	BATHHOUSE("bathhouse", "bathhouse", WallpaperColor.BLUE, WallpaperColor.LIGHT_BLUE),
	BLUE("blue", "blue", WallpaperColor.LIGHT_BLUE, WallpaperColor.YELLOW, true),
	BLUE_TARP("blueTarp", "blue_tarp", WallpaperColor.BLUE, WallpaperColor.LIGHT_GRAY),
	BLUE_TRIM("blueTrim", "blue_trim", WallpaperColor.YELLOW, WallpaperColor.BLUE),
	CABANA("cabana", "cabana", WallpaperColor.ORANGE, WallpaperColor.GREEN),
	CABIN("cabin", "cabin", WallpaperColor.BROWN, WallpaperColor.BROWN),
	CHAINLINK_FENCE("chainlinkFence", "chainlink_fence", WallpaperColor.CYAN, WallpaperColor.GRAY),
	CHIC("chic", "chic", WallpaperColor.YELLOW, WallpaperColor.LIME),
	CITRUS("citrus", "citrus", WallpaperColor.LIME, WallpaperColor.ORANGE),
	CITYSCAPE("cityscape", "cityscape", WallpaperColor.PURPLE, WallpaperColor.YELLOW, true),
	CLASSIC("classic", "classic", WallpaperColor.LIME, WallpaperColor.LIGHT_GRAY),
	CLASSROOM("classroom", "classroom", WallpaperColor.YELLOW, WallpaperColor.CYAN),
	COMMON("common", "common", WallpaperColor.GREEN, WallpaperColor.LIME),
	CONCRETE("concrete", "concrete", WallpaperColor.LIGHT_GRAY, WallpaperColor.LIGHT_GRAY),
	CONVENIENCE("convenience", "convenience", WallpaperColor.WHITE, WallpaperColor.ORANGE),
	CRANNY("cranny", "cranny", WallpaperColor.LIGHT_GRAY, WallpaperColor.BROWN),
	DEPARTMENT("department", "department", WallpaperColor.GREEN, WallpaperColor.WHITE),
	DESERT_VISTA("desertVista", "desert_vista", WallpaperColor.LIGHT_BLUE, WallpaperColor.BROWN),
	DIRT_CLOD("dirtClod", "dirt_clod", WallpaperColor.GRAY, WallpaperColor.RED),
	EGG("egg", "egg", WallpaperColor.ORANGE, WallpaperColor.YELLOW),
	EXOTIC("exotic", "exotic", WallpaperColor.RED, WallpaperColor.ORANGE),
	EXQUISITE("exquisite", "exquisite", WallpaperColor.BLACK, WallpaperColor.GREEN),
	FLORAL("floral", "floral", WallpaperColor.GREEN, WallpaperColor.ORANGE),
	FOREST("forest", "forest", WallpaperColor.RED, WallpaperColor.BROWN, true),
	GARDEN("garden", "garden", WallpaperColor.BROWN, WallpaperColor.GREEN),
	GOLD_SCREEN("goldScreen", "gold_screen", WallpaperColor.BROWN, WallpaperColor.YELLOW),
	GORGEOUS("gorgeous", "gorgeous", WallpaperColor.BLACK, WallpaperColor.RED),
	GRACIE("gracie", "gracie", WallpaperColor.LIME, WallpaperColor.BROWN),
	GREEN("green", "green", WallpaperColor.GREEN, WallpaperColor.BROWN),
	GROOVY("groovy", "groovy", WallpaperColor.PINK, WallpaperColor.ORANGE),
	HARVEST("harvest", "harvest", WallpaperColor.BLUE, WallpaperColor.MAGENTA, true),
	ILLUSION("illusion", "illusion", WallpaperColor.BLACK, WallpaperColor.WHITE),
	IMPERIAL("imperial", "imperial", WallpaperColor.RED, WallpaperColor.GRAY),
	INDUSTRIAL("industrial", "industrial", WallpaperColor.GRAY, WallpaperColor.GRAY),
	IVY("ivy", "ivy", WallpaperColor.GREEN, WallpaperColor.RED),
	JINGLE("jingle", "jingle", WallpaperColor.RED, WallpaperColor.GREEN),
	KIDDIE("kiddie", "kiddie", WallpaperColor.LIGHT_BLUE, WallpaperColor.LIME),
	KITCHEN("kitchen", "kitchen", WallpaperColor.WHITE, WallpaperColor.RED),
	KITSCHY("kitschy", "kitschy", WallpaperColor.GRAY, WallpaperColor.YELLOW),
	LAB("lab", "lab", WallpaperColor.LIGHT_GRAY, WallpaperColor.WHITE),
	LATTICE("lattice", "lattice", WallpaperColor.GRAY, WallpaperColor.BROWN, true),
	LIBRARY("library", "library", WallpaperColor.GRAY, WallpaperColor.CYAN, true),
	LOVELY("lovely", "lovely", WallpaperColor.PINK, WallpaperColor.WHITE, true),
	LUNAR_HORIZON("lunarHorizon", "lunar_horizon", WallpaperColor.CYAN, WallpaperColor.LIGHT_BLUE, true),
	MANOR("manor", "manor", WallpaperColor.PINK, WallpaperColor.BROWN, true),
	MEADOW_VISTA("meadowVista", "meadow_vista", WallpaperColor.GRAY, WallpaperColor.GREEN),
	MOD("mod", "mod", WallpaperColor.ORANGE, WallpaperColor.WHITE),
	MODERN("modern", "modern", WallpaperColor.WHITE, WallpaperColor.GRAY),
	MODERN_SCREEN("modernScreen", "modern_screen", WallpaperColor.BROWN, WallpaperColor.LIGHT_GRAY),
	MORTAR("mortar", "mortar", WallpaperColor.LIGHT_GRAY, WallpaperColor.BLUE),
	MOSAIC("mosaic", "mosaic", WallpaperColor.CYAN, WallpaperColor.RED),
	MUSHROOM_MURAL("mushroomMural", "mushroom_mural", WallpaperColor.LIGHT_BLUE, WallpaperColor.ORANGE, true),
	MUSIC_ROOM("musicRoom", "music_room", WallpaperColor.BLACK, WallpaperColor.BROWN),
	OFFICE("office", "office", WallpaperColor.LIGHT_BLUE, WallpaperColor.GRAY),
	OLD("old", "old", WallpaperColor.LIGHT_GRAY, WallpaperColor.LIME),
	OLD_BRICK("oldBrick", "old_brick", WallpaperColor.YELLOW, WallpaperColor.RED),
	ORNATE("ornate", "ornate", WallpaperColor.LIME, WallpaperColor.GREEN),
	PARLOR("parlor", "parlor", WallpaperColor.MAGENTA, WallpaperColor.PINK),
	PAVE("pave", "pave", WallpaperColor.CYAN, WallpaperColor.WHITE),
	PERSIAN("persian", "persian", WallpaperColor.LIME, WallpaperColor.YELLOW),
	PLASTER("plaster", "plaster", WallpaperColor.LIME, WallpaperColor.WHITE),
	PLAYROOM("playroom", "playroom", WallpaperColor.BLUE, WallpaperColor.YELLOW),
	PLAZA("plaza", "plaza", WallpaperColor.WHITE, WallpaperColor.BLACK),
	PRINCESS("princess", "princess", WallpaperColor.PINK, WallpaperColor.LIGHT_BLUE, true),
	RANCH("ranch", "ranch", WallpaperColor.PINK, WallpaperColor.LIME),
	REGAL("regal", "regal", WallpaperColor.YELLOW, WallpaperColor.GRAY),
	RINGSIDE_SEATING("ringsideSeating", "ringside_seating", WallpaperColor.BLACK, WallpaperColor.YELLOW),
	ROBO("robo", "robo", WallpaperColor.LIGHT_GRAY, WallpaperColor.LIGHT_BLUE),
	ROSE("rose", "rose", WallpaperColor.ORANGE, WallpaperColor.LIME),
	SEA_VIEW("seaView", "sea_view", WallpaperColor.CYAN, WallpaperColor.BROWN),
	SHANTY("shanty", "shanty", WallpaperColor.WHITE, WallpaperColor.LIGHT_GRAY),
	SHOJI_SCREEN("shojiScreen", "shoji_screen", WallpaperColor.WHITE, WallpaperColor.YELLOW),
	SKY("sky", "sky", WallpaperColor.BLUE, WallpaperColor.WHITE, true),
	SNOWMAN("snowman", "snowman", WallpaperColor.WHITE, WallpaperColor.CYAN),
	SPOOKY("spooky", "spooky", WallpaperColor.ORANGE, WallpaperColor.PURPLE, true),
	STATELY("stately", "stately", WallpaperColor.YELLOW, WallpaperColor.YELLOW),
	STONE("stone", "stone", WallpaperColor.GRAY, WallpaperColor.WHITE),
	SUPERMARKET("supermarket", "supermarket", WallpaperColor.CYAN, WallpaperColor.PINK),
	SWEETS("sweets", "sweets", WallpaperColor.BROWN, WallpaperColor.RED, true),
	TEA_ROOM("teaRoom", "tea_room", WallpaperColor.GREEN, WallpaperColor.YELLOW),
	TREE_LINED("treeLined", "tree_lined", WallpaperColor.GREEN, WallpaperColor.LIGHT_BLUE),
	TROPICAL_VISTA("tropicalVista", "tropical_vista", WallpaperColor.BLUE, WallpaperColor.CYAN, true),
	WESTERN_VISTA("westernVista", "western_vista", WallpaperColor.LIGHT_BLUE, WallpaperColor.RED),
	WOOD_PANELING("woodPaneling", "wood_paneling", WallpaperColor.ORANGE, WallpaperColor.GRAY);

	private static final WallpaperType[] TYPES = values();

	private static final Map<String, WallpaperType> NAME_LOOKUP = new HashMap<>();

	static {
		for (WallpaperType wallpaper : values()) {
			NAME_LOOKUP.put(wallpaper.getFileName(), wallpaper);
		}
	}

	private String unlocalizedName;

	private String fileName;

	private WallpaperColor colorOne;

	private WallpaperColor colorTwo;

	private boolean enabled;

	WallpaperType(String unlocalizedName, String fileName, WallpaperColor colorOne, WallpaperColor colorTwo) {
		this(unlocalizedName, fileName, colorOne, colorTwo, false);
	}

	WallpaperType(String unlocalizedName, String fileName, WallpaperColor colorOne, WallpaperColor colorTwo, boolean enabled) {
		this.unlocalizedName = unlocalizedName;
		this.fileName = fileName;
		this.colorOne = colorOne;
		this.colorTwo = colorTwo;
		this.enabled = enabled;
	}

	public String getUnlocalizedName() {
		return unlocalizedName;
	}

	public String getFileName() {
		return fileName;
	}

	public WallpaperColor getColorOne() {
		return colorOne;
	}

	public WallpaperColor getColorTwo() {
		return colorTwo;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public static WallpaperType getWallpaper(int index) {
		WallpaperType[] wallpapers = values();
		if (index < 0 || index >= wallpapers.length) {
			index = 0;
		}
		return wallpapers[index];
	}

	@Nullable
	public static WallpaperType byName(@Nullable String name) {
		return name == null ? null : NAME_LOOKUP.get(name.toLowerCase(Locale.ROOT));
	}

	public static Stream<WallpaperType> stream() {
		return Stream.of(TYPES);
	}
}
