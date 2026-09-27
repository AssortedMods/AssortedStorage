package com.grim3212.assorted.shulkers.data;

import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.shulkers.Constants;
import com.grim3212.assorted.shulkers.api.ShulkersTags;
import com.grim3212.assorted.lib.core.storage.shulker.LockedShulkerBoxBlock;
import com.grim3212.assorted.shulkers.common.block.ShulkersBlocks;
import com.grim3212.assorted.shulkers.common.crafting.LockedShulkerBoxColoring;
import com.grim3212.assorted.shulkers.common.crafting.LockedUpgradingRecipeBuilder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ShulkersRecipes extends ConditionalRecipeProvider {

    private final HolderGetter<Item> items;

    public ShulkersRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output, Constants.MOD_ID);
        this.items = registries.lookupOrThrow(Registries.ITEM);
    }

    @Override
    public void registerConditions() {
        for (Map.Entry<StorageMaterial, IRegistryObject<LockedShulkerBoxBlock>> shulker : ShulkersBlocks.SHULKERS.entrySet()) {
            this.addConditions(itemTagExists(shulker.getKey().getMaterial()), shulker.getValue().getId());
        }
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        SpecialRecipeBuilder.special(() -> LockedShulkerBoxColoring.INSTANCE).save(this.output, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "shulker_box_coloring").toString());

        for (Map.Entry<StorageMaterial, IRegistryObject<LockedShulkerBoxBlock>> shulker : ShulkersBlocks.SHULKERS.entrySet()) {
            TagKey<Item> mat = shulker.getKey().getMaterial();

            switch (shulker.getKey().getStorageLevel()) {
                case 1:
                    LockedUpgradingRecipeBuilder.shaped(this.items, shulker.getValue().get(), 1).define('C', ShulkersTags.Items.SHULKERS_LEVEL_0).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_shulker", has(ShulkersTags.Items.SHULKERS_LEVEL_0)).unlockedBy("has_material", has(mat)).save(this.output, key(shulker.getValue().getId()));
                    break;
                case 2:
                    LockedUpgradingRecipeBuilder.shaped(this.items, shulker.getValue().get(), 1).define('C', ShulkersTags.Items.SHULKERS_LEVEL_1).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_shulker", has(ShulkersTags.Items.SHULKERS_LEVEL_1)).unlockedBy("has_material", has(mat)).save(this.output, key(shulker.getValue().getId()));
                    break;
                case 3:
                    LockedUpgradingRecipeBuilder.shaped(this.items, shulker.getValue().get(), 1).define('C', ShulkersTags.Items.SHULKERS_LEVEL_2).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_shulker", has(ShulkersTags.Items.SHULKERS_LEVEL_2)).unlockedBy("has_material", has(mat)).save(this.output, key(shulker.getValue().getId()));
                    break;
                case 4:
                    LockedUpgradingRecipeBuilder.shaped(this.items, shulker.getValue().get(), 1).define('C', ShulkersTags.Items.SHULKERS_LEVEL_3).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_shulker", has(ShulkersTags.Items.SHULKERS_LEVEL_3)).unlockedBy("has_material", has(mat)).save(this.output, key(shulker.getValue().getId()));
                    break;
                case 5:
                    LockedUpgradingRecipeBuilder.shaped(this.items, shulker.getValue().get(), 1).define('C', ShulkersTags.Items.SHULKERS_LEVEL_4).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_shulker", has(ShulkersTags.Items.SHULKERS_LEVEL_4)).unlockedBy("has_material", has(mat)).save(this.output, key(shulker.getValue().getId()));
                    break;
                default:
                    LockedUpgradingRecipeBuilder.shaped(this.items, shulker.getValue().get(), 1).define('C', ShulkersTags.Items.SHULKERS_NORMAL).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_shulker", has(ShulkersTags.Items.SHULKERS_NORMAL)).unlockedBy("has_material", has(mat)).save(this.output, key(shulker.getValue().getId()));
                    break;
            }
        }
    }

    /**
     * Recipes are addressed by {@code ResourceKey<Recipe<?>>} rather than a raw id now.
     */
    private static ResourceKey<Recipe<?>> key(Identifier id) {
        return ResourceKey.create(Registries.RECIPE, id);
    }

    /**
     * Recipe providers are not data providers any more - a {@link RecipeProvider.Runner} owns the
     * file writing and builds a fresh provider around the {@link RecipeOutput} it hands out.
     */
    public static class Runner extends ConditionalRecipeProvider.Runner {

        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries, Constants.MOD_ID);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new ShulkersRecipes(registries, output);
        }

        @Override
        public String getName() {
            return "Recipes: " + Constants.MOD_ID;
        }
    }
}
