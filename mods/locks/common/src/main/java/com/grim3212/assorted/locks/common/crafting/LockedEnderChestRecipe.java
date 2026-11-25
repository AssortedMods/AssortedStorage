package com.grim3212.assorted.locks.common.crafting;

import com.grim3212.assorted.lib.core.inventory.locking.LockItems;
import com.grim3212.assorted.lib.core.inventory.locking.StorageUtil;
import com.grim3212.assorted.locks.common.block.LocksBlocks;
import net.minecraft.world.item.AirItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

/** An ender chest and a coded padlock make a locked ender chest. */
public class LockedEnderChestRecipe extends CustomRecipe {

    public static final RecipeSerializer<LockedEnderChestRecipe> SERIALIZER = new SimpleCraftingRecipeSerializer<>(LockedEnderChestRecipe::new);

    public LockedEnderChestRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput inv, Level worldIn) {
        ItemStack enderChest = ItemStack.EMPTY;
        ItemStack lock = ItemStack.EMPTY;

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty())
                continue;
            Item item = stack.getItem();
            if (item instanceof AirItem)
                continue;
            if (item == Blocks.ENDER_CHEST.asItem() && enderChest.isEmpty())
                enderChest = stack;
            else if (LockItems.isLock(stack) && lock.isEmpty() && StorageUtil.hasCode(stack))
                lock = stack;
            else
                return false;
        }
        return !enderChest.isEmpty() && !lock.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider registries) {
        ItemStack enderChest = ItemStack.EMPTY;
        ItemStack lock = ItemStack.EMPTY;

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            Item item = stack.getItem();
            if (item == Blocks.ENDER_CHEST.asItem() && enderChest.isEmpty())
                enderChest = stack;
            else if (LockItems.isLock(stack) && lock.isEmpty() && StorageUtil.hasCode(stack))
                lock = stack;
        }

        if (enderChest.isEmpty() || lock.isEmpty())
            return ItemStack.EMPTY;

        String lockCode = StorageUtil.getCode(lock);
        return StorageUtil.setCodeOnStack(lockCode, new ItemStack(LocksBlocks.LOCKED_ENDER_CHEST.get()));
    }


    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<LockedEnderChestRecipe> getSerializer() {
        return SERIALIZER;
    }

}
