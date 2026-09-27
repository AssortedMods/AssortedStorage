package com.grim3212.assorted.locks.common.handlers;

import com.grim3212.assorted.lib.core.inventory.locking.BaseLockedBlockEntity;
import com.grim3212.assorted.lib.core.inventory.locking.LockConversion;
import com.grim3212.assorted.lib.core.inventory.locking.LockConversions;
import com.grim3212.assorted.lib.core.inventory.locking.LockItems;
import com.grim3212.assorted.locks.common.block.LocksBlocks;
import com.grim3212.assorted.locks.common.item.LocksItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import java.util.function.Supplier;

/**
 * The padlock every storage mod's locked blocks drop, and what it turns each door into. The other Assorted Storage
 * mods register their own containers.
 */
public class LocksLocks {

    public static void init() {
        LockItems.registerLockItem(LocksItems.LOCKSMITH_LOCK);

        registerDoors();
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
