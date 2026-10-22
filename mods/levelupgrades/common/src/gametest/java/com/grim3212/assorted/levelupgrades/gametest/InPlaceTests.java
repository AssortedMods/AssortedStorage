package com.grim3212.assorted.levelupgrades.gametest;

import com.grim3212.assorted.levelupgrades.common.item.LevelUpgradesItems;
import com.grim3212.assorted.lib.core.storage.BaseStorageBlockEntity;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.test.TestSupport;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** A level upgrade raises a placed chest one material without unpacking it, as the site describes, once Assorted Chests is there to turn it into. */
final class InPlaceTests {

    private static final BlockPos AT = new BlockPos(2, 1, 2);

    private InPlaceTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("level_upgrade_raises_a_chest_keeping_its_items", InPlaceTests::levelUpgradeRaisesAChestKeepingItsItems);
    }

    private static void levelUpgradeRaisesAChestKeepingItsItems(GameTestHelper helper) {
        if (!Services.PLATFORM.isModLoaded("assortedchests")) {
            helper.succeed();
            return;
        }
        helper.setBlock(AT, Blocks.CHEST);
        helper.getBlockEntity(AT, ChestBlockEntity.class).setItem(0, new ItemStack(Items.DIAMOND, 4));

        ItemStack upgrade = new ItemStack(LevelUpgradesItems.LEVEL_UPGRADES.get(StorageMaterial.COPPER).get());
        ServerPlayer player = TestSupport.survivalPlayer(helper, upgrade);
        player.setShiftKeyDown(true);
        upgrade.useOn(new UseOnContext(player, InteractionHand.MAIN_HAND, TestSupport.hitTop(helper.absolutePos(AT))));

        String now = BuiltInRegistries.BLOCK.getKey(helper.getBlockState(AT).getBlock()).toString();
        helper.assertTrue(now.equals("assortedchests:chest_copper"), "a copper level upgrade turned a chest into " + now);
        ItemStack kept = helper.getBlockEntity(AT, BaseStorageBlockEntity.class).getStorageHandler().getItemStorageHandler(null).getStackInSlot(0);
        helper.assertTrue(kept.is(Items.DIAMOND) && kept.getCount() == 4, "the upgraded chest holds " + kept + ", not the four diamonds");
        helper.assertTrue(upgrade.isEmpty(), "the level upgrade was not used up");
        helper.succeed();
    }
}
