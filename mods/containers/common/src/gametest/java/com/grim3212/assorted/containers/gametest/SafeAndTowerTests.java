package com.grim3212.assorted.containers.gametest;

import com.grim3212.assorted.containers.common.block.ContainersBlocks;
import com.grim3212.assorted.lib.core.inventory.IItemStorageHandler;
import com.grim3212.assorted.lib.core.storage.BaseStorageBlockEntity;
import com.grim3212.assorted.lib.test.TestSupport;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** What sets the safes and the item tower apart, as the site describes them. */
final class SafeAndTowerTests {

    private static final BlockPos AT = new BlockPos(2, 1, 2);

    private SafeAndTowerTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("gold_safe_keeps_its_items_when_broken", SafeAndTowerTests::goldSafeKeepsItsItemsWhenBroken);
        out.accept("obsidian_safe_shrugs_off_an_explosion", SafeAndTowerTests::obsidianSafeShrugsOffAnExplosion);
        out.accept("stacked_item_towers_share_their_slots", SafeAndTowerTests::stackedItemTowersShareTheirSlots);
    }

    /** Broken, it drops itself still holding the diamond, which is there again once it is placed. */
    private static void goldSafeKeepsItsItemsWhenBroken(GameTestHelper helper) {
        helper.setBlock(AT, ContainersBlocks.GOLD_SAFE.get());
        handler(helper, AT).insertItem(0, new ItemStack(Items.DIAMOND, 3), false);
        helper.getLevel().destroyBlock(helper.absolutePos(AT), true);

        List<ItemEntity> dropped = helper.getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(helper.absolutePos(AT)).inflate(2.0D),
                entity -> entity.getItem().is(ContainersBlocks.GOLD_SAFE.get().asItem()));
        helper.assertFalse(dropped.isEmpty(), "a broken gold safe did not drop itself");
        helper.assertTrue(helper.getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(helper.absolutePos(AT)).inflate(2.0D),
                entity -> entity.getItem().is(Items.DIAMOND)).isEmpty(), "a broken gold safe spilled its diamonds");

        ItemStack safe = dropped.getFirst().getItem().copy();
        BlockPos placeOn = AT.east(2);
        helper.setBlock(placeOn, Blocks.STONE);
        ServerPlayer player = TestSupport.survivalPlayer(helper, safe);
        safe.useOn(new UseOnContext(player, InteractionHand.MAIN_HAND, TestSupport.hitTop(helper.absolutePos(placeOn))));
        ItemStack held = handler(helper, placeOn.above()).getStackInSlot(0);
        helper.assertTrue(held.is(Items.DIAMOND) && held.getCount() == 3, "the gold safe placed again holds " + held);
        helper.succeed();
    }

    private static void obsidianSafeShrugsOffAnExplosion(GameTestHelper helper) {
        helper.setBlock(AT, ContainersBlocks.OBSIDIAN_SAFE.get());
        Vec3 beside = Vec3.atCenterOf(helper.absolutePos(AT.east()));
        helper.getLevel().explode(null, beside.x, beside.y, beside.z, 4.0F, Level.ExplosionInteraction.TNT);
        helper.assertBlockPresent(ContainersBlocks.OBSIDIAN_SAFE.get(), AT);
        helper.succeed();
    }

    private static void stackedItemTowersShareTheirSlots(GameTestHelper helper) {
        helper.setBlock(AT, ContainersBlocks.ITEM_TOWER.get());
        int one = handler(helper, AT).getSlots();
        helper.setBlock(AT.above(), ContainersBlocks.ITEM_TOWER.get());
        int stacked = handler(helper, AT).getSlots();
        helper.assertValueEqual(stacked, one * 2, "slots the bottom of two stacked item towers reaches");
        helper.succeed();
    }

    private static IItemStorageHandler handler(GameTestHelper helper, BlockPos rel) {
        return helper.getBlockEntity(rel, BaseStorageBlockEntity.class).getStorageHandler().getItemStorageHandler(null);
    }
}
