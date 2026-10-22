package com.grim3212.assorted.barrels.data;

import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.barrels.Constants;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.barrels.api.BarrelsTags;
import com.grim3212.assorted.barrels.common.block.BarrelsBlocks;
import com.grim3212.assorted.lib.core.storage.barrel.LockedBarrelBlock;
import com.grim3212.assorted.barrels.common.crafting.LockedUpgradingRecipeBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.*;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Map;

public class BarrelsRecipes extends ConditionalRecipeProvider {

    public BarrelsRecipes(BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
        super(recipes, advancements, Constants.MOD_ID);
    }

    // Each barrel names its own condition as it is built.
    @Override
    public void registerConditions() {
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        for (Map.Entry<StorageMaterial, IRegistryObject<LockedBarrelBlock>> barrel : BarrelsBlocks.BARRELS.entrySet()) {
            TagKey<Item> mat = barrel.getKey().getMaterial();
            this.addConditions(itemTagExists(mat), barrel.getValue().getId());

            switch (barrel.getKey().getStorageLevel()) {
                case 1:
                    LockedUpgradingRecipeBuilder.shaped(this.items, barrel.getValue().get(), 1).define('C', BarrelsTags.Items.BARRELS_LEVEL_0).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_barrel", has(BarrelsTags.Items.BARRELS_LEVEL_0)).unlockedBy("has_material", has(mat)).save(this.output, key(barrel.getValue().getId()));
                    break;
                case 2:
                    LockedUpgradingRecipeBuilder.shaped(this.items, barrel.getValue().get(), 1).define('C', BarrelsTags.Items.BARRELS_LEVEL_1).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_barrel", has(BarrelsTags.Items.BARRELS_LEVEL_1)).unlockedBy("has_material", has(mat)).save(this.output, key(barrel.getValue().getId()));
                    break;
                case 3:
                    LockedUpgradingRecipeBuilder.shaped(this.items, barrel.getValue().get(), 1).define('C', BarrelsTags.Items.BARRELS_LEVEL_2).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_barrel", has(BarrelsTags.Items.BARRELS_LEVEL_2)).unlockedBy("has_material", has(mat)).save(this.output, key(barrel.getValue().getId()));
                    break;
                case 4:
                    LockedUpgradingRecipeBuilder.shaped(this.items, barrel.getValue().get(), 1).define('C', BarrelsTags.Items.BARRELS_LEVEL_3).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_barrel", has(BarrelsTags.Items.BARRELS_LEVEL_3)).unlockedBy("has_material", has(mat)).save(this.output, key(barrel.getValue().getId()));
                    break;
                case 5:
                    LockedUpgradingRecipeBuilder.shaped(this.items, barrel.getValue().get(), 1).define('C', BarrelsTags.Items.BARRELS_LEVEL_4).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_barrel", has(BarrelsTags.Items.BARRELS_LEVEL_4)).unlockedBy("has_material", has(mat)).save(this.output, key(barrel.getValue().getId()));
                    break;
                default:
                    LockedUpgradingRecipeBuilder.shaped(this.items, barrel.getValue().get(), 1).define('C', LibCommonTags.Items.BARRELS_WOODEN).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_barrel", has(LibCommonTags.Items.BARRELS_WOODEN)).unlockedBy("has_material", has(mat)).save(this.output, key(barrel.getValue().getId()));
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
