package com.grim3212.assorted.locks.gametest;

import com.grim3212.assorted.lib.core.inventory.locking.StorageUtil;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.locks.common.block.LocksBlocks;
import com.grim3212.assorted.locks.common.item.LocksItems;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * What only a client can check: tooltips as Fabric builds them, the workbench menu arriving through
 * Fabric's packet path, and locked doors reaching other clients whole. Run with
 * {@code ./gradlew :locks:fabric:runClientGameTest}; it exits non-zero on a failure.
 */
public class LocksClientGameTests implements FabricClientGameTest {

    @Override
    public void runTest(ClientGameTestContext context) {
        // Inside a world: an ItemStack cannot be made on the title screen, because an item's default
        // components are only bound once a world's registries have loaded.
        try (TestSingleplayerContext world = context.worldBuilder().create()) {
            tooltipsShowTheCode(context);
            workbenchOpensOnTheClient(context, world);
            lockedDoorsReachOtherClientsWhole(context, world);
        }
    }

    private static void tooltipsShowTheCode(ClientGameTestContext context) {
        context.runOnClient(client -> {
            List<String> key = tooltipKeys(client, StorageUtil.setCodeOnStack("1234", new ItemStack(LocksItems.LOCKSMITH_KEY.get())));
            check(key.contains("assortedlib.info.combo"), "a cut key's tooltip is " + key);
        });
    }

    /** The server opens the workbench for the client's own player, and the client builds the same menu. */
    private static void workbenchOpensOnTheClient(ClientGameTestContext context, TestSingleplayerContext world) {
        Block block = LocksBlocks.LOCKSMITH_WORKBENCH.get();
        BlockPos pos = world.getServer().computeOnServer(server -> {
            ServerPlayer player = player(server);
            BlockPos at = player.blockPosition().offset(2, 0, -4);
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

    /**
     * Both halves of a door, not only the one the padlock was clicked on. That half is swapped with
     * UPDATE_KNOWN_SHAPE, so it does not ask its still-vanilla partner whether it should stay - and
     * that flag carries no UPDATE_CLIENTS, so it was never sent. A client that did not run the
     * interaction saw half a locked door and half a plain one.
     */
    private static void lockedDoorsReachOtherClientsWhole(ClientGameTestContext context, TestSingleplayerContext world) {
        Block locked = LocksBlocks.VANILLA_DOORS.get(Blocks.OAK_DOOR).get();

        BlockPos lower = world.getServer().computeOnServer(server -> {
            ServerPlayer player = player(server);
            ServerLevel level = player.level();
            BlockPos at = player.blockPosition().offset(6, 0, 2);
            // A door needs something to stand on, and the world this runs in is ordinary terrain.
            level.setBlockAndUpdate(at.below(), Blocks.STONE.defaultBlockState());
            level.setBlockAndUpdate(at, Blocks.OAK_DOOR.defaultBlockState().setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER));
            level.setBlockAndUpdate(at.above(), Blocks.OAK_DOOR.defaultBlockState().setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER));
            return at;
        });
        context.waitFor(client -> client.level != null && client.level.getBlockState(lower).is(Blocks.OAK_DOOR)
                && client.level.getBlockState(lower.above()).is(Blocks.OAK_DOOR), 100);

        world.getServer().runOnServer(server -> {
            ServerPlayer player = player(server);
            // A door answers a plain right click by opening, so the padlock only reaches its own
            // useOn when the block's use is suppressed - which is to say while sneaking, the way one
            // is fitted in game.
            player.setShiftKeyDown(true);
            rightClickOnServer(player, lower, StorageUtil.setCodeOnStack("1234", new ItemStack(LocksItems.LOCKSMITH_LOCK.get())));
            player.setShiftKeyDown(false);
        });
        world.getServer().runOnServer(server -> {
            ServerLevel level = player(server).level();
            check(level.getBlockState(lower).is(locked) && level.getBlockState(lower.above()).is(locked),
                    "the server did not lock both halves: lower " + level.getBlockState(lower).getBlock() + ", upper " + level.getBlockState(lower.above()).getBlock());
        });

        try {
            context.waitFor(client -> client.level.getBlockState(lower).is(locked)
                    && client.level.getBlockState(lower.above()).is(locked), 100);
        } catch (AssertionError timedOut) {
            String seen = context.computeOnClient(client -> "lower " + client.level.getBlockState(lower).getBlock()
                    + ", upper " + client.level.getBlockState(lower.above()).getBlock());
            throw new AssertionError("a door the server locked reached this client as " + seen, timedOut);
        }
    }

    /** A right click on the top of {@code pos}, through the game mode, so the loader's use-block event runs. */
    private static void rightClickOnServer(ServerPlayer player, BlockPos pos, ItemStack held) {
        player.setItemInHand(InteractionHand.MAIN_HAND, held);
        BlockHitResult hit = new BlockHitResult(Vec3.atCenterOf(pos).relative(Direction.UP, 0.5D), Direction.UP, pos, false);
        player.gameMode.useItemOn(player, player.level(), held, InteractionHand.MAIN_HAND, hit);
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
