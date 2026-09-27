package com.grim3212.assorted.barrels.gametest;

import com.grim3212.assorted.lib.core.inventory.locking.StorageUtil;
import com.grim3212.assorted.barrels.common.block.BarrelsBlocks;
import com.grim3212.assorted.barrels.common.item.BarrelsDataComponents;
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
     * A barrel item's lock and level lines come from its {@code storage_info} component. NeoForge also
     * builds the full tooltip on the server, so there it is checked too.
     */
    private static void storageTooltipsComeFromComponents(GameTestHelper helper) {
        DataComponentType<StorageInfo> info = BarrelsDataComponents.STORAGE_INFO.get();
        String combo = "assortedlib.info.combo";
        String level = "assortedlib.info.storage_level";

        ItemStack lockedBarrel = StorageUtil.setCodeOnStack("1234", new ItemStack(BarrelsBlocks.BARRELS.values().iterator().next().get()));
        helper.assertValueEqual(tooltipKeys(helper, lockedBarrel, info), List.of(combo, level), "a locked barrel item's tooltip");

        if (onNeoForge()) {
            helper.assertTrue(fullTooltipKeys(helper, lockedBarrel).contains(combo), "the barrel's lock line is missing from its tooltip");
        }
        helper.succeed();
    }
}
