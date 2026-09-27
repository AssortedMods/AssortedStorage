package com.grim3212.assorted.crates.gametest;

import com.grim3212.assorted.lib.core.inventory.IMenuDataProvider;
import com.grim3212.assorted.lib.core.inventory.MenuData;
import com.grim3212.assorted.crates.common.block.CratesBlocks;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.lib.test.TestSupport.*;
import static com.grim3212.assorted.crates.gametest.CratesTestSupport.*;

/**
 * Crate menus built on the client from what the server sends. Each loader's own packet path is
 * out of reach of a test player; {@code CratesClientGameTests} opens them for real on Fabric.
 */
final class MenuTests {

    private MenuTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("data_menus_rebuild_on_the_client", MenuTests::dataMenusRebuildOnTheClient);
    }

    /**
     * Every crate whose client menu needs server data: the data it sends, read back through its
     * menu type, builds the menu the server opened.
     */
    // NeoForge deprecates RegistryFriendlyByteBuf.decorator for an overload only its patched jar has.
    @SuppressWarnings("deprecation")
    private static void dataMenusRebuildOnTheClient(GameTestHelper helper) {
        ServerPlayer player = survivalPlayer(helper, ItemStack.EMPTY);
        List<Block> blocks = List.of(oakCrate(), oakCrateQuadruple(), CratesBlocks.CRATE_COMPACTING.get());

        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            String name = BuiltInRegistries.BLOCK.getKey(block).toString();
            BlockPos rel = new BlockPos(1 + i * 2, 1, 2);
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
