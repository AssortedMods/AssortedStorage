package com.grim3212.assorted.shulkers.common.handlers;

import com.grim3212.assorted.lib.core.inventory.locking.LockConversion;
import com.grim3212.assorted.lib.core.inventory.locking.LockConversions;
import com.grim3212.assorted.shulkers.common.block.ShulkersBlocks;
import com.grim3212.assorted.shulkers.common.block.blockentity.LockedShulkerBoxBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;

/** What a padlock turns a vanilla shulker box into, dyed or not. */
public class ShulkersLocks {

    public static void init() {
        LockConversion shulker = (level, pos, state, code) -> {
            if (!(level.getBlockEntity(pos) instanceof ShulkerBoxBlockEntity previous)) {
                return false;
            }

            // Not emptied first like the others: a shulker box keeps its contents when it is removed
            NonNullList<ItemStack> items = NonNullList.withSize(previous.getContainerSize(), ItemStack.EMPTY);
            for (int i = 0; i < previous.getContainerSize(); i++) {
                items.set(i, previous.getItem(i).copy());
            }

            level.setBlock(pos, ShulkersBlocks.LOCKED_SHULKER_BOX.get().defaultBlockState().setValue(ShulkerBoxBlock.FACING, state.getValue(ShulkerBoxBlock.FACING)), Block.UPDATE_ALL);
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
}
