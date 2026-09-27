package com.grim3212.assorted.chests.common.crafting;

import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.chests.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.Supplier;

public class ChestsRecipeSerializers {

    public static final RegistryProvider<RecipeSerializer<?>> RECIPES = RegistryProvider.create(Registries.RECIPE_SERIALIZER, Constants.MOD_ID);

    public static final IRegistryObject<RecipeSerializer<LockedEnderChestRecipe>> LOCKED_ENDER_CHEST = register("locked_ender_chest", () -> LockedEnderChestRecipe.SERIALIZER);
    public static final IRegistryObject<RecipeSerializer<LockedChestRecipe>> LOCKED_CHEST = register("locked_chest", () -> LockedChestRecipe.SERIALIZER);
    public static final IRegistryObject<RecipeSerializer<LockedUpgradingRecipe>> LOCKED_UPGRADING = register("locked_upgrading", () -> LockedUpgradingRecipe.SERIALIZER);

    private static <T extends RecipeSerializer<?>> IRegistryObject<T> register(final String name, final Supplier<T> sup) {
        return RECIPES.register(name, sup);
    }

    public static void init() {
    }
}
