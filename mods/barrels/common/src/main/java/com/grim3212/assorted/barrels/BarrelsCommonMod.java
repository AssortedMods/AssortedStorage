package com.grim3212.assorted.barrels;

import com.grim3212.assorted.barrels.common.block.BarrelsBlocks;
import com.grim3212.assorted.barrels.common.block.blockentity.BarrelsBlockEntityTypes;
import com.grim3212.assorted.barrels.common.crafting.BarrelsRecipeSerializers;
import com.grim3212.assorted.barrels.common.crafting.LockedUpgradingRecipe;
import com.grim3212.assorted.barrels.common.handlers.BarrelsCreativeItems;
import com.grim3212.assorted.barrels.common.handlers.BarrelsLevelUpgrades;
import com.grim3212.assorted.barrels.common.inventory.BarrelsContainerTypes;
import com.grim3212.assorted.barrels.common.item.BarrelsDataComponents;
import com.grim3212.assorted.lib.crafting.SyncedRecipes;
import com.grim3212.assorted.lib.family.Families;
import com.grim3212.assorted.lib.migration.MovedIds;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeType;

public class BarrelsCommonMod {

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");
        Families.join(Constants.MOD_ID, Constants.FAMILY_ID)
                .icon(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "barrel_gold"), 70)
                .manualOrder(80);

        BarrelsDataComponents.init();
        BarrelsBlocks.init();
        BarrelsBlockEntityTypes.init();
        BarrelsContainerTypes.init();
        BarrelsRecipeSerializers.init();
        // The manual and JEI read whole recipes. NeoForge sends every crafting recipe;
        // Fabric sends only serializers that were named.
        SyncedRecipes.require(() -> RecipeType.CRAFTING, LockedUpgradingRecipe.SERIALIZER);
        BarrelsLevelUpgrades.init();
        BarrelsCreativeItems.init();

        // Recipes unlocked when this was all one mod carry over to their new ids.
        MovedIds.inherit(Constants.FAMILY_ID, Constants.MOD_ID);
    }
}
