package com.grim3212.assorted.levelupgrades.data;

import com.grim3212.assorted.levelupgrades.Constants;
import com.grim3212.assorted.levelupgrades.api.LevelUpgradesTags;
import com.grim3212.assorted.levelupgrades.common.item.LevelUpgradesItems;
import com.grim3212.assorted.levelupgrades.common.item.upgrades.LevelUpgradeItem;
import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class LevelUpgradesRecipes extends ConditionalRecipeProvider {

    private static final String CRATES = "assortedcrates";

    private final HolderLookup.RegistryLookup<Item> items;

    public LevelUpgradesRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output, Constants.MOD_ID);
        this.items = registries.lookupOrThrow(Registries.ITEM);
    }

    @Override
    public void registerConditions() {
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        for (Map.Entry<StorageMaterial, IRegistryObject<LevelUpgradeItem>> levelUpgrade : LevelUpgradesItems.LEVEL_UPGRADES.entrySet()) {
            TagKey<Item> mat = levelUpgrade.getKey().getMaterial();
            Identifier id = levelUpgrade.getValue().getId();
            Identifier fromBlank = Identifier.parse(id + "_from_blank_upgrade");

            this.addConditions(itemTagExists(mat), id);
            ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, levelUpgrade.getValue().get(), 1).define('M', mat).define('P', LevelUpgradesTags.Items.PAPER).pattern("MMM").pattern("MPM").pattern("MMM").unlockedBy("has_paper", has(LevelUpgradesTags.Items.PAPER)).unlockedBy("has_material", has(mat)).save(this.output, key(id));

            // The recipe from before the split, for anyone who has Assorted Crates' blank upgrades to spend.
            this.addConditions(and(modLoaded(CRATES), itemTagExists(mat)), fromBlank);
            ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, levelUpgrade.getValue().get(), 1).define('M', mat).define('B', byId(CRATES, "blank_upgrade")).pattern("MMM").pattern("MBM").pattern("MMM").unlockedBy("has_material", has(mat)).save(this.output, key(fromBlank));
        }
    }

    /**
     * An item of another mod, which is not registered while this mod's data is generated. A stand alone holder
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
            return new LevelUpgradesRecipes(registries, output);
        }

        @Override
        public String getName() {
            return "Recipes: " + Constants.MOD_ID;
        }
    }
}
