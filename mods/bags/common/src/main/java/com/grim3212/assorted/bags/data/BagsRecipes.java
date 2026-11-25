package com.grim3212.assorted.bags.data;

import com.grim3212.assorted.bags.Constants;
import com.grim3212.assorted.bags.api.BagsTags;
import com.grim3212.assorted.bags.common.crafting.BagColoringRecipe;
import com.grim3212.assorted.bags.common.crafting.LockedUpgradingRecipeBuilder;
import com.grim3212.assorted.bags.common.item.BagItem;
import com.grim3212.assorted.bags.common.item.BagsItems;
import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class BagsRecipes extends ConditionalRecipeProvider {

    // A material chest goes straight into a bag of its material when Assorted Chests is installed.
    private static final String CHESTS = "assortedchests";

    /** Assorted Chests' chests, named by id. */
    private final OtherPartItems otherParts = new OtherPartItems(Constants.MOD_ID);

    public BagsRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Constants.MOD_ID);
    }

    @Override
    public void registerConditions() {
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BagsItems.ENDER_BAG.get(), 1).define('S', LibCommonTags.Items.STRING).define('C', LibCommonTags.Items.CHESTS_ENDER).define('L', LibCommonTags.Items.LEATHER).pattern("SLS").pattern("LCL").pattern("LLL").unlockedBy("has_leather", has(LibCommonTags.Items.LEATHER)).unlockedBy("has_chest", has(LibCommonTags.Items.CHESTS_ENDER)).save(this.output, BagsItems.ENDER_BAG.getId());

        SpecialRecipeBuilder.special(BagColoringRecipe::new).save(this.output, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "bag_coloring").toString());

        LockedUpgradingRecipeBuilder.shaped(BagsItems.BAG.get(), 1).define('S', LibCommonTags.Items.STRING).define('C', LibCommonTags.Items.CHESTS_WOODEN).define('L', LibCommonTags.Items.LEATHER).pattern("SLS").pattern("LCL").pattern("LLL").unlockedBy("has_leather", has(LibCommonTags.Items.LEATHER)).unlockedBy("has_chest", has(LibCommonTags.Items.CHESTS_WOODEN)).save(this.output, BagsItems.BAG.getId());

        for (Map.Entry<StorageMaterial, IRegistryObject<BagItem>> bag : BagsItems.BAGS.entrySet()) {
            TagKey<Item> mat = bag.getKey().getMaterial();

            this.addConditions(modLoaded(CHESTS), ResourceLocation.parse(bag.getValue().getId() + "_chest"));
            this.addConditions(itemTagExists(mat), bag.getValue().getId());

            LockedUpgradingRecipeBuilder.shaped(bag.getValue().get(), 1).define('C', this.otherParts.of(CHESTS, "chest_" + bag.getKey())).define('S', LibCommonTags.Items.STRING).define('L', LibCommonTags.Items.LEATHER).pattern("SLS").pattern("LCL").pattern("LLL").unlockedBy("has_leather", has(LibCommonTags.Items.LEATHER)).save(this.output, ResourceLocation.parse(bag.getValue().getId() + "_chest"));

            switch (bag.getKey().getStorageLevel()) {
                case 1:
                    LockedUpgradingRecipeBuilder.shaped(bag.getValue().get(), 1).define('C', BagsTags.Items.BAGS_LEVEL_0).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_bag", has(BagsTags.Items.BAGS_LEVEL_0)).unlockedBy("has_material", has(mat)).save(this.output, bag.getValue().getId());
                    break;
                case 2:
                    LockedUpgradingRecipeBuilder.shaped(bag.getValue().get(), 1).define('C', BagsTags.Items.BAGS_LEVEL_1).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_bag", has(BagsTags.Items.BAGS_LEVEL_1)).unlockedBy("has_material", has(mat)).save(this.output, bag.getValue().getId());
                    break;
                case 3:
                    LockedUpgradingRecipeBuilder.shaped(bag.getValue().get(), 1).define('C', BagsTags.Items.BAGS_LEVEL_2).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_bag", has(BagsTags.Items.BAGS_LEVEL_2)).unlockedBy("has_material", has(mat)).save(this.output, bag.getValue().getId());
                    break;
                case 4:
                    LockedUpgradingRecipeBuilder.shaped(bag.getValue().get(), 1).define('C', BagsTags.Items.BAGS_LEVEL_3).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_bag", has(BagsTags.Items.BAGS_LEVEL_3)).unlockedBy("has_material", has(mat)).save(this.output, bag.getValue().getId());
                    break;
                case 5:
                    LockedUpgradingRecipeBuilder.shaped(bag.getValue().get(), 1).define('C', BagsTags.Items.BAGS_LEVEL_4).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_bag", has(BagsTags.Items.BAGS_LEVEL_4)).unlockedBy("has_material", has(mat)).save(this.output, bag.getValue().getId());
                    break;
                default:
                    LockedUpgradingRecipeBuilder.shaped(bag.getValue().get(), 1).define('C', BagsItems.BAG.get()).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_bag", has(BagsItems.BAG.get())).unlockedBy("has_material", has(mat)).save(this.output, bag.getValue().getId());
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
