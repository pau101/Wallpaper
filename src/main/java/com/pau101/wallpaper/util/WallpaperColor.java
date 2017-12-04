package com.pau101.wallpaper.util;

public enum WallpaperColor {
	WHITE("White"),
	ORANGE("Orange"),
	MAGENTA("Magenta"),
	LIGHT_BLUE("LightBlue"),
	YELLOW("Yellow"),
	LIME("Lime"),
	PINK("Pink"),
	GRAY("Gray"),
	LIGHT_GRAY("LightGray"),
	CYAN("Cyan"),
	PURPLE("Purple"),
	BLUE("Blue"),
	BROWN("Brown"),
	GREEN("Green"),
	RED("Red"),
	BLACK("Black");

	private String name;

	WallpaperColor(String name) {
		this.name = "dye" + name;
	}

	public String getName() {
		return name;
	}
}
