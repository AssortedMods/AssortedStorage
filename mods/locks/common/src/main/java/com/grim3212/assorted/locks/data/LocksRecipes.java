package com.grim3212.assorted.locks.data;

import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.locks.Constants;
import com.grim3212.assorted.locks.common.block.LocksBlocks;
import com.grim3212.assorted.locks.common.crafting.LockedBarrelRecipe;
import com.grim3212.assorted.locks.common.crafting.LockedChestRecipe;
import com.grim3212.assorted.locks.common.crafting.LockedEnderChestRecipe;
import com.grim3212.assorted.locks.common.crafting.LockedHopperRecipe;
import com.grim3212.assorted.locks.common.crafting.LockedShulkerBoxColoring;
import com.grim3212.assorted.locks.common.crafting.LockedShulkerBoxRecipe;
import com.grim3212.assorted.locks.common.item.LocksItems;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class LocksRecipes extends ConditionalRecipeProvider {

    private final HolderGetter<Item> items;

    public LocksRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output, Constants.MOD_ID);
        this.items = registries.lookupOrThrow(Registries.ITEM);
    }

    @Override
    public void registerConditions() {
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, LocksItems.LOCKSMITH_LOCK.get(), 3).define('X', LibCommonTags.Items.INGOTS_IRON).pattern(" X ").pattern("X X").pattern("XXX").unlockedBy("has_iron", has(LibCommonTags.Items.INGOTS_IRON)).save(this.output);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, LocksItems.LOCKSMITH_KEY.get(), 3).define('X', LibCommonTags.Items.INGOTS_IRON).pattern("XX").pattern("XX").pattern("X ").unlockedBy("has_iron", has(LibCommonTags.Items.INGOTS_IRON)).save(this.output);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, LocksItems.KEY_RING.get(), 1).define('X', LibCommonTags.Items.INGOTS_IRON).define('K', LocksItems.LOCKSMITH_KEY.get()).pattern(" X ").pattern("XKX").pattern(" X ").unlockedBy("has_iron", has(LibCommonTags.Items.INGOTS_IRON)).unlockedBy("has_key", has(LocksItems.LOCKSMITH_KEY.get())).save(this.output);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, LocksBlocks.LOCKSMITH_WORKBENCH.get(), 1).define('L', LocksItems.LOCKSMITH_LOCK.get()).define('K', LocksItems.LOCKSMITH_KEY.get()).define('W', Blocks.CRAFTING_TABLE).pattern("L").pattern("K").pattern("W").unlockedBy("has_lock", has(LocksItems.LOCKSMITH_LOCK.get())).save(this.output);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, LocksBlocks.LOCKSMITH_WORKBENCH.get(), 1).define('L', LocksItems.LOCKSMITH_LOCK.get()).define('K', LocksItems.LOCKSMITH_KEY.get()).define('W', Blocks.CRAFTING_TABLE).pattern("K").pattern("L").pattern("W").unlockedBy("has_lock", has(LocksItems.LOCKSMITH_LOCK.get())).save(this.output, key(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "locksmith_workbench_alt")));

        SpecialRecipeBuilder.special(() -> LockedEnderChestRecipe.INSTANCE).save(this.output, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "locked_ender_chest").toString());
        SpecialRecipeBuilder.special(() -> LockedChestRecipe.INSTANCE).save(this.output, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "locked_chest").toString());
        SpecialRecipeBuilder.special(() -> LockedBarrelRecipe.INSTANCE).save(this.output, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "locked_barrel").toString());
        SpecialRecipeBuilder.special(() -> LockedHopperRecipe.INSTANCE).save(this.output, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "locked_hopper").toString());
        SpecialRecipeBuilder.special(() -> LockedShulkerBoxRecipe.INSTANCE).save(this.output, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "locked_shulker_box").toString());
        SpecialRecipeBuilder.special(() -> LockedShulkerBoxColoring.INSTANCE).save(this.output, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "locked_shulker_box_coloring").toString());
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
            return new LocksRecipes(registries, output);
        }

        @Override
        public String getName() {
            return "Recipes: " + Constants.MOD_ID;
        }
    }
}
