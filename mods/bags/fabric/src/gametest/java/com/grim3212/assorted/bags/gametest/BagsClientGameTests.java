package com.grim3212.assorted.bags.gametest;

import com.grim3212.assorted.bags.common.item.BagsItems;
import com.grim3212.assorted.lib.core.inventory.locking.StorageUtil;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * What only a client can check: a bag's tooltip as Fabric builds it. Run with
 * {@code ./gradlew :fabric:runClientGameTest}; it exits non-zero on a failure.
 */
public class BagsClientGameTests implements FabricClientGameTest {

    @Override
    public void runTest(ClientGameTestContext context) {
        // Inside a world: an ItemStack cannot be made on the title screen, because an item's default
        // components are only bound once a world's registries have loaded.
        try (TestSingleplayerContext world = context.worldBuilder().create()) {
            tooltipsShowStorageInfo(context);
        }
    }

    private static void tooltipsShowStorageInfo(ClientGameTestContext context) {
        context.runOnClient(client -> {
            List<String> bag = tooltipKeys(client, StorageUtil.setCodeOnStack("1234", new ItemStack(BagsItems.BAG.get())));
            check(bag.contains("assortedlib.info.locked") && bag.contains("assortedlib.info.storage_level"), "a locked bag's tooltip is " + bag);
        });
    }

    private static List<String> tooltipKeys(Minecraft client, ItemStack stack) {
        return stack.getTooltipLines(Item.TooltipContext.of(client.level), client.player, TooltipFlag.NORMAL).stream()
                .map(line -> line.getContents() instanceof TranslatableContents translatable ? translatable.getKey() : line.getString())
                .toList();
    }

    private static void check(boolean ok, String message) {
        if (!ok) {
            throw new AssertionError(message);
        }
    }
}
