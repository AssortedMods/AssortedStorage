package com.grim3212.assorted.chests.data;

import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.chests.Constants;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.chests.api.ChestsTags;
import com.grim3212.assorted.chests.common.block.ChestsBlocks;
import com.grim3212.assorted.chests.common.block.LockedChestBlock;
import com.grim3212.assorted.chests.common.crafting.LockedChestRecipe;
import com.grim3212.assorted.chests.common.crafting.LockedEnderChestRecipe;
import com.grim3212.assorted.chests.common.crafting.LockedUpgradingRecipeBuilder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ChestsRecipes extends ConditionalRecipeProvider {

    private final HolderGetter<Item> items;

    public ChestsRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output, Constants.MOD_ID);
        this.items = registries.lookupOrThrow(Registries.ITEM);
    }

    // Each chest names its own condition as it is built.
    @Override
    public void registerConditions() {
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        SpecialRecipeBuilder.special(() -> LockedEnderChestRecipe.INSTANCE).save(this.output, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "locked_ender_chest").toString());
        SpecialRecipeBuilder.special(() -> LockedChestRecipe.INSTANCE).save(this.output, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "locked_chest").toString());

        for (Map.Entry<StorageMaterial, IRegistryObject<LockedChestBlock>> chest : ChestsBlocks.CHESTS.entrySet()) {
            TagKey<Item> mat = chest.getKey().getMaterial();
            this.addConditions(itemTagExists(mat), chest.getValue().getId());

            switch (chest.getKey().getStorageLevel()) {
                case 1:
                    LockedUpgradingRecipeBuilder.shaped(this.items, chest.getValue().get(), 1).define('C', ChestsTags.Items.CHESTS_LEVEL_0).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_chest", has(ChestsTags.Items.CHESTS_LEVEL_0)).unlockedBy("has_material", has(mat)).save(this.output, key(chest.getValue().getId()));
                    break;
                case 2:
                    LockedUpgradingRecipeBuilder.shaped(this.items, chest.getValue().get(), 1).define('C', ChestsTags.Items.CHESTS_LEVEL_1).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_chest", has(ChestsTags.Items.CHESTS_LEVEL_1)).unlockedBy("has_material", has(mat)).save(this.output, key(chest.getValue().getId()));
                    break;
                case 3:
                    LockedUpgradingRecipeBuilder.shaped(this.items, chest.getValue().get(), 1).define('C', ChestsTags.Items.CHESTS_LEVEL_2).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_chest", has(ChestsTags.Items.CHESTS_LEVEL_2)).unlockedBy("has_material", has(mat)).save(this.output, key(chest.getValue().getId()));
                    break;
                case 4:
                    LockedUpgradingRecipeBuilder.shaped(this.items, chest.getValue().get(), 1).define('C', ChestsTags.Items.CHESTS_LEVEL_3).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_chest", has(ChestsTags.Items.CHESTS_LEVEL_3)).unlockedBy("has_material", has(mat)).save(this.output, key(chest.getValue().getId()));
                    break;
                case 5:
                    LockedUpgradingRecipeBuilder.shaped(this.items, chest.getValue().get(), 1).define('C', ChestsTags.Items.CHESTS_LEVEL_4).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_chest", has(ChestsTags.Items.CHESTS_LEVEL_4)).unlockedBy("has_material", has(mat)).save(this.output, key(chest.getValue().getId()));
                    break;
                default:
                    LockedUpgradingRecipeBuilder.shaped(this.items, chest.getValue().get(), 1).define('C', LibCommonTags.Items.CHESTS_WOODEN).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_chest", has(LibCommonTags.Items.CHESTS_WOODEN)).unlockedBy("has_material", has(mat)).save(this.output, key(chest.getValue().getId()));
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
            return new ChestsRecipes(registries, output);
        }

        @Override
        public String getName() {
            return "Recipes: " + Constants.MOD_ID;
        }
    }
}
