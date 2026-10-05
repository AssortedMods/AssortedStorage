package com.grim3212.assorted.locks;

import com.grim3212.assorted.lib.family.Families;
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
import net.minecraft.resources.Identifier;

public class LocksCommonMod {

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");
        Families.join(Constants.MOD_ID, Constants.FAMILY_ID)
                .icon(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "locksmith_lock"), 20)
                .manualOrder(80);

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
        MovedIds.inherit(Constants.FAMILY_ID, Constants.MOD_ID);
    }
}
