package com.grim3212.assorted.hoppers.data;

import com.grim3212.assorted.hoppers.Constants;
import com.grim3212.assorted.hoppers.api.HoppersTags;
import com.grim3212.assorted.hoppers.common.block.HoppersBlocks;
import com.grim3212.assorted.lib.core.storage.hopper.LockedHopperBlock;
import com.grim3212.assorted.hoppers.common.crafting.LockedUpgradingRecipeBuilder;
import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * The hopper recipes. The ones made from a material chest name the chest by id and load only with
 * Assorted Chests, so this mod never needs its code.
 */
public class HoppersRecipes extends ConditionalRecipeProvider {

    private static final String CHESTS = "assortedchests";

    /** Assorted Chests' chests, named by id. */
    private final OtherPartItems otherParts = new OtherPartItems(Constants.MOD_ID);

    public HoppersRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Constants.MOD_ID);
    }

    @Override
    public void registerConditions() {
        for (Map.Entry<StorageMaterial, IRegistryObject<LockedHopperBlock>> hopper : HoppersBlocks.HOPPERS.entrySet()) {
            this.addConditions(modLoaded(CHESTS), ResourceLocation.parse(hopper.getValue().getId() + "_chest"));
            this.addConditions(itemTagExists(hopper.getKey().getMaterial()), hopper.getValue().getId());
        }
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        for (Map.Entry<StorageMaterial, IRegistryObject<LockedHopperBlock>> hopper : HoppersBlocks.HOPPERS.entrySet()) {
            TagKey<Item> mat = hopper.getKey().getMaterial();

            LockedUpgradingRecipeBuilder.shaped(hopper.getValue().get(), 1).define('C', this.otherParts.of(CHESTS, "chest_" + hopper.getKey())).define('M', LibCommonTags.Items.INGOTS_IRON).pattern("M M").pattern("MCM").pattern(" M ").unlockedBy("has_iron", has(LibCommonTags.Items.INGOTS_IRON)).save(this.output, ResourceLocation.parse(hopper.getValue().getId() + "_chest"));

            switch (hopper.getKey().getStorageLevel()) {
                case 1:
                    LockedUpgradingRecipeBuilder.shaped(hopper.getValue().get(), 1).define('C', HoppersTags.Items.HOPPERS_LEVEL_0).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_hopper", has(HoppersTags.Items.HOPPERS_LEVEL_0)).unlockedBy("has_material", has(mat)).save(this.output, hopper.getValue().getId());
                    break;
                case 2:
                    LockedUpgradingRecipeBuilder.shaped(hopper.getValue().get(), 1).define('C', HoppersTags.Items.HOPPERS_LEVEL_1).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_hopper", has(HoppersTags.Items.HOPPERS_LEVEL_1)).unlockedBy("has_material", has(mat)).save(this.output, hopper.getValue().getId());
                    break;
                case 3:
                    LockedUpgradingRecipeBuilder.shaped(hopper.getValue().get(), 1).define('C', HoppersTags.Items.HOPPERS_LEVEL_2).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_hopper", has(HoppersTags.Items.HOPPERS_LEVEL_2)).unlockedBy("has_material", has(mat)).save(this.output, hopper.getValue().getId());
                    break;
                case 4:
                    LockedUpgradingRecipeBuilder.shaped(hopper.getValue().get(), 1).define('C', HoppersTags.Items.HOPPERS_LEVEL_3).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_hopper", has(HoppersTags.Items.HOPPERS_LEVEL_3)).unlockedBy("has_material", has(mat)).save(this.output, hopper.getValue().getId());
                    break;
                case 5:
                    LockedUpgradingRecipeBuilder.shaped(hopper.getValue().get(), 1).define('C', HoppersTags.Items.HOPPERS_LEVEL_4).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_hopper", has(HoppersTags.Items.HOPPERS_LEVEL_4)).unlockedBy("has_material", has(mat)).save(this.output, hopper.getValue().getId());
                    break;
                default:
                    LockedUpgradingRecipeBuilder.shaped(hopper.getValue().get(), 1).define('C', Items.HOPPER).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_hopper", has(Items.HOPPER)).unlockedBy("has_material", has(mat)).save(this.output, hopper.getValue().getId());
                    break;
            }
        }
    }

    /** Writes the other parts' items {@link #otherParts} stood in for. */
    @Override
    public CompletableFuture<?> run(CachedOutput cache, HolderLookup.Provider registries) {
        return super.run(this.otherParts.wrap(cache), registries);
    }
}
