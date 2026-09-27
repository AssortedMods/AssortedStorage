package com.grim3212.assorted.chests.gametest;

import com.grim3212.assorted.lib.core.inventory.locking.LockConversions;
import com.grim3212.assorted.lib.platform.Services;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import static com.grim3212.assorted.chests.gametest.ChestsTestSupport.*;

/**
 * Padlocks on vanilla chests, which are Assorted Locks' to turn into locked ones.
 */
final class LockTests {

    private LockTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("vanilla_chests_lock_only_through_assorted_locks", LockTests::vanillaChestsLockOnlyThroughAssortedLocks);
    }

    /**
     * A padlock turns a vanilla chest or ender chest into Assorted Locks' locked one when that mod is installed, and does nothing
     * without it, as the locked vanilla containers are that mod's.
     */
    private static void vanillaChestsLockOnlyThroughAssortedLocks(GameTestHelper helper) {
        boolean locks = Services.PLATFORM.isModLoaded("assortedlocks");
        Map<Block, String> vanilla = new LinkedHashMap<>();
        vanilla.put(Blocks.CHEST, "locked_chest");
        vanilla.put(Blocks.ENDER_CHEST, "locked_ender_chest");

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
