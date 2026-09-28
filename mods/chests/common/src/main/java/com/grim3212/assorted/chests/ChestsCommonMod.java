package com.grim3212.assorted.chests;

import com.grim3212.assorted.chests.common.block.ChestsBlocks;
import com.grim3212.assorted.chests.common.block.blockentity.ChestsBlockEntityTypes;
import com.grim3212.assorted.chests.common.crafting.ChestsRecipeSerializers;
import com.grim3212.assorted.chests.common.crafting.LockedUpgradingRecipe;
import com.grim3212.assorted.chests.common.handlers.ChestsCreativeItems;
import com.grim3212.assorted.chests.common.handlers.ChestsLevelUpgrades;
import com.grim3212.assorted.chests.common.inventory.ChestsContainerTypes;
import com.grim3212.assorted.chests.common.item.ChestsDataComponents;
import com.grim3212.assorted.chests.config.ChestsCommonConfig;
import com.grim3212.assorted.lib.crafting.SyncedRecipes;
import com.grim3212.assorted.lib.family.Families;
import com.grim3212.assorted.lib.migration.MovedIds;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeType;

public class ChestsCommonMod {

    public static final ChestsCommonConfig COMMON_CONFIG = new ChestsCommonConfig();

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");
        Families.join(Constants.MOD_ID, Constants.FAMILY_ID)
                .icon(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "chest_gold"), 50)
                .manualOrder(80);

        ChestsDataComponents.init();
        ChestsBlocks.init();
        ChestsBlockEntityTypes.init();
        ChestsContainerTypes.init();
        ChestsRecipeSerializers.init();
        // The manual and JEI read whole recipes. NeoForge sends every crafting recipe;
        // Fabric sends only serializers that were named.
        SyncedRecipes.require(() -> RecipeType.CRAFTING, LockedUpgradingRecipe.SERIALIZER);
        ChestsLevelUpgrades.init();
        ChestsCreativeItems.init();

        // Recipes unlocked when this was all one mod carry over to their new ids.
        MovedIds.inherit(Constants.FAMILY_ID, Constants.MOD_ID);
    }
}
