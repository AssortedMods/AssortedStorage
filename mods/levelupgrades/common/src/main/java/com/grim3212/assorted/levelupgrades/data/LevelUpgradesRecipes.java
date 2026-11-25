package com.grim3212.assorted.levelupgrades.data;

import com.grim3212.assorted.levelupgrades.Constants;
import com.grim3212.assorted.levelupgrades.api.LevelUpgradesTags;
import com.grim3212.assorted.levelupgrades.common.item.LevelUpgradesItems;
import com.grim3212.assorted.levelupgrades.common.item.upgrades.LevelUpgradeItem;
import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class LevelUpgradesRecipes extends ConditionalRecipeProvider {

    private static final String CRATES = "assortedcrates";

    /** Assorted Crates' blank upgrade, named by id, as this mod never needs Crates' code. */
    private final OtherPartItems otherParts = new OtherPartItems(Constants.MOD_ID);

    public LevelUpgradesRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Constants.MOD_ID);
    }

    @Override
    public void registerConditions() {
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        for (Map.Entry<StorageMaterial, IRegistryObject<LevelUpgradeItem>> levelUpgrade : LevelUpgradesItems.LEVEL_UPGRADES.entrySet()) {
            TagKey<Item> mat = levelUpgrade.getKey().getMaterial();
            ResourceLocation id = levelUpgrade.getValue().getId();
            ResourceLocation fromBlank = ResourceLocation.parse(id + "_from_blank_upgrade");

            this.addConditions(itemTagExists(mat), id);
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, levelUpgrade.getValue().get(), 1).define('M', mat).define('P', LevelUpgradesTags.Items.PAPER).pattern("MMM").pattern("MPM").pattern("MMM").unlockedBy("has_paper", has(LevelUpgradesTags.Items.PAPER)).unlockedBy("has_material", has(mat)).save(this.output, id);

            // The recipe from before the split, for anyone who has Assorted Crates' blank upgrades to spend.
            this.addConditions(and(modLoaded(CRATES), itemTagExists(mat)), fromBlank);
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, levelUpgrade.getValue().get(), 1).define('M', mat).define('B', this.otherParts.of(CRATES, "blank_upgrade")).pattern("MMM").pattern("MBM").pattern("MMM").unlockedBy("has_material", has(mat)).save(this.output, fromBlank);
        }
    }

    /** Writes the other parts' items {@link #otherParts} stood in for. */
    @Override
    public CompletableFuture<?> run(CachedOutput cache, HolderLookup.Provider registries) {
        return super.run(this.otherParts.wrap(cache), registries);
    }
}
