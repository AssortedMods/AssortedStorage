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
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class BarrelsRecipes extends ConditionalRecipeProvider {

    public BarrelsRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Constants.MOD_ID);
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
                    LockedUpgradingRecipeBuilder.shaped(barrel.getValue().get(), 1).define('C', BarrelsTags.Items.BARRELS_LEVEL_0).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_barrel", has(BarrelsTags.Items.BARRELS_LEVEL_0)).unlockedBy("has_material", has(mat)).save(this.output, barrel.getValue().getId());
                    break;
                case 2:
                    LockedUpgradingRecipeBuilder.shaped(barrel.getValue().get(), 1).define('C', BarrelsTags.Items.BARRELS_LEVEL_1).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_barrel", has(BarrelsTags.Items.BARRELS_LEVEL_1)).unlockedBy("has_material", has(mat)).save(this.output, barrel.getValue().getId());
                    break;
                case 3:
                    LockedUpgradingRecipeBuilder.shaped(barrel.getValue().get(), 1).define('C', BarrelsTags.Items.BARRELS_LEVEL_2).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_barrel", has(BarrelsTags.Items.BARRELS_LEVEL_2)).unlockedBy("has_material", has(mat)).save(this.output, barrel.getValue().getId());
                    break;
                case 4:
                    LockedUpgradingRecipeBuilder.shaped(barrel.getValue().get(), 1).define('C', BarrelsTags.Items.BARRELS_LEVEL_3).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_barrel", has(BarrelsTags.Items.BARRELS_LEVEL_3)).unlockedBy("has_material", has(mat)).save(this.output, barrel.getValue().getId());
                    break;
                case 5:
                    LockedUpgradingRecipeBuilder.shaped(barrel.getValue().get(), 1).define('C', BarrelsTags.Items.BARRELS_LEVEL_4).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_barrel", has(BarrelsTags.Items.BARRELS_LEVEL_4)).unlockedBy("has_material", has(mat)).save(this.output, barrel.getValue().getId());
                    break;
                default:
                    LockedUpgradingRecipeBuilder.shaped(barrel.getValue().get(), 1).define('C', LibCommonTags.Items.BARRELS_WOODEN).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_barrel", has(LibCommonTags.Items.BARRELS_WOODEN)).unlockedBy("has_material", has(mat)).save(this.output, barrel.getValue().getId());
                    break;
            }
        }
    }

}
