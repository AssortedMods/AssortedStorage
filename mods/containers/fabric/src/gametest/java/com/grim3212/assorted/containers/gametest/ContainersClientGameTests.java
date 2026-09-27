package com.grim3212.assorted.containers.gametest;

import com.grim3212.assorted.containers.common.block.ContainersBlocks;
import com.grim3212.assorted.lib.platform.Services;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.Block;

import java.util.List;

/**
 * What only a client can check: menus arriving through Fabric's packet path. Run with
 * {@code ./gradlew :fabric:runClientGameTest}; it exits non-zero on a failure.
 */
public class ContainersClientGameTests implements FabricClientGameTest {

    @Override
    public void runTest(ClientGameTestContext context) {
        try (TestSingleplayerContext world = context.worldBuilder().create()) {
            menusOpenOnTheClient(context, world);
        }
    }

    /**
     * The server opens each menu for the client's own player, and the client builds the same menu
     * from what arrives: one with data (the item tower's position) and a plain vanilla one.
     */
    private static void menusOpenOnTheClient(ClientGameTestContext context, TestSingleplayerContext world) {
        List<Block> blocks = List.of(ContainersBlocks.ITEM_TOWER.get(), ContainersBlocks.WOOD_CABINET.get());

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

    private static void check(boolean ok, String message) {
        if (!ok) {
            throw new AssertionError(message);
        }
    }
}
