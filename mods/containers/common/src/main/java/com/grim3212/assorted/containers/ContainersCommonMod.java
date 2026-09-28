package com.grim3212.assorted.containers;

import com.grim3212.assorted.containers.common.block.ContainersBlocks;
import com.grim3212.assorted.containers.common.block.blockentity.ContainersBlockEntityTypes;
import com.grim3212.assorted.containers.common.handlers.ContainersCreativeItems;
import com.grim3212.assorted.containers.common.inventory.ContainersContainerTypes;
import com.grim3212.assorted.containers.common.item.ContainersDataComponents;
import com.grim3212.assorted.lib.family.Families;
import com.grim3212.assorted.lib.migration.MovedIds;
import net.minecraft.resources.Identifier;

public class ContainersCommonMod {

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");
        Families.join(Constants.MOD_ID, Constants.FAMILY_ID)
                .icon(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "wood_cabinet"), 90)
                .manualOrder(80);

        ContainersDataComponents.init();
        ContainersBlocks.init();
        ContainersBlockEntityTypes.init();
        ContainersContainerTypes.init();
        ContainersCreativeItems.init();

        // Recipes unlocked when this was all one mod carry over to their new ids.
        MovedIds.inherit(Constants.FAMILY_ID, Constants.MOD_ID);
    }
}
