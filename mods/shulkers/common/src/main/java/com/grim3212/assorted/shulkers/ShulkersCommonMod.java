package com.grim3212.assorted.shulkers;

import com.grim3212.assorted.lib.crafting.SyncedRecipes;
import com.grim3212.assorted.lib.family.Families;
import com.grim3212.assorted.lib.migration.MovedIds;
import com.grim3212.assorted.shulkers.common.block.ShulkersBlocks;
import com.grim3212.assorted.shulkers.common.block.blockentity.ShulkersBlockEntityTypes;
import com.grim3212.assorted.shulkers.common.crafting.LockedUpgradingRecipe;
import com.grim3212.assorted.shulkers.common.crafting.ShulkersRecipeSerializers;
import com.grim3212.assorted.shulkers.common.handlers.ShulkersCreativeItems;
import com.grim3212.assorted.shulkers.common.handlers.ShulkersLevelUpgrades;
import com.grim3212.assorted.shulkers.common.inventory.ShulkersContainerTypes;
import com.grim3212.assorted.shulkers.common.item.ShulkersDataComponents;
import com.grim3212.assorted.shulkers.config.ShulkersCommonConfig;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeType;

public class ShulkersCommonMod {

    public static final ShulkersCommonConfig COMMON_CONFIG = new ShulkersCommonConfig();

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");
        Families.join(Constants.MOD_ID, Constants.FAMILY_ID)
                .icon(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "shulker_box_gold"), 40)
                .manualOrder(80);

        ShulkersDataComponents.init();
        ShulkersBlocks.init();
        ShulkersBlockEntityTypes.init();
        ShulkersContainerTypes.init();
        ShulkersRecipeSerializers.init();
        // The manual reads whole recipes, and Fabric only sends clients the serializers named here.
        SyncedRecipes.require(() -> RecipeType.CRAFTING, LockedUpgradingRecipe.SERIALIZER);
        ShulkersLevelUpgrades.init();
        ShulkersCreativeItems.init();

        // Recipes unlocked when this was all one mod carry over to their new ids.
        MovedIds.inherit(Constants.FAMILY_ID, Constants.MOD_ID);
    }
}
