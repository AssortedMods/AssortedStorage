package com.grim3212.assorted.containers;

import com.grim3212.assorted.containers.common.block.ContainersBlocks;
import com.grim3212.assorted.containers.common.block.blockentity.ContainersBlockEntityTypes;
import com.grim3212.assorted.containers.common.handlers.ContainersCreativeItems;
import com.grim3212.assorted.containers.common.inventory.ContainersContainerTypes;
import com.grim3212.assorted.containers.common.item.ContainersDataComponents;
import com.grim3212.assorted.lib.migration.MovedIds;

public class ContainersCommonMod {

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");

        ContainersDataComponents.init();
        ContainersBlocks.init();
        ContainersBlockEntityTypes.init();
        ContainersContainerTypes.init();
        ContainersCreativeItems.init();

        // Recipes unlocked when this was all one mod carry over to their new ids.
        MovedIds.inherit(Family.ID, Constants.MOD_ID);
    }
}
