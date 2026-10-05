package com.grim3212.assorted.levelupgrades.gametest;

import com.grim3212.assorted.levelupgrades.common.item.LevelUpgradesItems;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.lib.test.TestSupport.*;

/**
 * Crafting a level upgrade, which needs nothing from Assorted Crates.
 */
final class RecipeTests {

    private RecipeTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("level_upgrades_craft_around_paper", RecipeTests::levelUpgradesCraftAroundPaper);
    }

    /** Eight of a material around a sheet of paper make that material's upgrade. */
    private static void levelUpgradesCraftAroundPaper(GameTestHelper helper) {
        ItemStack iron = new ItemStack(Items.IRON_INGOT);
        ItemStack upgrade = craft(helper, CraftingInput.of(3, 3, List.of(iron, iron, iron, iron, new ItemStack(Items.PAPER), iron, iron, iron, iron)), "iron around paper");
        helper.assertTrue(upgrade.is(LevelUpgradesItems.LEVEL_UPGRADES.get(StorageMaterial.IRON).get()), "iron around paper crafted " + upgrade + " rather than an iron level upgrade");
        helper.succeed();
    }
}
