package com.grim3212.assorted.barrels.gametest;

import com.grim3212.assorted.barrels.common.block.BarrelsBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.barrels.gametest.BarrelsTestSupport.*;

/**
 * The locked barrel's lock reaching clients.
 */
final class LockedBarrelTests {

    private LockedBarrelTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("locked_blocks_send_their_lock_to_clients", LockedBarrelTests::lockedBlocksSendTheirLockToClients);
    }

    /** The barrel sends its lock to clients in the update tag, or other players keep seeing the unlocked top. */
    private static void lockedBlocksSendTheirLockToClients(GameTestHelper helper) {
        assertLockReachesClients(helper, new BlockPos(2, 1, 4), BarrelsBlocks.LOCKED_BARREL.get());
        assertLockReachesClients(helper, new BlockPos(6, 1, 4), BarrelsBlocks.BARRELS.values().iterator().next().get());
        helper.succeed();
    }
}
