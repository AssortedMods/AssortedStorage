package com.grim3212.assorted.levelupgrades.data;

import com.grim3212.assorted.levelupgrades.Constants;
import com.grim3212.assorted.levelupgrades.api.LevelUpgradesTags;
import com.grim3212.assorted.levelupgrades.common.item.LevelUpgradesItems;
import com.grim3212.assorted.levelupgrades.common.item.upgrades.LevelUpgradeItem;
import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Map;

public class LevelUpgradesRecipes extends ConditionalRecipeProvider {

    private static final String CRATES = "assortedcrates";

    public LevelUpgradesRecipes(BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
        super(recipes, advancements, Constants.MOD_ID);
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
}
