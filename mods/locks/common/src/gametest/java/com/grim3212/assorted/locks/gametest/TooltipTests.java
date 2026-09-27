package com.grim3212.assorted.locks.gametest;

import com.grim3212.assorted.lib.core.inventory.locking.StorageUtil;
import com.grim3212.assorted.lib.core.storage.StorageInfo;
import com.grim3212.assorted.locks.common.item.LocksDataComponents;
import com.grim3212.assorted.locks.common.item.LocksItems;
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
     * A cut key or padlock shows its code through the shared {@code storage_info} component, and a blank one shows
     * nothing. NeoForge also builds the full tooltip on the server; {@code LocksClientGameTests} covers Fabric.
     */
    private static void storageTooltipsComeFromComponents(GameTestHelper helper) {
        DataComponentType<StorageInfo> info = LocksDataComponents.STORAGE_INFO.get();
        String combo = "assortedlib.info.combo";

        ItemStack cutKey = StorageUtil.setCodeOnStack("1234", new ItemStack(LocksItems.LOCKSMITH_KEY.get()));
        helper.assertValueEqual(tooltipKeys(helper, cutKey, info), List.of(combo), "a cut key's tooltip");
        helper.assertValueEqual(tooltipKeys(helper, StorageUtil.setCodeOnStack("1234", new ItemStack(LocksItems.LOCKSMITH_LOCK.get())), info), List.of(combo), "a coded padlock's tooltip");
        helper.assertValueEqual(tooltipKeys(helper, new ItemStack(LocksItems.LOCKSMITH_KEY.get()), info), List.of(), "a blank key's tooltip");

        if (onNeoForge()) {
            helper.assertTrue(fullTooltipKeys(helper, cutKey).contains(combo), "the key's code line is missing from its tooltip");
        }
        helper.succeed();
    }
}
