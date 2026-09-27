package com.grim3212.assorted.chests.gametest;

import com.grim3212.assorted.lib.core.inventory.IItemStorageHandler;
import com.grim3212.assorted.lib.core.storage.BaseStorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.chests.gametest.ChestsTestSupport.*;

/**
 * Chests: holding, dropping and saving their contents.
 */
final class ChestsBlockTests {

    private ChestsBlockTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("every_storage_block_holds_items", ChestsBlockTests::everyStorageBlockHoldsItems);
        out.accept("every_storage_block_drops_its_contents", ChestsBlockTests::everyStorageBlockDropsItsContents);
        out.accept("every_storage_block_survives_save_load", ChestsBlockTests::everyStorageBlockSurvivesSaveLoad);
    }

    /**
     * Every chest takes items in and hands them back, through the platform handler the loaders
     * bridge onto - the same object a hopper or a pipe sees, not the raw list behind it. Driven off
     * the block list so a new material is covered the day it is registered.
     */
    private static void everyStorageBlockHoldsItems(GameTestHelper helper) {
        List<Block> blocks = storageBlocks();

        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            BlockPos pos = spread(i);
            helper.setBlock(pos, block);

            BaseStorageBlockEntity storage = helper.getBlockEntity(pos, BaseStorageBlockEntity.class);
            IItemStorageHandler handler = storage.getStorageHandler().getItemStorageHandler(null);
            int last = handler.getSlots() - 1;

            helper.assertTrue(last > 0, name(block) + " has no slots to hold anything");
            helper.assertTrue(handler.insertItem(0, new ItemStack(Items.DIAMOND, 5), false).isEmpty(), name(block) + " refused an item in its first slot");
            helper.assertTrue(handler.insertItem(last, new ItemStack(Items.EMERALD, 2), false).isEmpty(), name(block) + " refused an item in its last slot");

            helper.assertTrue(handler.getStackInSlot(0).is(Items.DIAMOND), name(block) + " did not hold what went into its first slot");
            helper.assertValueEqual(handler.getStackInSlot(0).getCount(), 5, name(block) + " first slot stack size");
            helper.assertTrue(handler.getStackInSlot(last).is(Items.EMERALD), name(block) + " did not hold what went into its last slot");

            // The platform handler has to be a view of the block entity's own inventory, not a copy.
            helper.assertTrue(storage.getItemStackStorageHandler().getStackInSlot(0).is(Items.DIAMOND), name(block) + " kept the item somewhere other than its own inventory");
        }

        helper.succeed();
    }

    /** Breaking a chest spills its contents from {@code preRemoveSideEffects}. Each chest gets its own marker item. */
    private static void everyStorageBlockDropsItsContents(GameTestHelper helper) {
        List<Block> blocks = storageBlocks();

        for (int i = 0; i < blocks.size(); i++) {
            BlockPos pos = spread(i);
            helper.setBlock(pos, blocks.get(i));
            helper.getBlockEntity(pos, BaseStorageBlockEntity.class).getItemStackStorageHandler().setStackInSlot(0, new ItemStack(MARKERS.get(i), 3));
            helper.destroyBlock(pos);
        }

        helper.succeedWhen(() -> {
            for (Item marker : MARKERS.subList(0, blocks.size())) {
                helper.assertItemEntityPresent(marker);
            }
        });
    }

    /** Contents and lock code survive the chunk save/load round trip block entity state moved onto, for every chest. */
    private static void everyStorageBlockSurvivesSaveLoad(GameTestHelper helper) {
        HolderLookup.Provider registries = helper.getLevel().registryAccess();
        List<Block> blocks = storageBlocks();

        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            BlockPos pos = spread(i);
            helper.setBlock(pos, block);

            BaseStorageBlockEntity storage = helper.getBlockEntity(pos, BaseStorageBlockEntity.class);
            int last = storage.getItemStackStorageHandler().getSlots() - 1;
            storage.getItemStackStorageHandler().setStackInSlot(0, new ItemStack(Items.DIAMOND, 5));
            storage.getItemStackStorageHandler().setStackInSlot(last, new ItemStack(Items.EMERALD, 2));
            storage.setLockCode(CODE);

            CompoundTag saved = storage.saveWithFullMetadata(registries);
            BlockEntity reloaded = BlockEntity.loadStatic(helper.absolutePos(pos), storage.getBlockState(), saved, registries);

            helper.assertTrue(reloaded instanceof BaseStorageBlockEntity, name(block) + " did not load back as a storage block entity");
            BaseStorageBlockEntity loaded = (BaseStorageBlockEntity) reloaded;

            helper.assertValueEqual(loaded.getLockCode(), CODE, name(block) + " lost its lock code across a save/load");
            helper.assertTrue(loaded.getItemStackStorageHandler().getStackInSlot(0).is(Items.DIAMOND), name(block) + " lost its first slot across a save/load");
            helper.assertValueEqual(loaded.getItemStackStorageHandler().getStackInSlot(0).getCount(), 5, name(block) + " first slot stack size across a save/load");
            helper.assertTrue(loaded.getItemStackStorageHandler().getStackInSlot(last).is(Items.EMERALD), name(block) + " lost its last slot across a save/load");
        }

        helper.succeed();
    }
}
