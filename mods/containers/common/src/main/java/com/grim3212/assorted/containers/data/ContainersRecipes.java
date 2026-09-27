package com.grim3212.assorted.containers.data;

import com.grim3212.assorted.containers.Constants;
import com.grim3212.assorted.containers.common.block.ContainersBlocks;
import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public class ContainersRecipes extends ConditionalRecipeProvider {

    private final HolderGetter<Item> items;

    public ContainersRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output, Constants.MOD_ID);
        this.items = registries.lookupOrThrow(Registries.ITEM);
    }

    @Override
    public void registerConditions() {
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.DECORATIONS, ContainersBlocks.WOOD_CABINET.get()).define('X', ItemTags.PLANKS).define('C', LibCommonTags.Items.CHESTS_WOODEN).pattern(" X ").pattern("XCX").pattern(" X ").unlockedBy("has_planks", has(ItemTags.PLANKS)).save(this.output);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.DECORATIONS, ContainersBlocks.GLASS_CABINET.get()).define('X', ItemTags.PLANKS).define('G', LibCommonTags.Items.GLASS).define('C', LibCommonTags.Items.CHESTS_WOODEN).pattern(" X ").pattern("GCG").pattern(" X ").unlockedBy("has_glass", has(LibCommonTags.Items.GLASS)).save(this.output);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.DECORATIONS, ContainersBlocks.GOLD_SAFE.get()).define('G', LibCommonTags.Items.INGOTS_GOLD).define('C', ContainersBlocks.OBSIDIAN_SAFE.get()).pattern(" G ").pattern("GCG").pattern(" G ").unlockedBy("has_obsidian_chest", has(ContainersBlocks.OBSIDIAN_SAFE.get())).unlockedBy("has_gold", has(LibCommonTags.Items.INGOTS_GOLD)).save(this.output);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.DECORATIONS, ContainersBlocks.OBSIDIAN_SAFE.get()).define('G', LibCommonTags.Items.CHESTS_WOODEN).define('X', LibCommonTags.Items.OBSIDIAN).pattern(" X ").pattern("XGX").pattern(" X ").unlockedBy("has_obsidian", has(LibCommonTags.Items.OBSIDIAN)).save(this.output);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.DECORATIONS, ContainersBlocks.LOCKER.get()).define('X', LibCommonTags.Items.INGOTS_IRON).define('C', LibCommonTags.Items.CHESTS_WOODEN).pattern(" X ").pattern("XCX").pattern(" X ").unlockedBy("has_iron", has(LibCommonTags.Items.INGOTS_IRON)).save(this.output);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.DECORATIONS, ContainersBlocks.ITEM_TOWER.get(), 2).define('I', LibCommonTags.Items.INGOTS_IRON).define('C', LibCommonTags.Items.CHESTS_WOODEN).pattern("I I").pattern("ICI").pattern("I I").unlockedBy("has_iron", has(LibCommonTags.Items.INGOTS_IRON)).save(this.output);
        // A warehouse crate is that wood's planks under a row of its logs.
        ContainersBlocks.WAREHOUSE_CRATES.forEach((wood, crate) -> {
            Item planks = wood.getPlanks().asItem();
            ShapedRecipeBuilder.shaped(this.items, RecipeCategory.DECORATIONS, crate.get()).define('P', planks).define('L', wood.getLogTag()).pattern("LLL").pattern("P P").pattern("PPP").unlockedBy("has_" + wood + "_planks", has(planks)).save(this.output);
        });
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
            return new ContainersRecipes(registries, output);
        }

        @Override
        public String getName() {
            return "Recipes: " + Constants.MOD_ID;
        }
    }
}
