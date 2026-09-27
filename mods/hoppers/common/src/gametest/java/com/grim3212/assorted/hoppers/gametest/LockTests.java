package com.grim3212.assorted.hoppers.gametest;

import com.grim3212.assorted.hoppers.common.block.HoppersBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.hoppers.gametest.HoppersTestSupport.*;

/**
 * Locked hoppers.
 */
final class LockTests {

    private LockTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("locked_blocks_send_their_lock_to_clients", LockTests::lockedBlocksSendTheirLockToClients);
    }

    /** A locked hopper sends its lock to clients in the update tag, or other players keep seeing the unlocked face. */
    private static void lockedBlocksSendTheirLockToClients(GameTestHelper helper) {
        assertLockReachesClients(helper, new BlockPos(4, 1, 4), HoppersBlocks.LOCKED_HOPPER.get());
        helper.succeed();
    }
}
