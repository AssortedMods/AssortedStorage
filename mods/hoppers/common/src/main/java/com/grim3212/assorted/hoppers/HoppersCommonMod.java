package com.grim3212.assorted.hoppers;

import com.grim3212.assorted.hoppers.common.block.HoppersBlocks;
import com.grim3212.assorted.hoppers.common.block.blockentity.HoppersBlockEntityTypes;
import com.grim3212.assorted.hoppers.common.crafting.HoppersRecipeSerializers;
import com.grim3212.assorted.hoppers.common.crafting.LockedUpgradingRecipe;
import com.grim3212.assorted.hoppers.common.handlers.HoppersCreativeItems;
import com.grim3212.assorted.hoppers.common.handlers.HoppersLevelUpgrades;
import com.grim3212.assorted.hoppers.common.inventory.HoppersContainerTypes;
import com.grim3212.assorted.hoppers.common.item.HoppersDataComponents;
import com.grim3212.assorted.hoppers.config.HoppersCommonConfig;
import com.grim3212.assorted.lib.crafting.SyncedRecipes;
import com.grim3212.assorted.lib.migration.MovedIds;
import net.minecraft.world.item.crafting.RecipeType;

public class HoppersCommonMod {

    public static final HoppersCommonConfig COMMON_CONFIG = new HoppersCommonConfig();

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");

        HoppersDataComponents.init();
        HoppersBlocks.init();
        HoppersBlockEntityTypes.init();
        HoppersContainerTypes.init();
        HoppersRecipeSerializers.init();
        // The manual reads whole recipes, and Fabric only sends clients the serializers named here.
        SyncedRecipes.require(() -> RecipeType.CRAFTING, LockedUpgradingRecipe.SERIALIZER);
        HoppersLevelUpgrades.init();
        HoppersCreativeItems.init();

        // Recipes unlocked when this was all one mod carry over to their new ids.
        MovedIds.inherit(Family.ID, Constants.MOD_ID);
    }
}
