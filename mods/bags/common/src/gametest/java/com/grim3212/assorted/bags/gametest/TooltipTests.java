package com.grim3212.assorted.bags.gametest;

import com.grim3212.assorted.bags.common.item.BagsDataComponents;
import com.grim3212.assorted.bags.common.item.BagsItems;
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
     * Each bag's lock and level lines come from its {@code storage_info} component: bags only say they are locked.
     * NeoForge also builds the full tooltip on the server, so there it is checked too; {@code BagsClientGameTests} covers Fabric.
     */
    private static void storageTooltipsComeFromComponents(GameTestHelper helper) {
        DataComponentType<StorageInfo> info = BagsDataComponents.STORAGE_INFO.get();
        String locked = "assortedlib.info.locked";
        String level = "assortedlib.info.storage_level";

        ItemStack lockedBag = StorageUtil.setCodeOnStack("1234", new ItemStack(BagsItems.BAG.get()));
        helper.assertValueEqual(tooltipKeys(helper, lockedBag, info), List.of(locked, level), "a locked bag's tooltip");
        helper.assertValueEqual(tooltipKeys(helper, new ItemStack(BagsItems.BAG.get()), info), List.of(level), "an unlocked bag's tooltip");
        helper.assertValueEqual(tooltipKeys(helper, StorageUtil.setCodeOnStack("1234", new ItemStack(BagsItems.ENDER_BAG.get())), info), List.of(locked), "a locked ender bag's tooltip");

        if (onNeoForge()) {
            helper.assertTrue(fullTooltipKeys(helper, lockedBag).contains(locked), "the bag's lock line is missing from its tooltip");
        }
        helper.succeed();
    }
}
