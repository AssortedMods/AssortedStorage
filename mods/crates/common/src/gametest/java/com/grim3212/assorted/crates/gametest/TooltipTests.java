package com.grim3212.assorted.crates.gametest;

import com.grim3212.assorted.lib.core.inventory.locking.StorageUtil;
import com.grim3212.assorted.crates.Constants;
import com.grim3212.assorted.crates.common.item.CratesDataComponents;
import com.grim3212.assorted.lib.core.storage.StorageInfo;
import com.grim3212.assorted.crates.common.item.CratesItems;
import com.grim3212.assorted.crates.common.item.UpgradeModeInfo;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.lib.test.TestSupport.*;
import static com.grim3212.assorted.crates.gametest.CratesTestSupport.*;

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
     * A locked crate item shows its code from its {@code storage_info} component, and an upgrade its mode from
     * {@code upgrade_mode_info}. NeoForge also builds the full tooltip on the server; {@code CratesClientGameTests} covers Fabric.
     */
    private static void storageTooltipsComeFromComponents(GameTestHelper helper) {
        DataComponentType<StorageInfo> info = CratesDataComponents.STORAGE_INFO.get();
        DataComponentType<UpgradeModeInfo> modeInfo = CratesDataComponents.UPGRADE_MODE_INFO.get();
        String combo = "assortedlib.info.combo";
        String mode = Constants.MOD_ID + ".info.upgrade.mode";

        ItemStack lockedCrate = StorageUtil.setCodeOnStack("1234", new ItemStack(oakCrate()));
        helper.assertValueEqual(tooltipKeys(helper, lockedCrate, info), List.of(combo), "a locked crate item's tooltip");

        helper.assertValueEqual(tooltipKeys(helper, new ItemStack(CratesItems.AMOUNT_UPGRADE.get()), modeInfo), List.of(mode), "an amount upgrade's tooltip");
        helper.assertValueEqual(tooltipKeys(helper, new ItemStack(CratesItems.REDSTONE_UPGRADE.get()), modeInfo), List.of(mode), "a redstone upgrade's tooltip");

        if (onNeoForge()) {
            helper.assertTrue(fullTooltipKeys(helper, new ItemStack(CratesItems.AMOUNT_UPGRADE.get())).contains(mode), "the upgrade's mode line is missing from its tooltip");
        }
        helper.succeed();
    }
}
