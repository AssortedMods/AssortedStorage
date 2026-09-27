package com.grim3212.assorted.locks.gametest;

import com.grim3212.assorted.lib.core.inventory.IMenuDataProvider;
import com.grim3212.assorted.lib.core.inventory.MenuData;
import com.grim3212.assorted.locks.common.block.LocksBlocks;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.lib.test.TestSupport.*;
import static com.grim3212.assorted.locks.gametest.LocksTestSupport.*;

/**
 * The workbench and locked container menus built on the client. Each loader's own packet path is out of reach of a test
 * player; {@code LocksClientGameTests} opens it for real on Fabric.
 */
final class MenuTests {

    private MenuTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("plain_menus_rebuild_on_the_client", MenuTests::plainMenusRebuildOnTheClient);
        out.accept("data_menus_rebuild_on_the_client", MenuTests::dataMenusRebuildOnTheClient);
    }

    /**
     * Blocks whose client menu needs nothing from the server open as vanilla menus: the client
     * builds them from the menu type alone, and they match what the server opened.
     */
    private static void plainMenusRebuildOnTheClient(GameTestHelper helper) {
        ServerPlayer player = survivalPlayer(helper, ItemStack.EMPTY);
        List<Block> blocks = List.of(LocksBlocks.LOCKSMITH_WORKBENCH.get());

        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            String name = BuiltInRegistries.BLOCK.getKey(block).toString();
            BlockPos rel = new BlockPos(1 + (i % 4) * 2, 1, 2 + (i / 4) * 3);
            helper.setBlock(rel, block);
            MenuProvider provider = helper.getBlockState(rel).getMenuProvider(helper.getLevel(), helper.absolutePos(rel));
            if (provider == null) {
                helper.fail(name + " has no menu provider");
                return;
            }
            helper.assertFalse(provider instanceof IMenuDataProvider<?>, name + " provides menu data it does not need");

            AbstractContainerMenu server = provider.createMenu(1, player.getInventory(), player);
            helper.assertFalse(MenuData.hasData(server.getType()), name + " opens a menu type that expects data");

            AbstractContainerMenu client = server.getType().create(1, player.getInventory());
            helper.assertTrue(client.getClass() == server.getClass(), name + " built a " + client.getClass().getSimpleName() + " on the client");
            helper.assertValueEqual(client.slots.size(), server.slots.size(), name + " client menu slots");
        }

        helper.succeed();
    }

    /** A locked container's client menu is built from the data the server sends, so a wrong material shows up as a different slot count. */
    // NeoForge deprecates RegistryFriendlyByteBuf.decorator for an overload only its patched jar has.
    @SuppressWarnings("deprecation")
    private static void dataMenusRebuildOnTheClient(GameTestHelper helper) {
        ServerPlayer player = survivalPlayer(helper, ItemStack.EMPTY);
        List<Block> blocks = List.of(LocksBlocks.LOCKED_CHEST.get(), LocksBlocks.LOCKED_BARREL.get(), LocksBlocks.LOCKED_HOPPER.get(), LocksBlocks.LOCKED_SHULKER_BOX.get());

        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            String name = BuiltInRegistries.BLOCK.getKey(block).toString();
            BlockPos rel = new BlockPos(1 + (i % 4) * 2, 1, 2 + (i / 4) * 3);
            helper.setBlock(rel, block);
            if (!(helper.getBlockEntity(rel, BlockEntity.class) instanceof IMenuDataProvider<?> provider)) {
                helper.fail(name + " does not provide menu data");
                return;
            }

            AbstractContainerMenu server = provider.createMenu(1, player.getInventory(), player);
            helper.assertTrue(MenuData.hasData(server.getType()), name + " opens a menu type that carries no data");

            RegistryFriendlyByteBuf buf = RegistryFriendlyByteBuf.decorator(helper.getLevel().registryAccess()).apply(Unpooled.buffer());
            MenuData.write(server.getType(), provider.getMenuData(player), buf);
            AbstractContainerMenu client = MenuData.read(server.getType(), 1, player.getInventory(), buf);

            helper.assertTrue(client.getClass() == server.getClass(), name + " built a " + client.getClass().getSimpleName() + " on the client");
            helper.assertValueEqual(client.slots.size(), server.slots.size(), name + " client menu slots");
            helper.assertValueEqual(buf.readableBytes(), 0, name + " menu data bytes left unread");
        }

        helper.succeed();
    }
}
