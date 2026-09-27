package com.grim3212.assorted.chests.gametest;

import com.grim3212.assorted.lib.core.inventory.locking.StorageUtil;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.chests.common.block.ChestsBlocks;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

/**
 * What only a client can check: tooltips as Fabric builds them and menus arriving through Fabric's
 * packet path. Run with {@code ./gradlew :fabric:runClientGameTest}; it exits non-zero on a failure.
 */
public class ChestsClientGameTests implements FabricClientGameTest {

    @Override
    public void runTest(ClientGameTestContext context) {
        // Inside a world: an ItemStack cannot be made on the title screen, because an item's default
        // components are only bound once a world's registries have loaded.
        try (TestSingleplayerContext world = context.worldBuilder().create()) {
            tooltipsShowStorageInfo(context);
            menusOpenOnTheClient(context, world);
        }
    }

    private static void tooltipsShowStorageInfo(ClientGameTestContext context) {
        context.runOnClient(client -> {
            List<String> chest = tooltipKeys(client, StorageUtil.setCodeOnStack("1234", new ItemStack(ChestsBlocks.CHESTS.get(StorageMaterial.GOLD).get())));
            check(chest.contains("assortedlib.info.combo") && chest.contains("assortedlib.info.storage_level"), "a locked chest's tooltip is " + chest);
        });
    }

    /**
     * The server opens each menu for the client's own player, and the client builds the same menu
     * from what arrives, material and all.
     */
    private static void menusOpenOnTheClient(ClientGameTestContext context, TestSingleplayerContext world) {
        List<Block> blocks = List.of(ChestsBlocks.CHESTS.get(StorageMaterial.GOLD).get(), ChestsBlocks.CHESTS.get(StorageMaterial.STONE).get());

        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            int offset = i * 2;
            BlockPos pos = world.getServer().computeOnServer(server -> {
                ServerPlayer player = player(server);
                BlockPos at = player.blockPosition().offset(2, 0, offset - 4);
                player.level().setBlockAndUpdate(at, block.defaultBlockState());
                return at;
            });
            context.waitFor(client -> client.level != null && client.level.getBlockState(pos).is(block));

            Opened opened = world.getServer().computeOnServer(server -> {
                ServerPlayer player = player(server);
                ServerLevel level = player.level();
                MenuProvider provider = level.getBlockState(pos).getMenuProvider(level, pos);
                Services.PLATFORM.openMenu(player, provider);
                AbstractContainerMenu menu = player.containerMenu;
                return new Opened(menu.containerId, menu.getClass(), menu.slots.size());
            });
            context.waitFor(client -> client.player.containerMenu.containerId == opened.containerId());
            context.runOnClient(client -> {
                AbstractContainerMenu menu = client.player.containerMenu;
                String name = block.getName().getString();
                check(menu.getClass() == opened.type(), name + " opened a " + menu.getClass().getSimpleName() + " on the client, not a " + opened.type().getSimpleName());
                check(menu.slots.size() == opened.slots(), name + " has " + menu.slots.size() + " slots on the client and " + opened.slots() + " on the server");
            });

            world.getServer().runOnServer(server -> player(server).closeContainer());
            context.waitFor(client -> client.player.containerMenu == client.player.inventoryMenu);
        }
    }

    private record Opened(int containerId, Class<?> type, int slots) {
    }

    private static ServerPlayer player(MinecraftServer server) {
        return server.getPlayerList().getPlayers().getFirst();
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
