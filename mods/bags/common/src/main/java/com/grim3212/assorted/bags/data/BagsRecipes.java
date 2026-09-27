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
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class BagsRecipes extends ConditionalRecipeProvider {

    // A material chest goes straight into a bag of its material when Assorted Chests is installed.
    private static final String CHESTS = "assortedchests";

    private final HolderLookup.RegistryLookup<Item> items;

    public BagsRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output, Constants.MOD_ID);
        this.items = registries.lookupOrThrow(Registries.ITEM);
    }

    @Override
    public void registerConditions() {
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, BagsItems.ENDER_BAG.get(), 1).define('S', LibCommonTags.Items.STRING).define('C', LibCommonTags.Items.CHESTS_ENDER).define('L', LibCommonTags.Items.LEATHER).pattern("SLS").pattern("LCL").pattern("LLL").unlockedBy("has_leather", has(LibCommonTags.Items.LEATHER)).unlockedBy("has_chest", has(LibCommonTags.Items.CHESTS_ENDER)).save(this.output, key(BagsItems.ENDER_BAG.getId()));

        SpecialRecipeBuilder.special(() -> BagColoringRecipe.INSTANCE).save(this.output, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "bag_coloring").toString());

        LockedUpgradingRecipeBuilder.shaped(this.items, BagsItems.BAG.get(), 1).define('S', LibCommonTags.Items.STRING).define('C', LibCommonTags.Items.CHESTS_WOODEN).define('L', LibCommonTags.Items.LEATHER).pattern("SLS").pattern("LCL").pattern("LLL").unlockedBy("has_leather", has(LibCommonTags.Items.LEATHER)).unlockedBy("has_chest", has(LibCommonTags.Items.CHESTS_WOODEN)).save(this.output, key(BagsItems.BAG.getId()));

        for (Map.Entry<StorageMaterial, IRegistryObject<BagItem>> bag : BagsItems.BAGS.entrySet()) {
            TagKey<Item> mat = bag.getKey().getMaterial();

            this.addConditions(modLoaded(CHESTS), Identifier.parse(bag.getValue().getId() + "_chest"));
            this.addConditions(itemTagExists(mat), bag.getValue().getId());

            LockedUpgradingRecipeBuilder.shaped(this.items, bag.getValue().get(), 1).define('C', byId(CHESTS, "chest_" + bag.getKey())).define('S', LibCommonTags.Items.STRING).define('L', LibCommonTags.Items.LEATHER).pattern("SLS").pattern("LCL").pattern("LLL").unlockedBy("has_leather", has(LibCommonTags.Items.LEATHER)).save(this.output, key(Identifier.parse(bag.getValue().getId() + "_chest")));

            switch (bag.getKey().getStorageLevel()) {
                case 1:
                    LockedUpgradingRecipeBuilder.shaped(this.items, bag.getValue().get(), 1).define('C', BagsTags.Items.BAGS_LEVEL_0).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_bag", has(BagsTags.Items.BAGS_LEVEL_0)).unlockedBy("has_material", has(mat)).save(this.output, key(bag.getValue().getId()));
                    break;
                case 2:
                    LockedUpgradingRecipeBuilder.shaped(this.items, bag.getValue().get(), 1).define('C', BagsTags.Items.BAGS_LEVEL_1).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_bag", has(BagsTags.Items.BAGS_LEVEL_1)).unlockedBy("has_material", has(mat)).save(this.output, key(bag.getValue().getId()));
                    break;
                case 3:
                    LockedUpgradingRecipeBuilder.shaped(this.items, bag.getValue().get(), 1).define('C', BagsTags.Items.BAGS_LEVEL_2).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_bag", has(BagsTags.Items.BAGS_LEVEL_2)).unlockedBy("has_material", has(mat)).save(this.output, key(bag.getValue().getId()));
                    break;
                case 4:
                    LockedUpgradingRecipeBuilder.shaped(this.items, bag.getValue().get(), 1).define('C', BagsTags.Items.BAGS_LEVEL_3).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_bag", has(BagsTags.Items.BAGS_LEVEL_3)).unlockedBy("has_material", has(mat)).save(this.output, key(bag.getValue().getId()));
                    break;
                case 5:
                    LockedUpgradingRecipeBuilder.shaped(this.items, bag.getValue().get(), 1).define('C', BagsTags.Items.BAGS_LEVEL_4).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_bag", has(BagsTags.Items.BAGS_LEVEL_4)).unlockedBy("has_material", has(mat)).save(this.output, key(bag.getValue().getId()));
                    break;
                default:
                    LockedUpgradingRecipeBuilder.shaped(this.items, bag.getValue().get(), 1).define('C', BagsItems.BAG.get()).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_bag", has(BagsItems.BAG.get())).unlockedBy("has_material", has(mat)).save(this.output, key(bag.getValue().getId()));
                    break;
            }
        }
    }

    /**
     * An item of another part, which is not registered while this mod's data is generated. A stand alone holder
     * still writes its id, and nothing here asks it for the item itself.
     */
    private Ingredient byId(String modId, String path) {
        Holder<Item> holder = Holder.Reference.createStandAlone(this.items, ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(modId, path)));
        return Ingredient.of(HolderSet.direct(holder));
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
            return new BagsRecipes(registries, output);
        }

        @Override
        public String getName() {
            return "Recipes: " + Constants.MOD_ID;
        }
    }
}
