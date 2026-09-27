package com.grim3212.assorted.locks.common.handlers;

import com.grim3212.assorted.lib.core.inventory.locking.BaseLockedBlockEntity;
import com.grim3212.assorted.lib.core.inventory.locking.LockConversion;
import com.grim3212.assorted.lib.core.inventory.locking.LockConversions;
import com.grim3212.assorted.lib.core.inventory.locking.LockItems;
import com.grim3212.assorted.lib.core.storage.barrel.LockedBarrelBlock;
import com.grim3212.assorted.lib.core.storage.hopper.LockedHopperBlock;
import com.grim3212.assorted.lib.core.storage.shulker.LockedShulkerBoxBlockEntity;
import com.grim3212.assorted.locks.common.block.LocksBlocks;
import com.grim3212.assorted.locks.common.block.blockentity.LockedEnderChestBlockEntity;
import com.grim3212.assorted.locks.common.item.LocksItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
 * The padlock every storage mod's locked blocks drop, and what it turns each door and vanilla container into. The
 * other Assorted Storage mods register their own containers.
 */
public class LocksLocks {

    public static void init() {
        LockItems.registerLockItem(LocksItems.LOCKSMITH_LOCK);

        registerDoors();
        registerContainers();
    }

    private static void registerContainers() {
        LockConversions.register(Blocks.CHEST, LockConversions.container(LocksBlocks.LOCKED_CHEST, (from, to) -> to.setValue(HorizontalDirectionalBlock.FACING, from.getValue(HorizontalDirectionalBlock.FACING))));
        LockConversions.register(Blocks.BARREL, LockConversions.container(LocksBlocks.LOCKED_BARREL, (from, to) -> to.setValue(LockedBarrelBlock.FACING, from.getValue(LockedBarrelBlock.FACING))));
        LockConversions.register(Blocks.HOPPER, LockConversions.container(LocksBlocks.LOCKED_HOPPER, (from, to) -> to.setValue(LockedHopperBlock.FACING, from.getValue(LockedHopperBlock.FACING))));
        LockConversions.register(Blocks.ENDER_CHEST, LocksLocks::enderChest);

        LockConversions.register(Blocks.SHULKER_BOX, LocksLocks::shulkerBox);
        Blocks.DYED_SHULKER_BOX.forEach(dyed -> LockConversions.register(dyed, LocksLocks::shulkerBox));
    }

    /** An ender chest holds nothing of its own, so only the lock moves across. */
    private static boolean enderChest(Level level, BlockPos pos, BlockState state, String code) {
        if (!(level.getBlockEntity(pos) instanceof EnderChestBlockEntity)) {
            return false;
        }

        level.setBlock(pos, LocksBlocks.LOCKED_ENDER_CHEST.get().defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, state.getValue(HorizontalDirectionalBlock.FACING)), Block.UPDATE_ALL);
        if (level.getBlockEntity(pos) instanceof LockedEnderChestBlockEntity chest) {
            chest.setLockCode(code);
        }

        return true;
    }

    private static boolean shulkerBox(Level level, BlockPos pos, BlockState state, String code) {
        if (!(level.getBlockEntity(pos) instanceof ShulkerBoxBlockEntity previous)) {
            return false;
        }

        // Copied rather than emptied like the others, as a shulker box keeps its contents when it is removed
        NonNullList<ItemStack> items = NonNullList.withSize(previous.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < previous.getContainerSize(); i++) {
            items.set(i, previous.getItem(i).copy());
        }

        level.setBlock(pos, LocksBlocks.LOCKED_SHULKER_BOX.get().defaultBlockState().setValue(ShulkerBoxBlock.FACING, state.getValue(ShulkerBoxBlock.FACING)), Block.UPDATE_ALL);
        if (level.getBlockEntity(pos) instanceof LockedShulkerBoxBlockEntity shulkerBE) {
            shulkerBE.getItemStackStorageHandler().setStacks(items);
            shulkerBE.setLockCode(code);
            shulkerBE.setColor(state.getBlock() instanceof ShulkerBoxBlock shulkerBlock ? shulkerBlock.getColor() : null);
        }

        return true;
    }

    private static void registerDoors() {
        LocksBlocks.VANILLA_DOORS.forEach((door, locked) -> LockConversions.register(door, door(locked)));

        LockConversions.register(LocksBlocks.QUARTZ_DOOR, door(LocksBlocks.LOCKED_QUARTZ_DOOR));
        LockConversions.register(LocksBlocks.GLASS_DOOR, door(LocksBlocks.LOCKED_GLASS_DOOR));
        LockConversions.register(LocksBlocks.STEEL_DOOR, door(LocksBlocks.LOCKED_STEEL_DOOR));
        LockConversions.register(LocksBlocks.CHAIN_LINK_DOOR, door(LocksBlocks.LOCKED_CHAIN_LINK_DOOR));
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
