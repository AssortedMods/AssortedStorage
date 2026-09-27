package com.grim3212.assorted.shulkers.common.block;

import com.grim3212.assorted.shulkers.common.block.blockentity.LockedShulkerBoxBlockEntity;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class Predicates {

	public static final BlockBehaviour.StatePredicate isShulkerBlock = (p_152653_, level, pos) -> {
		BlockEntity blockentity = level.getBlockEntity(pos);
		if (!(blockentity instanceof LockedShulkerBoxBlockEntity shulkerboxblockentity)) {
			return true;
		} else {
			return shulkerboxblockentity.isClosed();
		}
	};

}
