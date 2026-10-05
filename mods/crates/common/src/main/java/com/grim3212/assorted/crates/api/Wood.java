package com.grim3212.assorted.crates.api;

import java.util.function.Supplier;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.WoodType;

/**
 * Every vanilla {@link WoodType}, in {@code WoodType.values()} order. A wood added here gets its four crates for free,
 * all but the {@code block/crates/<wood>_facing} texture, which the {@code every_vanilla_wood_has_a_family} gametest guards.
 */
public enum Wood {
	OAK(WoodType.OAK, Blocks.OAK_LOG, () -> ItemTags.OAK_LOGS),
	SPRUCE(WoodType.SPRUCE, Blocks.SPRUCE_LOG, () -> ItemTags.SPRUCE_LOGS),
	BIRCH(WoodType.BIRCH, Blocks.BIRCH_LOG, () -> ItemTags.BIRCH_LOGS),
	ACACIA(WoodType.ACACIA, Blocks.ACACIA_LOG, () -> ItemTags.ACACIA_LOGS),
	CHERRY(WoodType.CHERRY, Blocks.CHERRY_LOG, () -> ItemTags.CHERRY_LOGS),
	JUNGLE(WoodType.JUNGLE, Blocks.JUNGLE_LOG, () -> ItemTags.JUNGLE_LOGS),
	DARK_OAK(WoodType.DARK_OAK, Blocks.DARK_OAK_LOG, () -> ItemTags.DARK_OAK_LOGS),
	PALE_OAK(WoodType.PALE_OAK, Blocks.PALE_OAK_LOG, () -> ItemTags.PALE_OAK_LOGS),
	CRIMSON(WoodType.CRIMSON, Blocks.CRIMSON_STEM, () -> ItemTags.CRIMSON_STEMS),
	WARPED(WoodType.WARPED, Blocks.WARPED_STEM, () -> ItemTags.WARPED_STEMS),
	MANGROVE(WoodType.MANGROVE, Blocks.MANGROVE_LOG, () -> ItemTags.MANGROVE_LOGS),
	// Bamboo's "log" is the bamboo block, and its tag is #minecraft:bamboo_blocks rather than a
	// *_logs one. Everything else reads through getLog()/getLogTag(), so nothing else special-cases it.
	BAMBOO(WoodType.BAMBOO, Blocks.BAMBOO_BLOCK, () -> ItemTags.BAMBOO_BLOCKS);

	private final WoodType type;
	private final Block log;
	private final Supplier<TagKey<Item>> logTag;

	Wood(WoodType type, Block log, Supplier<TagKey<Item>> logTag) {
		this.type = type;
		this.log = log;
		this.logTag = logTag;
	}

	public WoodType getType() {
		return type;
	}

	public Block getLog() {
		return log;
	}

	public TagKey<Item> getLogTag() {
		return logTag.get();
	}

	@Override
	public String toString() {
		return this.type.name();
	}

	/**
	 * The vanilla side texture of {@link #getLog()}, which is its registry path: {@code oak_log},
	 * {@code crimson_stem}, {@code bamboo_block}. Taken from the registry rather than spelled out,
	 * because the three families disagree on the suffix.
	 */
	public String getLogTextureName() {
		return BuiltInRegistries.BLOCK.getKey(this.log).getPath();
	}
}
