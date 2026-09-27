package com.grim3212.assorted.bags.common.crafting;

import com.grim3212.assorted.bags.Constants;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.Supplier;

public class BagsRecipeSerializers {

    public static final RegistryProvider<RecipeSerializer<?>> RECIPES = RegistryProvider.create(Registries.RECIPE_SERIALIZER, Constants.MOD_ID);

    public static final IRegistryObject<RecipeSerializer<LockedUpgradingRecipe>> LOCKED_UPGRADING = register("locked_upgrading", () -> LockedUpgradingRecipe.SERIALIZER);
    public static final IRegistryObject<RecipeSerializer<BagColoringRecipe>> BAG_COLORING = register("bag_coloring", () -> BagColoringRecipe.SERIALIZER);

    private static <T extends RecipeSerializer<?>> IRegistryObject<T> register(final String name, final Supplier<T> sup) {
        return RECIPES.register(name, sup);
    }

    public static void init() {
    }
}
