package com.grim3212.assorted.crates;

import com.grim3212.assorted.crates.common.block.CratesBlocks;
import com.grim3212.assorted.crates.common.block.blockentity.CratesBlockEntityTypes;
import com.grim3212.assorted.crates.common.events.CratesEvents;
import com.grim3212.assorted.crates.common.handlers.CratesCreativeItems;
import com.grim3212.assorted.crates.common.inventory.CratesContainerTypes;
import com.grim3212.assorted.crates.common.item.CratesDataComponents;
import com.grim3212.assorted.crates.common.item.CratesItems;
import com.grim3212.assorted.crates.common.network.CratesPackets;
import com.grim3212.assorted.crates.config.CratesCommonConfig;
import com.grim3212.assorted.lib.migration.MovedIds;

public class CratesCommonMod {

    public static final CratesCommonConfig COMMON_CONFIG = new CratesCommonConfig();

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");

        CratesDataComponents.init();
        CratesBlocks.init();
        CratesBlockEntityTypes.init();
        CratesItems.init();
        CratesContainerTypes.init();
        CratesPackets.init();
        CratesEvents.init();
        CratesCreativeItems.init();

        // Recipes and advancements unlocked when this was all one mod carry over to their new ids.
        MovedIds.inherit(Family.ID, Constants.MOD_ID);
    }
}
