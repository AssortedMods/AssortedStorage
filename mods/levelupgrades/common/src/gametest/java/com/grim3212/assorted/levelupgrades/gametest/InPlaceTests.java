package com.grim3212.assorted.levelupgrades.gametest;

import com.grim3212.assorted.levelupgrades.common.item.LevelUpgradesItems;
import com.grim3212.assorted.lib.core.storage.BaseStorageBlockEntity;
import com.grim3212.assorted.lib.core.storage.LevelUpgrades;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.test.TestSupport;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A level upgrade raises a placed chest one material without unpacking it, as the site describes,
 * once Assorted Chests is there to turn it into. Without it nothing takes a vanilla chest, and the
 * upgrade must leave the chest, its items and itself alone.
 */
final class InPlaceTests {

    private static final BlockPos AT = new BlockPos(2, 1, 2);

    private InPlaceTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("level_upgrade_raises_a_chest_only_through_assorted_chests", InPlaceTests::levelUpgradeRaisesAChestOnlyThroughAssortedChests);
    }

    private static void levelUpgradeRaisesAChestOnlyThroughAssortedChests(GameTestHelper helper) {
        helper.setBlock(AT, Blocks.CHEST);
        helper.getBlockEntity(AT, ChestBlockEntity.class).setItem(0, new ItemStack(Items.DIAMOND, 4));

        ItemStack upgrade = new ItemStack(LevelUpgradesItems.LEVEL_UPGRADES.get(StorageMaterial.COPPER).get());
        ServerPlayer player = TestSupport.survivalPlayer(helper, upgrade);
        player.setShiftKeyDown(true);

        if (!Services.PLATFORM.isModLoaded("assortedchests")) {
            helper.assertTrue(LevelUpgrades.prepare(helper.getLevel(), helper.absolutePos(AT), player, StorageMaterial.COPPER) == null, "something other than Assorted Chests offered to upgrade a vanilla chest");
            InteractionResult result = upgrade.useOn(new UseOnContext(player, InteractionHand.MAIN_HAND, TestSupport.hitTop(helper.absolutePos(AT))));
            helper.assertValueEqual(result, InteractionResult.PASS, "what a level upgrade with nothing to turn a chest into answered");
            helper.assertTrue(helper.getBlockState(AT).is(Blocks.CHEST), "a level upgrade with nothing to turn it into replaced a chest with " + helper.getBlockState(AT));
            ItemStack left = helper.getBlockEntity(AT, ChestBlockEntity.class).getItem(0);
            helper.assertTrue(left.is(Items.DIAMOND) && left.getCount() == 4, "the chest holds " + left + " after a level upgrade did nothing, not the four diamonds");
            helper.assertValueEqual(upgrade.getCount(), 1, "level upgrades left after one did nothing");
            helper.succeed();
            return;
        }

        upgrade.useOn(new UseOnContext(player, InteractionHand.MAIN_HAND, TestSupport.hitTop(helper.absolutePos(AT))));

        String now = BuiltInRegistries.BLOCK.getKey(helper.getBlockState(AT).getBlock()).toString();
        helper.assertTrue(now.equals("assortedchests:chest_copper"), "a copper level upgrade turned a chest into " + now);
        ItemStack kept = helper.getBlockEntity(AT, BaseStorageBlockEntity.class).getStorageHandler().getItemStorageHandler(null).getStackInSlot(0);
        helper.assertTrue(kept.is(Items.DIAMOND) && kept.getCount() == 4, "the upgraded chest holds " + kept + ", not the four diamonds");
        helper.assertTrue(upgrade.isEmpty(), "the level upgrade was not used up");
        helper.succeed();
    }
}
