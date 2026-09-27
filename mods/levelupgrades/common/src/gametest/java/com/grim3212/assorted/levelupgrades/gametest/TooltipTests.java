package com.grim3212.assorted.levelupgrades.gametest;

import com.grim3212.assorted.levelupgrades.common.item.LevelUpgradesDataComponents;
import com.grim3212.assorted.levelupgrades.common.item.LevelUpgradesItems;
import com.grim3212.assorted.lib.core.storage.StorageInfo;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.lib.test.TestSupport.*;

/**
 * Item tooltips, which come from data components.
 */
final class TooltipTests {

    private TooltipTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("storage_tooltips_come_from_components", TooltipTests::storageTooltipsComeFromComponents);
    }

    /**
     * A level upgrade's level line comes from the shared {@code storage_info} component. NeoForge also builds the full
     * tooltip on the server, so there it is checked too.
     */
    private static void storageTooltipsComeFromComponents(GameTestHelper helper) {
        DataComponentType<StorageInfo> info = LevelUpgradesDataComponents.STORAGE_INFO.get();
        String level = "assortedlib.info.storage_level";

        ItemStack levelUpgrade = new ItemStack(LevelUpgradesItems.LEVEL_UPGRADES.values().iterator().next().get());
        helper.assertValueEqual(tooltipKeys(helper, levelUpgrade, info), List.of(level), "a level upgrade's tooltip");

        if (onNeoForge()) {
            helper.assertTrue(fullTooltipKeys(helper, levelUpgrade).contains(level), "the upgrade's level line is missing from its tooltip");
        }
        helper.succeed();
    }
}
