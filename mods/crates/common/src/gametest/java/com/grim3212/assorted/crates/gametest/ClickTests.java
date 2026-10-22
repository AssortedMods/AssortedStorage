package com.grim3212.assorted.crates.gametest;

import com.grim3212.assorted.crates.common.block.blockentity.CrateBlockEntity;
import com.grim3212.assorted.lib.test.TestSupport;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.crates.gametest.CratesTestSupport.BLOCK;
import static com.grim3212.assorted.crates.gametest.CratesTestSupport.oakCrate;

/** The clicks the site lists for a crate's face: right click puts in, left click takes one, and sneaking takes a stack. */
final class ClickTests {

    private ClickTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("crate_clicks_put_in_and_take_out", ClickTests::crateClicksPutInAndTakeOut);
    }

    private static void crateClicksPutInAndTakeOut(GameTestHelper helper) {
        helper.setBlock(BLOCK, oakCrate());
        CrateBlockEntity crate = helper.getBlockEntity(BLOCK, CrateBlockEntity.class);
        // A crate faces north until placed by a player.
        BlockHitResult front = TestSupport.hitSide(helper.absolutePos(BLOCK), Direction.NORTH);
        ServerPlayer player = TestSupport.survivalPlayer(helper, new ItemStack(Items.DIAMOND, 10));

        crate.use(player, InteractionHand.MAIN_HAND, front);
        helper.assertValueEqual(crate.getItemStackStorageHandler().getLargeItemStack(0).getAmount(), 10, "diamonds in the crate after a right click");

        crate.attack(player, front);
        helper.assertValueEqual(TestSupport.countInInventory(player, Items.DIAMOND), 1, "diamonds a left click took back");

        player.setShiftKeyDown(true);
        crate.attack(player, front);
        helper.assertValueEqual(TestSupport.countInInventory(player, Items.DIAMOND), 10, "diamonds held after a sneaking left click took the rest");
        helper.succeed();
    }
}
