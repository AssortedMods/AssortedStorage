package com.grim3212.assorted.storage.common.handlers;

import com.grim3212.assorted.lib.core.inventory.locking.BaseLockedBlockEntity;
import com.grim3212.assorted.lib.core.inventory.locking.LockConversion;
import com.grim3212.assorted.lib.core.inventory.locking.LockConversions;
import com.grim3212.assorted.lib.core.inventory.locking.LockItems;
import com.grim3212.assorted.storage.common.block.LockedBarrelBlock;
import com.grim3212.assorted.storage.common.block.LockedHopperBlock;
import com.grim3212.assorted.storage.common.block.StorageBlocks;
import com.grim3212.assorted.storage.common.block.blockentity.LockedEnderChestBlockEntity;
import com.grim3212.assorted.storage.common.block.blockentity.LockedShulkerBoxBlockEntity;
import com.grim3212.assorted.storage.common.item.StorageItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import java.util.function.Supplier;

/**
 * What a padlock turns each vanilla block into. Each group is one future mod's share, which that mod
 * will register for itself.
 */
public class StorageLocks {

    public static void init() {
        LockItems.registerLockItem(StorageItems.LOCKSMITH_LOCK);

        registerChests();
        registerBarrels();
        registerShulkers();
        registerHoppers();
        registerDoors();
    }

    private static void registerChests() {
        LockConversions.register(Blocks.CHEST, LockConversions.container(StorageBlocks.LOCKED_CHEST, (from, to) -> to.setValue(HorizontalDirectionalBlock.FACING, from.getValue(HorizontalDirectionalBlock.FACING))));
        LockConversions.register(Blocks.ENDER_CHEST, (level, pos, state, code) -> {
            if (!(level.getBlockEntity(pos) instanceof EnderChestBlockEntity)) {
                return false;
            }

            level.setBlock(pos, StorageBlocks.LOCKED_ENDER_CHEST.get().defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, state.getValue(HorizontalDirectionalBlock.FACING)), Block.UPDATE_ALL);
            if (level.getBlockEntity(pos) instanceof LockedEnderChestBlockEntity chest) {
                chest.setLockCode(code);
            }

            return true;
        });
    }

    private static void registerBarrels() {
        LockConversions.register(Blocks.BARREL, LockConversions.container(StorageBlocks.LOCKED_BARREL, (from, to) -> to.setValue(LockedBarrelBlock.FACING, from.getValue(LockedBarrelBlock.FACING))));
    }

    private static void registerShulkers() {
        LockConversion shulker = (level, pos, state, code) -> {
            if (!(level.getBlockEntity(pos) instanceof ShulkerBoxBlockEntity previous)) {
                return false;
            }

            // Not emptied first like the others: a shulker box keeps its contents when it is removed
            NonNullList<ItemStack> items = NonNullList.withSize(previous.getContainerSize(), ItemStack.EMPTY);
            for (int i = 0; i < previous.getContainerSize(); i++) {
                items.set(i, previous.getItem(i).copy());
            }

            level.setBlock(pos, StorageBlocks.LOCKED_SHULKER_BOX.get().defaultBlockState().setValue(ShulkerBoxBlock.FACING, state.getValue(ShulkerBoxBlock.FACING)), Block.UPDATE_ALL);
            if (level.getBlockEntity(pos) instanceof LockedShulkerBoxBlockEntity shulkerBE) {
                shulkerBE.getItemStackStorageHandler().setStacks(items);
                shulkerBE.setLockCode(code);
                shulkerBE.setColor(state.getBlock() instanceof ShulkerBoxBlock shulkerBlock ? shulkerBlock.getColor() : null);
            }

            return true;
        };

        LockConversions.register(Blocks.SHULKER_BOX, shulker);
        Blocks.DYED_SHULKER_BOX.forEach(dyed -> LockConversions.register(dyed, shulker));
    }

    private static void registerHoppers() {
        LockConversions.register(Blocks.HOPPER, LockConversions.container(StorageBlocks.LOCKED_HOPPER, (from, to) -> to.setValue(LockedHopperBlock.FACING, from.getValue(LockedHopperBlock.FACING))));
    }

    private static void registerDoors() {
        StorageBlocks.VANILLA_DOORS.forEach((door, locked) -> LockConversions.register(door, door(locked)));

        // Assorted Decor's doors, by id since Decor may not be installed
        LockConversions.register(Identifier.parse("assorteddecor:quartz_door"), door(StorageBlocks.LOCKED_QUARTZ_DOOR));
        LockConversions.register(Identifier.parse("assorteddecor:glass_door"), door(StorageBlocks.LOCKED_GLASS_DOOR));
        LockConversions.register(Identifier.parse("assorteddecor:steel_door"), door(StorageBlocks.LOCKED_STEEL_DOOR));
        LockConversions.register(Identifier.parse("assorteddecor:chain_link_door"), door(StorageBlocks.LOCKED_CHAIN_LINK_DOOR));
    }

    private static LockConversion door(Supplier<? extends Block> locked) {
        return (level, pos, currentDoor, code) -> {
            BlockState newState = locked.get().defaultBlockState().setValue(DoorBlock.FACING, currentDoor.getValue(DoorBlock.FACING)).setValue(DoorBlock.OPEN, currentDoor.getValue(DoorBlock.OPEN)).setValue(DoorBlock.HINGE, currentDoor.getValue(DoorBlock.HINGE));
            DoubleBlockHalf currentHalf = currentDoor.getValue(DoorBlock.HALF);
            BlockPos otherHalf = currentHalf == DoubleBlockHalf.UPPER ? pos.below() : pos.above();
            // The clicked half must not ask its unlocked partner whether to stay, but still has to reach other players
            level.setBlock(pos, newState.setValue(DoorBlock.HALF, currentHalf), Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
            level.setBlock(otherHalf, newState.setValue(DoorBlock.HALF, currentHalf == DoubleBlockHalf.UPPER ? DoubleBlockHalf.LOWER : DoubleBlockHalf.UPPER), Block.UPDATE_ALL);

            if (level.getBlockEntity(pos) instanceof BaseLockedBlockEntity lockedDoor) {
                lockedDoor.setLockCode(code);
            }

            if (level.getBlockEntity(otherHalf) instanceof BaseLockedBlockEntity otherHalfLockedDoor) {
                otherHalfLockedDoor.setLockCode(code);
            }

            return true;
        };
    }
}
