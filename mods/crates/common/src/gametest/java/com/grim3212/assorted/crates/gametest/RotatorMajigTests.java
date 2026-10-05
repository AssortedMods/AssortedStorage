package com.grim3212.assorted.crates.gametest;

import com.grim3212.assorted.crates.common.item.CratesItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.lib.test.TestSupport.*;
import static com.grim3212.assorted.crates.gametest.CratesTestSupport.*;

/**
 * The rotator majig.
 */
final class RotatorMajigTests {

    private RotatorMajigTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("rotator_majig_rotates_a_block", RotatorMajigTests::rotatorMajigRotatesABlock);
    }

    /** The rotator majig turns whatever it is used on a quarter turn clockwise. */
    private static void rotatorMajigRotatesABlock(GameTestHelper helper) {
        helper.setBlock(BLOCK, Blocks.FURNACE.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH));

        ServerPlayer player = survivalPlayer(helper, new ItemStack(CratesItems.ROTATOR_MAJIG.get()));
        BlockPos pos = helper.absolutePos(BLOCK);
        BlockHitResult hit = new BlockHitResult(Vec3.atCenterOf(pos).add(0.0D, 0.5D, 0.0D), Direction.UP, pos, false);
        InteractionResult result = player.getItemInHand(InteractionHand.MAIN_HAND).useOn(new UseOnContext(player, InteractionHand.MAIN_HAND, hit));

        helper.assertTrue(result.consumesAction(), "the rotator majig did not act on the block");
        helper.assertBlockProperty(BLOCK, HorizontalDirectionalBlock.FACING, Direction.EAST);
        helper.succeed();
    }
}
