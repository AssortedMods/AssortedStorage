package com.grim3212.assorted.locks;

import com.grim3212.assorted.lib.migration.MovedIds;
import com.grim3212.assorted.locks.common.block.LocksBlocks;
import com.grim3212.assorted.locks.common.block.blockentity.LocksBlockEntityTypes;
import com.grim3212.assorted.locks.common.crafting.LocksRecipeSerializers;
import com.grim3212.assorted.locks.common.handlers.LocksCreativeItems;
import com.grim3212.assorted.locks.common.handlers.LocksLocks;
import com.grim3212.assorted.locks.common.inventory.LocksContainerTypes;
import com.grim3212.assorted.locks.common.item.LocksDataComponents;
import com.grim3212.assorted.locks.common.item.LocksItems;
import com.grim3212.assorted.locks.common.loot.LocksLootConditions;
import com.grim3212.assorted.locks.common.loot.LocksLootEntries;
import com.grim3212.assorted.locks.common.network.LocksPackets;

public class LocksCommonMod {

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");

        LocksDataComponents.init();
        LocksBlocks.init();
        LocksBlockEntityTypes.init();
        LocksItems.init();
        LocksContainerTypes.init();
        LocksRecipeSerializers.init();
        LocksPackets.init();
        LocksLootConditions.init();
        LocksLootEntries.init();
        LocksLocks.init();
        LocksCreativeItems.init();

        // Recipes unlocked when this was all one mod carry over to their new ids.
        MovedIds.inherit(Family.ID, Constants.MOD_ID);
    }
}
