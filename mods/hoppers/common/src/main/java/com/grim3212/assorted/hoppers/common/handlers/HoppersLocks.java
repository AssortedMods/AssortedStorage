package com.grim3212.assorted.hoppers.common.handlers;

import com.grim3212.assorted.hoppers.common.block.HoppersBlocks;
import com.grim3212.assorted.hoppers.common.block.LockedHopperBlock;
import com.grim3212.assorted.lib.core.inventory.locking.LockConversions;
import net.minecraft.world.level.block.Blocks;

/** What a padlock turns a vanilla hopper into. */
public class HoppersLocks {

    public static void init() {
        LockConversions.register(Blocks.HOPPER, LockConversions.container(HoppersBlocks.LOCKED_HOPPER, (from, to) -> to.setValue(LockedHopperBlock.FACING, from.getValue(LockedHopperBlock.FACING))));
    }
}
