package com.pau101.wallpaper.util;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nullable;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

@SuppressWarnings({ "unused", "WeakerAccess" })
public final class Util {
	private Util() {}

	private static final Converter<String, String> UNDRSCR_TO_CML = CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL);

	public static String underScoreToCamel(String value) {
		return UNDRSCR_TO_CML.convert(value);
	}

	public static <I extends Item> I name(I item, String registryName) {
		name(item, registryName, item::setUnlocalizedName);
		return item;
	}

	public static <B extends Block> B name(B block, String registryName) {
		name(block, registryName, block::setUnlocalizedName);
		return block;
	}

	private static <T extends IForgeRegistryEntry.Impl<T>> T name(T entry, String registryName, Consumer<String> unlocalizedNameSetter) {
		entry.setRegistryName(registryName);
		unlocalizedNameSetter.accept(underScoreToCamel(registryName));
		return entry;
	}

	public static ResourceLocation getName(IForgeRegistryEntry<?> entry) {
		return Objects.requireNonNull(entry.getRegistryName());
	}

	public static <S> void ifPlayer(@Nullable S object, Consumer<? super EntityPlayer> action) {
		ifIs(object, EntityPlayer.class, action);
	}

	public static <S> void ifPlayer(@Nullable S object, Predicate<S> condition, Consumer<? super EntityPlayer> action) {
		ifIs(object, condition, EntityPlayer.class, action);
	}

	public static <S, T> void ifIs(@Nullable S object, Class<T> typeClass, Consumer<? super T> action) {
		ifIs(object, s -> true, typeClass, action);
	}

	public static <S, T> void ifIs(@Nullable S object, Predicate<S> condition, Class<T> typeClass, Consumer<? super T> action) {
		if (object != null && typeClass.isAssignableFrom(object.getClass()) && condition.test(object)) {
			//noinspection unchecked
			action.accept((T) object);
		}
	}

	public static <T extends Enum> T safeEnumGet(Class<T> enumClass, int ordinal) {
		return rangeCheckEnumGet(enumClass, ordinal, 0);
	}

	public static <T extends Enum> T rangeCheckEnumGet(Class<T> enumClass, int ordinal, int fallback) {
		T[] values = enumClass.getEnumConstants();
		if (ordinal < 0 || ordinal >= values.length) {
			ordinal = fallback;
		}
		return values[ordinal];
	}

	public static int mod(int a, int b) {
		return (a % b + b) % b;
	}
}
