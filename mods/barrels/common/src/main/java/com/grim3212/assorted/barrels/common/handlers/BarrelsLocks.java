package com.grim3212.assorted.barrels.common.handlers;

import com.grim3212.assorted.lib.core.inventory.locking.LockConversions;
import com.grim3212.assorted.barrels.common.block.LockedBarrelBlock;
import com.grim3212.assorted.barrels.common.block.BarrelsBlocks;
import net.minecraft.world.level.block.Blocks;

/** What a padlock turns a vanilla barrel into. */
public class BarrelsLocks {

    public static void init() {
        LockConversions.register(Blocks.BARREL, LockConversions.container(BarrelsBlocks.LOCKED_BARREL, (from, to) -> to.setValue(LockedBarrelBlock.FACING, from.getValue(LockedBarrelBlock.FACING))));
    }
}
