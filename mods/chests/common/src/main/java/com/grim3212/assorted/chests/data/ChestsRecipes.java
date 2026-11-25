package com.grim3212.assorted.chests.data;

import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.chests.Constants;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.chests.api.ChestsTags;
import com.grim3212.assorted.chests.common.block.ChestsBlocks;
import com.grim3212.assorted.lib.core.storage.chest.LockedChestBlock;
import com.grim3212.assorted.chests.common.crafting.LockedUpgradingRecipeBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ChestsRecipes extends ConditionalRecipeProvider {

    public ChestsRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Constants.MOD_ID);
    }

    // Each chest names its own condition as it is built.
    @Override
    public void registerConditions() {
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        for (Map.Entry<StorageMaterial, IRegistryObject<LockedChestBlock>> chest : ChestsBlocks.CHESTS.entrySet()) {
            TagKey<Item> mat = chest.getKey().getMaterial();
            this.addConditions(itemTagExists(mat), chest.getValue().getId());

            switch (chest.getKey().getStorageLevel()) {
                case 1:
                    LockedUpgradingRecipeBuilder.shaped(chest.getValue().get(), 1).define('C', ChestsTags.Items.CHESTS_LEVEL_0).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_chest", has(ChestsTags.Items.CHESTS_LEVEL_0)).unlockedBy("has_material", has(mat)).save(this.output, chest.getValue().getId());
                    break;
                case 2:
                    LockedUpgradingRecipeBuilder.shaped(chest.getValue().get(), 1).define('C', ChestsTags.Items.CHESTS_LEVEL_1).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_chest", has(ChestsTags.Items.CHESTS_LEVEL_1)).unlockedBy("has_material", has(mat)).save(this.output, chest.getValue().getId());
                    break;
                case 3:
                    LockedUpgradingRecipeBuilder.shaped(chest.getValue().get(), 1).define('C', ChestsTags.Items.CHESTS_LEVEL_2).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_chest", has(ChestsTags.Items.CHESTS_LEVEL_2)).unlockedBy("has_material", has(mat)).save(this.output, chest.getValue().getId());
                    break;
                case 4:
                    LockedUpgradingRecipeBuilder.shaped(chest.getValue().get(), 1).define('C', ChestsTags.Items.CHESTS_LEVEL_3).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_chest", has(ChestsTags.Items.CHESTS_LEVEL_3)).unlockedBy("has_material", has(mat)).save(this.output, chest.getValue().getId());
                    break;
                case 5:
                    LockedUpgradingRecipeBuilder.shaped(chest.getValue().get(), 1).define('C', ChestsTags.Items.CHESTS_LEVEL_4).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_chest", has(ChestsTags.Items.CHESTS_LEVEL_4)).unlockedBy("has_material", has(mat)).save(this.output, chest.getValue().getId());
                    break;
                default:
                    LockedUpgradingRecipeBuilder.shaped(chest.getValue().get(), 1).define('C', LibCommonTags.Items.CHESTS_WOODEN).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_chest", has(LibCommonTags.Items.CHESTS_WOODEN)).unlockedBy("has_material", has(mat)).save(this.output, chest.getValue().getId());
                    break;
            }
        }
    }

}
