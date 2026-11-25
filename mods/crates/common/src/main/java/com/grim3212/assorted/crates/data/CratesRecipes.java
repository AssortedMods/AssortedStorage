package com.grim3212.assorted.crates.data;

import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.crates.Constants;
import com.grim3212.assorted.crates.api.CratesTags;
import com.grim3212.assorted.crates.common.block.CratesBlocks;
import com.grim3212.assorted.crates.common.item.CratesItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class CratesRecipes extends ConditionalRecipeProvider {

    public CratesRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Constants.MOD_ID);
    }

    @Override
    public void registerConditions() {
        this.addConditions(itemTagExists(CratesTags.Items.INGOTS_BRONZE), ResourceLocation.parse(CratesBlocks.CRATE_BRIDGE.getId() + "_bronze"));
        this.addConditions(itemTagExists(CratesTags.Items.INGOTS_ALUMINUM), ResourceLocation.parse(CratesBlocks.CRATE_COMPACTING.getId() + "_aluminum"));
        this.addConditions(itemTagExists(CratesTags.Items.INGOTS_STEEL), ResourceLocation.parse(CratesBlocks.CRATE_COMPACTING.getId() + "_steel"));

        this.addConditions(itemTagExists(CratesTags.Items.INGOTS_ALUMINUM), ResourceLocation.parse(CratesItems.ROTATOR_MAJIG.getId() + "_aluminum"), ResourceLocation.parse(CratesItems.ROTATOR_MAJIG.getId() + "_aluminum_alt"));
        this.addConditions(itemTagExists(CratesTags.Items.INGOTS_STEEL), ResourceLocation.parse(CratesItems.ROTATOR_MAJIG.getId() + "_steel"), ResourceLocation.parse(CratesItems.ROTATOR_MAJIG.getId() + "_steel_alt"));
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CratesItems.BLANK_UPGRADE.get(), 1).define('W', CratesTags.Items.PAPER).define('P', ItemTags.PLANKS).pattern("PPP").pattern("PWP").pattern("PPP").unlockedBy("has_paper", has(CratesTags.Items.PAPER)).save(this.output, CratesItems.BLANK_UPGRADE.getId());

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CratesBlocks.CRATE_CONTROLLER.get(), 1).define('R', Items.COMPARATOR).define('C', CratesTags.Items.CRATES).define('D', CratesTags.Items.DEEPSLATE).define('E', LibCommonTags.Items.ENDER_PEARLS).pattern("DDD").pattern("CRC").pattern("DED").unlockedBy("has_crates", has(CratesTags.Items.CRATES)).unlockedBy("has_ender_pearls", has(LibCommonTags.Items.ENDER_PEARLS)).save(this.output, CratesBlocks.CRATE_CONTROLLER.getId());
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CratesBlocks.CRATE_BRIDGE.get(), 1).define('R', Items.REPEATER).define('C', CratesTags.Items.CRATES).define('D', CratesTags.Items.DEEPSLATE).define('I', LibCommonTags.Items.INGOTS_GOLD).pattern("DDD").pattern("RCR").pattern("DID").unlockedBy("has_crates", has(CratesTags.Items.CRATES)).unlockedBy("has_gold", has(LibCommonTags.Items.INGOTS_GOLD)).save(this.output, ResourceLocation.parse(CratesBlocks.CRATE_BRIDGE.getId() + "_gold"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CratesBlocks.CRATE_BRIDGE.get(), 1).define('R', Items.REPEATER).define('C', CratesTags.Items.CRATES).define('D', CratesTags.Items.DEEPSLATE).define('I', LibCommonTags.Items.INGOTS_COPPER).pattern("DDD").pattern("RCR").pattern("DID").unlockedBy("has_crates", has(CratesTags.Items.CRATES)).unlockedBy("has_copper", has(LibCommonTags.Items.INGOTS_COPPER)).save(this.output, ResourceLocation.parse(CratesBlocks.CRATE_BRIDGE.getId() + "_copper"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CratesBlocks.CRATE_BRIDGE.get(), 1).define('R', Items.REPEATER).define('C', CratesTags.Items.CRATES).define('D', CratesTags.Items.DEEPSLATE).define('I', CratesTags.Items.INGOTS_BRONZE).pattern("DDD").pattern("RCR").pattern("DID").unlockedBy("has_crates", has(CratesTags.Items.CRATES)).unlockedBy("has_bronze", has(CratesTags.Items.INGOTS_BRONZE)).save(this.output, ResourceLocation.parse(CratesBlocks.CRATE_BRIDGE.getId() + "_bronze"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CratesBlocks.CRATE_COMPACTING.get(), 1).define('P', CratesTags.Items.PISTONS).define('C', CratesTags.Items.CRATES_TRIPLE).define('D', CratesTags.Items.DEEPSLATE).define('I', LibCommonTags.Items.INGOTS_IRON).pattern("DDD").pattern("PCP").pattern("DID").unlockedBy("has_triple_crate", has(CratesTags.Items.CRATES_TRIPLE)).unlockedBy("has_pistons", has(CratesTags.Items.PISTONS)).save(this.output, ResourceLocation.parse(CratesBlocks.CRATE_COMPACTING.getId() + "_iron"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CratesBlocks.CRATE_COMPACTING.get(), 1).define('P', CratesTags.Items.PISTONS).define('C', CratesTags.Items.CRATES_TRIPLE).define('D', CratesTags.Items.DEEPSLATE).define('I', CratesTags.Items.INGOTS_ALUMINUM).pattern("DDD").pattern("PCP").pattern("DID").unlockedBy("has_triple_crate", has(CratesTags.Items.CRATES_TRIPLE)).unlockedBy("has_pistons", has(CratesTags.Items.PISTONS)).save(this.output, ResourceLocation.parse(CratesBlocks.CRATE_COMPACTING.getId() + "_aluminum"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CratesBlocks.CRATE_COMPACTING.get(), 1).define('P', CratesTags.Items.PISTONS).define('C', CratesTags.Items.CRATES_TRIPLE).define('D', CratesTags.Items.DEEPSLATE).define('I', CratesTags.Items.INGOTS_STEEL).pattern("DDD").pattern("PCP").pattern("DID").unlockedBy("has_triple_crate", has(CratesTags.Items.CRATES_TRIPLE)).unlockedBy("has_pistons", has(CratesTags.Items.PISTONS)).save(this.output, ResourceLocation.parse(CratesBlocks.CRATE_COMPACTING.getId() + "_steel"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CratesItems.AMOUNT_UPGRADE.get(), 1).requires(CratesItems.BLANK_UPGRADE.get()).requires(ItemTags.SIGNS).unlockedBy("has_blank_upgrade", has(CratesItems.BLANK_UPGRADE.get())).save(this.output, CratesItems.AMOUNT_UPGRADE.getId());
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CratesItems.GLOW_UPGRADE.get(), 1).requires(CratesItems.BLANK_UPGRADE.get()).requires(Items.GLOW_INK_SAC).unlockedBy("has_blank_upgrade", has(CratesItems.BLANK_UPGRADE.get())).unlockedBy("has_glow_ink_sac", has(Items.GLOW_INK_SAC)).save(this.output, CratesItems.GLOW_UPGRADE.getId());
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CratesItems.GLOW_UPGRADE.get(), 1).requires(CratesItems.BLANK_UPGRADE.get()).requires(LibCommonTags.Items.DUSTS_GLOWSTONE).unlockedBy("has_blank_upgrade", has(CratesItems.BLANK_UPGRADE.get())).unlockedBy("has_glowstone_dust", has(LibCommonTags.Items.DUSTS_GLOWSTONE)).save(this.output, ResourceLocation.parse(CratesItems.GLOW_UPGRADE.getId() + "_glowstone"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CratesItems.REDSTONE_UPGRADE.get(), 2).define('U', CratesItems.BLANK_UPGRADE.get()).define('D', LibCommonTags.Items.DUSTS_REDSTONE).define('R', Items.REPEATER).define('C', Items.COMPARATOR).pattern("DCD").pattern("RUR").pattern("DCD").unlockedBy("has_blank_upgrade", has(CratesItems.BLANK_UPGRADE.get())).save(this.output, CratesItems.REDSTONE_UPGRADE.getId());
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CratesItems.VOID_UPGRADE.get(), 1).define('U', CratesItems.BLANK_UPGRADE.get()).define('O', LibCommonTags.Items.OBSIDIAN).pattern("OOO").pattern("OUO").pattern("OOO").unlockedBy("has_blank_upgrade", has(CratesItems.BLANK_UPGRADE.get())).save(this.output, CratesItems.VOID_UPGRADE.getId());

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CratesItems.ROTATOR_MAJIG.get(), 1).define('B', LibCommonTags.Items.RODS_BLAZE).define('S', LibCommonTags.Items.RODS_WOODEN).define('I', LibCommonTags.Items.INGOTS_IRON).pattern("I I").pattern(" B ").pattern(" S ").unlockedBy("has_blaze_rod", has(LibCommonTags.Items.RODS_BLAZE)).save(this.output, ResourceLocation.parse(CratesItems.ROTATOR_MAJIG.getId() + "_iron"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CratesItems.ROTATOR_MAJIG.get(), 1).define('B', LibCommonTags.Items.RODS_BLAZE).define('S', LibCommonTags.Items.RODS_WOODEN).define('I', LibCommonTags.Items.INGOTS_IRON).pattern("I I").pattern(" S ").pattern(" B ").unlockedBy("has_blaze_rod", has(LibCommonTags.Items.RODS_BLAZE)).save(this.output, ResourceLocation.parse(CratesItems.ROTATOR_MAJIG.getId() + "_iron_alt"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CratesItems.ROTATOR_MAJIG.get(), 1).define('B', LibCommonTags.Items.RODS_BLAZE).define('S', LibCommonTags.Items.RODS_WOODEN).define('I', CratesTags.Items.INGOTS_ALUMINUM).pattern("I I").pattern(" B ").pattern(" S ").unlockedBy("has_blaze_rod", has(LibCommonTags.Items.RODS_BLAZE)).save(this.output, ResourceLocation.parse(CratesItems.ROTATOR_MAJIG.getId() + "_aluminum"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CratesItems.ROTATOR_MAJIG.get(), 1).define('B', LibCommonTags.Items.RODS_BLAZE).define('S', LibCommonTags.Items.RODS_WOODEN).define('I', CratesTags.Items.INGOTS_ALUMINUM).pattern("I I").pattern(" S ").pattern(" B ").unlockedBy("has_blaze_rod", has(LibCommonTags.Items.RODS_BLAZE)).save(this.output, ResourceLocation.parse(CratesItems.ROTATOR_MAJIG.getId() + "_aluminum_alt"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CratesItems.ROTATOR_MAJIG.get(), 1).define('B', LibCommonTags.Items.RODS_BLAZE).define('S', LibCommonTags.Items.RODS_WOODEN).define('I', CratesTags.Items.INGOTS_STEEL).pattern("I I").pattern(" B ").pattern(" S ").unlockedBy("has_blaze_rod", has(LibCommonTags.Items.RODS_BLAZE)).save(this.output, ResourceLocation.parse(CratesItems.ROTATOR_MAJIG.getId() + "_steel"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CratesItems.ROTATOR_MAJIG.get(), 1).define('B', LibCommonTags.Items.RODS_BLAZE).define('S', LibCommonTags.Items.RODS_WOODEN).define('I', CratesTags.Items.INGOTS_STEEL).pattern("I I").pattern(" S ").pattern(" B ").unlockedBy("has_blaze_rod", has(LibCommonTags.Items.RODS_BLAZE)).save(this.output, ResourceLocation.parse(CratesItems.ROTATOR_MAJIG.getId() + "_steel_alt"));

        for (CratesBlocks.CrateGroup group : CratesBlocks.CRATES) {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, group.SINGLE.get(), 1).define('L', group.getType().getLogTag()).define('C', LibCommonTags.Items.CHESTS_WOODEN).define('D', CratesTags.Items.DEEPSLATE).pattern(" D ").pattern("LCL").pattern(" D ").unlockedBy("has_chest", has(LibCommonTags.Items.CHESTS_WOODEN)).unlockedBy("has_deepslate", has(CratesTags.Items.DEEPSLATE)).save(this.output, group.SINGLE.getId());
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, group.DOUBLE.get(), 1).define('L', group.getType().getLogTag()).define('C', LibCommonTags.Items.CHESTS_WOODEN).define('D', CratesTags.Items.DEEPSLATE).pattern("DCD").pattern("L L").pattern("DCD").unlockedBy("has_chest", has(LibCommonTags.Items.CHESTS_WOODEN)).unlockedBy("has_deepslate", has(CratesTags.Items.DEEPSLATE)).save(this.output, group.DOUBLE.getId());
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, group.TRIPLE.get(), 1).define('L', group.getType().getLogTag()).define('C', LibCommonTags.Items.CHESTS_WOODEN).define('D', CratesTags.Items.DEEPSLATE).pattern(" D ").pattern("LCL").pattern("CDC").unlockedBy("has_chest", has(LibCommonTags.Items.CHESTS_WOODEN)).unlockedBy("has_deepslate", has(CratesTags.Items.DEEPSLATE)).save(this.output, group.TRIPLE.getId());
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, group.QUADRUPLE.get(), 1).define('L', group.getType().getLogTag()).define('C', LibCommonTags.Items.CHESTS_WOODEN).define('D', CratesTags.Items.DEEPSLATE).pattern("CDC").pattern("L L").pattern("CDC").unlockedBy("has_chest", has(LibCommonTags.Items.CHESTS_WOODEN)).unlockedBy("has_deepslate", has(CratesTags.Items.DEEPSLATE)).save(this.output, group.QUADRUPLE.getId());
        }
    }

}
