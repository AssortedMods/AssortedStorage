package com.grim3212.assorted.locks.common.crafting;

import com.grim3212.assorted.lib.core.inventory.locking.LockItems;
import com.grim3212.assorted.lib.core.inventory.locking.StorageUtil;
import com.grim3212.assorted.locks.common.block.LocksBlocks;
import net.minecraft.world.item.AirItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;

/** A hopper and a coded padlock make a locked hopper. */
public class LockedHopperRecipe extends CustomRecipe {

    public static final RecipeSerializer<LockedHopperRecipe> SERIALIZER = new SimpleCraftingRecipeSerializer<>(LockedHopperRecipe::new);

    public LockedHopperRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput inv, Level worldIn) {
        ItemStack hopper = ItemStack.EMPTY;
        ItemStack lock = ItemStack.EMPTY;

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty())
                continue;
            Item item = stack.getItem();
            if (item instanceof AirItem)
                continue;
            if (stack.getItem() == Items.HOPPER && hopper.isEmpty())
                hopper = stack;
            else if (LockItems.isLock(stack) && lock.isEmpty() && StorageUtil.hasCode(stack))
                lock = stack;
            else
                return false;
        }
        return !hopper.isEmpty() && !lock.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider registries) {
        ItemStack hopper = ItemStack.EMPTY;
        ItemStack lock = ItemStack.EMPTY;

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            Item item = stack.getItem();
            if (stack.getItem() == Items.HOPPER && hopper.isEmpty())
                hopper = stack;
            else if (LockItems.isLock(stack) && lock.isEmpty() && StorageUtil.hasCode(stack))
                lock = stack;
        }

        if (hopper.isEmpty() || lock.isEmpty())
            return ItemStack.EMPTY;

        String lockCode = StorageUtil.getCode(lock);
        return StorageUtil.setCodeOnStack(lockCode, new ItemStack(LocksBlocks.LOCKED_HOPPER.get()));
    }


    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<LockedHopperRecipe> getSerializer() {
        return SERIALIZER;
    }

}
