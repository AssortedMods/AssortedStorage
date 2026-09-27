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
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * The hopper recipes. The ones made from a material chest name the chest by id and load only with
 * Assorted Chests, so this mod never needs its code.
 */
public class HoppersRecipes extends ConditionalRecipeProvider {

    private static final String CHESTS = "assortedchests";

    private final HolderLookup.RegistryLookup<Item> items;

    public HoppersRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output, Constants.MOD_ID);
        this.items = registries.lookupOrThrow(Registries.ITEM);
    }

    @Override
    public void registerConditions() {
        for (Map.Entry<StorageMaterial, IRegistryObject<LockedHopperBlock>> hopper : HoppersBlocks.HOPPERS.entrySet()) {
            this.addConditions(modLoaded(CHESTS), Identifier.parse(hopper.getValue().getId() + "_chest"));
            this.addConditions(itemTagExists(hopper.getKey().getMaterial()), hopper.getValue().getId());
        }
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        for (Map.Entry<StorageMaterial, IRegistryObject<LockedHopperBlock>> hopper : HoppersBlocks.HOPPERS.entrySet()) {
            TagKey<Item> mat = hopper.getKey().getMaterial();

            LockedUpgradingRecipeBuilder.shaped(this.items, hopper.getValue().get(), 1).define('C', byId(CHESTS, "chest_" + hopper.getKey())).define('M', LibCommonTags.Items.INGOTS_IRON).pattern("M M").pattern("MCM").pattern(" M ").unlockedBy("has_iron", has(LibCommonTags.Items.INGOTS_IRON)).save(this.output, key(Identifier.parse(hopper.getValue().getId() + "_chest")));

            switch (hopper.getKey().getStorageLevel()) {
                case 1:
                    LockedUpgradingRecipeBuilder.shaped(this.items, hopper.getValue().get(), 1).define('C', HoppersTags.Items.HOPPERS_LEVEL_0).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_hopper", has(HoppersTags.Items.HOPPERS_LEVEL_0)).unlockedBy("has_material", has(mat)).save(this.output, key(hopper.getValue().getId()));
                    break;
                case 2:
                    LockedUpgradingRecipeBuilder.shaped(this.items, hopper.getValue().get(), 1).define('C', HoppersTags.Items.HOPPERS_LEVEL_1).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_hopper", has(HoppersTags.Items.HOPPERS_LEVEL_1)).unlockedBy("has_material", has(mat)).save(this.output, key(hopper.getValue().getId()));
                    break;
                case 3:
                    LockedUpgradingRecipeBuilder.shaped(this.items, hopper.getValue().get(), 1).define('C', HoppersTags.Items.HOPPERS_LEVEL_2).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_hopper", has(HoppersTags.Items.HOPPERS_LEVEL_2)).unlockedBy("has_material", has(mat)).save(this.output, key(hopper.getValue().getId()));
                    break;
                case 4:
                    LockedUpgradingRecipeBuilder.shaped(this.items, hopper.getValue().get(), 1).define('C', HoppersTags.Items.HOPPERS_LEVEL_3).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_hopper", has(HoppersTags.Items.HOPPERS_LEVEL_3)).unlockedBy("has_material", has(mat)).save(this.output, key(hopper.getValue().getId()));
                    break;
                case 5:
                    LockedUpgradingRecipeBuilder.shaped(this.items, hopper.getValue().get(), 1).define('C', HoppersTags.Items.HOPPERS_LEVEL_4).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_hopper", has(HoppersTags.Items.HOPPERS_LEVEL_4)).unlockedBy("has_material", has(mat)).save(this.output, key(hopper.getValue().getId()));
                    break;
                default:
                    LockedUpgradingRecipeBuilder.shaped(this.items, hopper.getValue().get(), 1).define('C', Items.HOPPER).define('M', mat).pattern("MMM").pattern("MCM").pattern("MMM").unlockedBy("has_hopper", has(Items.HOPPER)).unlockedBy("has_material", has(mat)).save(this.output, key(hopper.getValue().getId()));
                    break;
            }
        }
    }

    /**
     * An item of another part, which is not registered while this mod's data is generated. A stand alone holder
     * still writes its id, and nothing here asks it for the item itself.
     */
    private Ingredient byId(String modId, String path) {
        return Ingredient.of(HolderSet.direct(Holder.Reference.createStandAlone(this.items, ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(modId, path)))));
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
            return new HoppersRecipes(registries, output);
        }

        @Override
        public String getName() {
            return "Recipes: " + Constants.MOD_ID;
        }
    }
}
