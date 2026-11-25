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
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ShulkersRecipes extends ConditionalRecipeProvider {

    public ShulkersRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Constants.MOD_ID);
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

        SpecialRecipeBuilder.special(LockedShulkerBoxColoring::new).save(this.output, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "shulker_box_coloring"));

        for (Map.Entry<StorageMaterial, IRegistryObject<LockedShulkerBoxBlock>> shulker : ShulkersBlocks.SHULKERS.entrySet()) {
            TagKey<Item> mat = shulker.getKey().getMaterial();

            switch (shulker.getKey().getStorageLevel()) {
                case 1:
                    LockedUpgradingRecipeBuilder.shaped(shulker.getValue().get(), 1).define('C', ShulkersTags.Items.SHULKERS_LEVEL_0).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_shulker", has(ShulkersTags.Items.SHULKERS_LEVEL_0)).unlockedBy("has_material", has(mat)).save(this.output, shulker.getValue().getId());
                    break;
                case 2:
                    LockedUpgradingRecipeBuilder.shaped(shulker.getValue().get(), 1).define('C', ShulkersTags.Items.SHULKERS_LEVEL_1).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_shulker", has(ShulkersTags.Items.SHULKERS_LEVEL_1)).unlockedBy("has_material", has(mat)).save(this.output, shulker.getValue().getId());
                    break;
                case 3:
                    LockedUpgradingRecipeBuilder.shaped(shulker.getValue().get(), 1).define('C', ShulkersTags.Items.SHULKERS_LEVEL_2).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_shulker", has(ShulkersTags.Items.SHULKERS_LEVEL_2)).unlockedBy("has_material", has(mat)).save(this.output, shulker.getValue().getId());
                    break;
                case 4:
                    LockedUpgradingRecipeBuilder.shaped(shulker.getValue().get(), 1).define('C', ShulkersTags.Items.SHULKERS_LEVEL_3).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_shulker", has(ShulkersTags.Items.SHULKERS_LEVEL_3)).unlockedBy("has_material", has(mat)).save(this.output, shulker.getValue().getId());
                    break;
                case 5:
                    LockedUpgradingRecipeBuilder.shaped(shulker.getValue().get(), 1).define('C', ShulkersTags.Items.SHULKERS_LEVEL_4).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_shulker", has(ShulkersTags.Items.SHULKERS_LEVEL_4)).unlockedBy("has_material", has(mat)).save(this.output, shulker.getValue().getId());
                    break;
                default:
                    LockedUpgradingRecipeBuilder.shaped(shulker.getValue().get(), 1).define('C', ShulkersTags.Items.SHULKERS_NORMAL).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_shulker", has(ShulkersTags.Items.SHULKERS_NORMAL)).unlockedBy("has_material", has(mat)).save(this.output, shulker.getValue().getId());
                    break;
            }
        }
    }

}
