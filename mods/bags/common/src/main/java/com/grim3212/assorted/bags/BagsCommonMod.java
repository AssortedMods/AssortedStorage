package com.grim3212.assorted.bags;

import com.grim3212.assorted.bags.common.crafting.BagsRecipeSerializers;
import com.grim3212.assorted.bags.common.crafting.LockedUpgradingRecipe;
import com.grim3212.assorted.bags.common.handlers.BagsCreativeItems;
import com.grim3212.assorted.bags.common.inventory.BagsContainerTypes;
import com.grim3212.assorted.bags.common.item.BagsDataComponents;
import com.grim3212.assorted.bags.common.item.BagsItems;
import com.grim3212.assorted.lib.crafting.SyncedRecipes;
import com.grim3212.assorted.lib.family.Families;
import com.grim3212.assorted.lib.migration.MovedIds;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeType;

public class BagsCommonMod {

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");
        Families.join(Constants.MOD_ID, Constants.FAMILY_ID)
                .icon(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "bag"), 60)
                .manualOrder(80);

        BagsDataComponents.init();
        BagsItems.init();
        BagsContainerTypes.init();
        BagsRecipeSerializers.init();
        // Bag pages in the manual and JEI read whole recipes. NeoForge sends every crafting recipe;
        // Fabric sends only serializers that were named.
        SyncedRecipes.require(() -> RecipeType.CRAFTING, LockedUpgradingRecipe.SERIALIZER);
        BagsCreativeItems.init();

        // Recipes unlocked when this was all one mod carry over to their new ids.
        MovedIds.inherit(Constants.FAMILY_ID, Constants.MOD_ID);
    }
}
