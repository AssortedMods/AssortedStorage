package com.grim3212.assorted.locks.api;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.WoodType;

/**
 * Every vanilla {@link WoodType}, in {@code WoodType.values()} order, with the door each locked wooden door stands in
 * for. Only its two textures ({@code block/locked_<wood>_door_bottom} and {@code _top}) have to be drawn.
 */
public enum Wood {
	OAK(WoodType.OAK, Blocks.OAK_DOOR),
	SPRUCE(WoodType.SPRUCE, Blocks.SPRUCE_DOOR),
	BIRCH(WoodType.BIRCH, Blocks.BIRCH_DOOR),
	ACACIA(WoodType.ACACIA, Blocks.ACACIA_DOOR),
	CHERRY(WoodType.CHERRY, Blocks.CHERRY_DOOR),
	JUNGLE(WoodType.JUNGLE, Blocks.JUNGLE_DOOR),
	DARK_OAK(WoodType.DARK_OAK, Blocks.DARK_OAK_DOOR),
	PALE_OAK(WoodType.PALE_OAK, Blocks.PALE_OAK_DOOR),
	CRIMSON(WoodType.CRIMSON, Blocks.CRIMSON_DOOR),
	WARPED(WoodType.WARPED, Blocks.WARPED_DOOR),
	MANGROVE(WoodType.MANGROVE, Blocks.MANGROVE_DOOR),
	BAMBOO(WoodType.BAMBOO, Blocks.BAMBOO_DOOR);

	private final WoodType type;
	private final Block door;

	Wood(WoodType type, Block door) {
		this.type = type;
		this.door = door;
	}

	public WoodType getType() {
		return type;
	}

	/** The vanilla door of this wood, which the matching locked door stands in for. */
	public Block getDoor() {
		return door;
	}

	@Override
	public String toString() {
		return this.type.name();
	}
}
