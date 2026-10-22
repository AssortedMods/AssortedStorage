package com.grim3212.assorted.locks.gametest;

import com.grim3212.assorted.lib.core.inventory.locking.BaseLockedBlockEntity;
import com.grim3212.assorted.locks.common.block.LocksBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.locks.gametest.LocksTestSupport.CODE;

/** The site's promise that a locked door no longer answers redstone, so a button outside cannot open it. */
final class RedstoneTests {

    private static final BlockPos LOWER = new BlockPos(4, 1, 4);

    private RedstoneTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("locked_door_ignores_redstone", RedstoneTests::lockedDoorIgnoresRedstone);
    }

    private static void lockedDoorIgnoresRedstone(GameTestHelper helper) {
        Block door = LocksBlocks.VANILLA_DOORS.get(Blocks.IRON_DOOR).get();
        helper.setBlock(LOWER, door.defaultBlockState().setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER));
        helper.setBlock(LOWER.above(), door.defaultBlockState().setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER));
        helper.getBlockEntity(LOWER, BaseLockedBlockEntity.class).setLockCode(CODE);
        helper.getBlockEntity(LOWER.above(), BaseLockedBlockEntity.class).setLockCode(CODE);

        helper.setBlock(LOWER.east(), Blocks.REDSTONE_BLOCK);
        helper.runAfterDelay(10, () -> {
            helper.assertFalse(helper.getBlockState(LOWER).getValue(DoorBlock.OPEN), "a locked iron door opened for a redstone block beside it");
            helper.succeed();
        });
    }
}
