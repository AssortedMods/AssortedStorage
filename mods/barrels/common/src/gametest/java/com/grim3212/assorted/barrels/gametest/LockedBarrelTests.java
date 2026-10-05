package com.grim3212.assorted.barrels.gametest;

import com.grim3212.assorted.barrels.common.block.BarrelsBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import com.grim3212.assorted.lib.core.inventory.locking.LockConversions;
import com.grim3212.assorted.lib.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import java.util.LinkedHashMap;
import java.util.Map;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;

import static com.grim3212.assorted.barrels.gametest.BarrelsTestSupport.*;

/**
 * A barrel's lock reaching clients, and padlocks on vanilla barrels.
 */
final class LockedBarrelTests {

    private LockedBarrelTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("locked_blocks_send_their_lock_to_clients", LockedBarrelTests::lockedBlocksSendTheirLockToClients);
        out.accept("vanilla_barrels_lock_only_through_assorted_locks", LockedBarrelTests::vanillaBarrelsLockOnlyThroughAssortedLocks);
    }

    /** The barrel sends its lock to clients in the update tag, or other players keep seeing the unlocked top. */
    private static void lockedBlocksSendTheirLockToClients(GameTestHelper helper) {
        assertLockReachesClients(helper, new BlockPos(2, 1, 4), BarrelsBlocks.BARRELS.get(StorageMaterial.STONE).get());
        assertLockReachesClients(helper, new BlockPos(6, 1, 4), BarrelsBlocks.BARRELS.values().iterator().next().get());
        helper.succeed();
    }

    /**
     * A padlock turns a vanilla barrel into Assorted Locks' locked one when that mod is installed, and does nothing
     * without it, as the locked vanilla containers are that mod's.
     */
    private static void vanillaBarrelsLockOnlyThroughAssortedLocks(GameTestHelper helper) {
        boolean locks = Services.PLATFORM.isModLoaded("assortedlocks");
        Map<Block, String> vanilla = new LinkedHashMap<>();
        vanilla.put(Blocks.BARREL, "locked_barrel");

        int i = 0;
        for (Map.Entry<Block, String> entry : vanilla.entrySet()) {
            BlockPos pos = new BlockPos(2 + i++ * 2, 1, 4);
            helper.setBlock(pos, entry.getKey());
            String name = BuiltInRegistries.BLOCK.getKey(entry.getKey()).toString();

            helper.assertValueEqual(LockConversions.tryLock(helper.getLevel(), helper.absolutePos(pos), CODE), locks, "whether a padlock locked a " + name);
            Identifier expected = locks ? Identifier.fromNamespaceAndPath("assortedlocks", entry.getValue()) : BuiltInRegistries.BLOCK.getKey(entry.getKey());
            helper.assertValueEqual(BuiltInRegistries.BLOCK.getKey(helper.getBlockState(pos).getBlock()), expected, "what a padlock left of a " + name);
        }
        helper.succeed();
    }
}
