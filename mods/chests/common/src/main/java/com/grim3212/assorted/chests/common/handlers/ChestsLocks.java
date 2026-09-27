package com.grim3212.assorted.chests.common.handlers;

import com.grim3212.assorted.lib.core.inventory.locking.LockConversions;
import com.grim3212.assorted.chests.common.block.ChestsBlocks;
import com.grim3212.assorted.chests.common.block.blockentity.LockedEnderChestBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;

/** What a padlock turns a vanilla chest and ender chest into. */
public class ChestsLocks {

    public static void init() {
        LockConversions.register(Blocks.CHEST, LockConversions.container(ChestsBlocks.LOCKED_CHEST, (from, to) -> to.setValue(HorizontalDirectionalBlock.FACING, from.getValue(HorizontalDirectionalBlock.FACING))));
        LockConversions.register(Blocks.ENDER_CHEST, (level, pos, state, code) -> {
            if (!(level.getBlockEntity(pos) instanceof EnderChestBlockEntity)) {
                return false;
            }

            level.setBlock(pos, ChestsBlocks.LOCKED_ENDER_CHEST.get().defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, state.getValue(HorizontalDirectionalBlock.FACING)), Block.UPDATE_ALL);
            if (level.getBlockEntity(pos) instanceof LockedEnderChestBlockEntity chest) {
                chest.setLockCode(code);
            }

            return true;
        });
    }
}
