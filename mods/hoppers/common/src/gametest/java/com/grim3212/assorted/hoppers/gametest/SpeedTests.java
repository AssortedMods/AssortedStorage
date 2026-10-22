package com.grim3212.assorted.hoppers.gametest;

import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.entity.HopperBlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** A higher storage level never moves items slower, and every material above level 0 beats a vanilla hopper. */
final class SpeedTests {

    private SpeedTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("higher_level_hoppers_move_items_faster", SpeedTests::higherLevelHoppersMoveItemsFaster);
    }

    private static void higherLevelHoppersMoveItemsFaster(GameTestHelper helper) {
        List<String> problems = new ArrayList<>();
        for (StorageMaterial a : StorageMaterial.values()) {
            if (a.getStorageLevel() > 0 && a.hopperCooldown() >= HopperBlockEntity.MOVE_ITEM_SPEED) {
                problems.add(a + " waits " + a.hopperCooldown() + " ticks, no faster than a vanilla hopper");
            }
            for (StorageMaterial b : StorageMaterial.values()) {
                if (a.getStorageLevel() > b.getStorageLevel() && a.hopperCooldown() > b.hopperCooldown()) {
                    problems.add(a + " at level " + a.getStorageLevel() + " is slower than " + b + " at level " + b.getStorageLevel());
                }
            }
        }
        helper.assertTrue(problems.isEmpty(), String.join("\n", problems));
        helper.succeed();
    }
}
