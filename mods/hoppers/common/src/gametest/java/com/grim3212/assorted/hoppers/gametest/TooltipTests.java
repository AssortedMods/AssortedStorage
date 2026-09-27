package com.grim3212.assorted.hoppers.gametest;

import com.grim3212.assorted.hoppers.common.block.HoppersBlocks;
import com.grim3212.assorted.hoppers.common.item.HoppersDataComponents;
import com.grim3212.assorted.lib.core.inventory.locking.StorageUtil;
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
     * A hopper item's lock and level lines come from its {@code storage_info} component. NeoForge also
     * builds the full tooltip on the server, so there it is checked too.
     */
    private static void storageTooltipsComeFromComponents(GameTestHelper helper) {
        DataComponentType<StorageInfo> info = HoppersDataComponents.STORAGE_INFO.get();
        String combo = "assortedlib.info.combo";
        String level = "assortedlib.info.storage_level";

        ItemStack lockedHopper = StorageUtil.setCodeOnStack("1234", new ItemStack(HoppersBlocks.HOPPERS.values().iterator().next().get()));
        helper.assertValueEqual(tooltipKeys(helper, lockedHopper, info), List.of(combo, level), "a locked hopper item's tooltip");
        helper.assertValueEqual(tooltipKeys(helper, new ItemStack(HoppersBlocks.LOCKED_HOPPER.get()), info), List.of(level), "an unlocked vanilla stand-in's tooltip");

        if (onNeoForge()) {
            helper.assertTrue(fullTooltipKeys(helper, lockedHopper).contains(combo), "the hopper's lock line is missing from its tooltip");
        }
        helper.succeed();
    }
}
