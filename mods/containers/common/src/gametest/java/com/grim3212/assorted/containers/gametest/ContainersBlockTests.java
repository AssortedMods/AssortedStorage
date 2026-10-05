package com.grim3212.assorted.containers.gametest;

import com.grim3212.assorted.containers.api.LockerHalf;
import com.grim3212.assorted.containers.common.block.ContainersBlocks;
import com.grim3212.assorted.containers.common.block.LockerBlock;
import com.grim3212.assorted.containers.common.block.blockentity.LockerBlockEntity;
import com.grim3212.assorted.containers.common.block.blockentity.WoodCabinetBlockEntity;
import com.grim3212.assorted.lib.core.inventory.IItemStorageHandler;
import com.grim3212.assorted.lib.core.inventory.locking.LockItems;
import com.grim3212.assorted.lib.core.inventory.locking.StorageUtil;
import com.grim3212.assorted.lib.core.storage.BaseStorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.containers.gametest.ContainersTestSupport.*;

/**
 * Storage blocks: holding, dropping and saving their contents, hoppers and the double locker.
 */
final class ContainersBlockTests {

    private ContainersBlockTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("storage_block_drops_contents_and_lock", ContainersBlockTests::storageBlockDropsContentsAndLock);
        out.accept("storage_block_survives_save_load", ContainersBlockTests::storageBlockSurvivesSaveLoad);
        out.accept("hoppers_feed_and_empty_cabinet", ContainersBlockTests::hoppersFeedAndEmptyCabinet);
        out.accept("every_storage_block_holds_items", ContainersBlockTests::everyStorageBlockHoldsItems);
        out.accept("double_locker_shares_one_inventory", ContainersBlockTests::doubleLockerSharesOneInventory);
        out.accept("every_storage_block_drops_its_contents", ContainersBlockTests::everyStorageBlockDropsItsContents);
        out.accept("every_storage_block_survives_save_load", ContainersBlockTests::everyStorageBlockSurvivesSaveLoad);
    }

    /**
     * Breaking a storage block spills its contents and its lock. This is the regression the
     * onRemove / preRemoveSideEffects split caused elsewhere in the workspace.
     */
    private static void storageBlockDropsContentsAndLock(GameTestHelper helper) {
        helper.setBlock(BLOCK, ContainersBlocks.WOOD_CABINET.get());
        WoodCabinetBlockEntity cabinet = helper.getBlockEntity(BLOCK, WoodCabinetBlockEntity.class);
        cabinet.getItemStackStorageHandler().setStackInSlot(0, new ItemStack(Items.DIAMOND, 5));
        cabinet.setLockCode(CODE);
        // The padlock comes from Assorted Locks, so there is only one to drop when that is installed too.
        Item lock = LockItems.createLock(CODE).getItem();

        helper.destroyBlock(BLOCK);
        helper.succeedWhen(() -> {
            helper.assertItemEntityPresent(Items.DIAMOND, BLOCK, 3.0D);
            if (lock == Items.AIR) {
                return;
            }

            // The dropped lock has to keep the code, otherwise the block can never be reopened.
            boolean codedLock = helper.getEntities(EntityTypes.ITEM).stream()
                    .map(entity -> entity.getItem())
                    .anyMatch(stack -> stack.is(lock) && CODE.equals(StorageUtil.getCode(stack)));
            helper.assertTrue(codedLock, "the dropped padlock lost its code");
        });
    }

    /** Contents and lock survive the chunk save/load round trip block entity state moved onto. */
    private static void storageBlockSurvivesSaveLoad(GameTestHelper helper) {
        helper.setBlock(BLOCK, ContainersBlocks.WOOD_CABINET.get());
        WoodCabinetBlockEntity cabinet = helper.getBlockEntity(BLOCK, WoodCabinetBlockEntity.class);
        cabinet.getItemStackStorageHandler().setStackInSlot(0, new ItemStack(Items.DIAMOND, 5));
        cabinet.getItemStackStorageHandler().setStackInSlot(26, new ItemStack(Items.EMERALD, 2));
        cabinet.setLockCode(CODE);

        HolderLookup.Provider registries = helper.getLevel().registryAccess();
        CompoundTag saved = cabinet.saveWithFullMetadata(registries);
        BlockEntity reloaded = BlockEntity.loadStatic(helper.absolutePos(BLOCK), cabinet.getBlockState(), saved, registries);

        helper.assertTrue(reloaded instanceof WoodCabinetBlockEntity, "a saved wood cabinet did not load back as one");
        WoodCabinetBlockEntity loaded = (WoodCabinetBlockEntity) reloaded;
        helper.assertValueEqual(loaded.getLockCode(), CODE, "the lock code across a save/load");

        ItemStack first = loaded.getItemStackStorageHandler().getStackInSlot(0);
        helper.assertTrue(first.is(Items.DIAMOND), "slot 0 lost its item across a save/load");
        helper.assertValueEqual(first.getCount(), 5, "slot 0 stack size across a save/load");
        helper.assertTrue(loaded.getItemStackStorageHandler().getStackInSlot(26).is(Items.EMERALD), "the last slot lost its item across a save/load");
        helper.succeed();
    }

    /**
     * A hopper above pushes in, a hopper below pulls out. This is the sided inventory bridge -
     * {@code ResourceHandler} on NeoForge, the transfer API on Fabric - and it is the part of the
     * port most likely to differ between the two.
     */
    private static void hoppersFeedAndEmptyCabinet(GameTestHelper helper) {
        BlockPos cabinetPos = new BlockPos(4, 2, 4);
        BlockPos above = cabinetPos.above();
        BlockPos below = cabinetPos.below();

        helper.setBlock(cabinetPos, ContainersBlocks.WOOD_CABINET.get());
        helper.setBlock(above, Blocks.HOPPER.defaultBlockState().setValue(HopperBlock.FACING, Direction.DOWN));
        helper.setBlock(below, Blocks.HOPPER.defaultBlockState().setValue(HopperBlock.FACING, Direction.DOWN));

        helper.getBlockEntity(above, HopperBlockEntity.class).setItem(0, new ItemStack(Items.DIAMOND, 1));
        helper.getBlockEntity(cabinetPos, WoodCabinetBlockEntity.class)
                .getItemStackStorageHandler().setStackInSlot(26, new ItemStack(Items.EMERALD, 1));

        helper.succeedWhen(() -> {
            helper.assertContainerEmpty(above);
            helper.assertContainerContains(below, Items.EMERALD);
        });
    }

    /**
     * Every storage block takes items in and hands them back, through the platform handler the
     * loaders bridge onto - the same object a hopper or a pipe sees, not the raw list behind it.
     * Driven off the block list so a new storage block is covered the day it is registered.
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

    /**
     * Two lockers stacked are one 90 slot inventory: the top half is slots 0-44 and the bottom
     * 45-89. The halves are set explicitly because {@code setBlock} skips the placement path that
     * would pair them.
     */
    private static void doubleLockerSharesOneInventory(GameTestHelper helper) {
        BlockPos bottom = new BlockPos(4, 1, 4);
        BlockPos top = bottom.above();

        helper.setBlock(bottom, ContainersBlocks.LOCKER.get().defaultBlockState().setValue(LockerBlock.HALF, LockerHalf.BOTTOM));
        helper.setBlock(top, ContainersBlocks.LOCKER.get().defaultBlockState().setValue(LockerBlock.HALF, LockerHalf.TOP));
        helper.assertBlockProperty(bottom, LockerBlock.HALF, LockerHalf.BOTTOM);
        helper.assertBlockProperty(top, LockerBlock.HALF, LockerHalf.TOP);

        LockerBlockEntity lower = helper.getBlockEntity(bottom, LockerBlockEntity.class);
        LockerBlockEntity upper = helper.getBlockEntity(top, LockerBlockEntity.class);

        IItemStorageHandler joined = lower.getStorageHandler().getItemStorageHandler(null);
        helper.assertValueEqual(joined.getSlots(), 90, "a double locker's joined slot count");

        helper.assertTrue(joined.insertItem(0, new ItemStack(Items.DIAMOND, 1), false).isEmpty(), "the double locker refused an item in its first slot");
        helper.assertTrue(joined.insertItem(89, new ItemStack(Items.EMERALD, 1), false).isEmpty(), "the double locker refused an item in its last slot");

        helper.assertTrue(upper.getItemStackStorageHandler().getStackInSlot(0).is(Items.DIAMOND), "the first joined slot did not land in the top half");
        helper.assertTrue(lower.getItemStackStorageHandler().getStackInSlot(44).is(Items.EMERALD), "the last joined slot did not land in the bottom half");
        helper.succeed();
    }

    /**
     * Breaking a storage block gives its contents back. Most spill from {@code
     * preRemoveSideEffects}; the gold safe keeps them in the dropped item.
     * Each block gets its own marker item.
     */
    private static void everyStorageBlockDropsItsContents(GameTestHelper helper) {
        List<Block> keepsContentsInTheItem = List.of(ContainersBlocks.GOLD_SAFE.get());
        List<Block> blocks = storageBlocks();
        List<Item> spilled = new ArrayList<>();

        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            BlockPos pos = spread(i);
            Item marker = MARKERS.get(i);

            helper.setBlock(pos, block);
            helper.getBlockEntity(pos, BaseStorageBlockEntity.class).getItemStackStorageHandler().setStackInSlot(0, new ItemStack(marker, 3));

            if (keepsContentsInTheItem.contains(block)) {
                // Needs the loot table to run, which helper.destroyBlock never reaches.
                helper.getLevel().destroyBlock(helper.absolutePos(pos), true);
            } else {
                helper.destroyBlock(pos);
                spilled.add(marker);
            }
        }

        helper.succeedWhen(() -> {
            for (Item marker : spilled) {
                helper.assertItemEntityPresent(marker);
            }

            for (Block block : keepsContentsInTheItem) {
                Item marker = MARKERS.get(blocks.indexOf(block));
                helper.assertTrue(droppedItemCarries(helper, block.asItem(), marker), name(block) + " dropped without the contents it is supposed to carry");
            }
        });
    }

    /**
     * Contents and lock code survive the chunk save/load round trip block entity state moved onto,
     * for every storage block rather than the one the first pass covered.
     */
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
