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
import net.minecraft.advancements.Advancement;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Map;

public class ShulkersRecipes extends ConditionalRecipeProvider {

    public ShulkersRecipes(BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
        super(recipes, advancements, Constants.MOD_ID);
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
}
