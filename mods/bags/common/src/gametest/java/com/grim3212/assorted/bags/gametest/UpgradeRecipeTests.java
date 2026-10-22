package com.grim3212.assorted.bags.gametest;

import com.grim3212.assorted.bags.Constants;
import com.grim3212.assorted.lib.test.CommonTests;
import net.minecraft.gametest.framework.GameTestHelper;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** Upgrading to the next material in a crafting grid keeps what the item carried, as the site promises: its contents, lock and name. */
final class UpgradeRecipeTests {

    private UpgradeRecipeTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("upgrading_keeps_what_the_item_carried", UpgradeRecipeTests::upgradingKeepsWhatTheItemCarried);
    }

    private static void upgradingKeepsWhatTheItemCarried(GameTestHelper helper) {
        List<String> problems = CommonTests.upgradesThatLoseTheirData(helper, Constants.MOD_ID);
        helper.assertTrue(problems.isEmpty(), String.join("\n", problems));
        helper.succeed();
    }
}
