package com.grim3212.assorted.locks.common.crafting;

import com.grim3212.assorted.lib.core.inventory.locking.LockItems;
import com.grim3212.assorted.lib.core.inventory.locking.StorageUtil;
import com.grim3212.assorted.lib.util.LibCommonTags;
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

/** A wooden chest and a coded padlock make a locked chest. */
public class LockedChestRecipe extends CustomRecipe {

    public static final RecipeSerializer<LockedChestRecipe> SERIALIZER = new SimpleCraftingRecipeSerializer<>(LockedChestRecipe::new);

    public LockedChestRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput inv, Level worldIn) {
        ItemStack chest = ItemStack.EMPTY;
        ItemStack lock = ItemStack.EMPTY;

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty())
                continue;
            Item item = stack.getItem();
            if (item instanceof AirItem)
                continue;
            if (stack.is(LibCommonTags.Items.CHESTS_WOODEN) && chest.isEmpty())
                chest = stack;
            else if (LockItems.isLock(stack) && lock.isEmpty() && StorageUtil.hasCode(stack))
                lock = stack;
            else
                return false;
        }
        return !chest.isEmpty() && !lock.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider registries) {
        ItemStack chest = ItemStack.EMPTY;
        ItemStack lock = ItemStack.EMPTY;

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            Item item = stack.getItem();
            if (stack.is(LibCommonTags.Items.CHESTS_WOODEN) && chest.isEmpty())
                chest = stack;
            else if (LockItems.isLock(stack) && lock.isEmpty() && StorageUtil.hasCode(stack))
                lock = stack;
        }

        if (chest.isEmpty() || lock.isEmpty())
            return ItemStack.EMPTY;

        String lockCode = StorageUtil.getCode(lock);
        return StorageUtil.setCodeOnStack(lockCode, new ItemStack(LocksBlocks.LOCKED_CHEST.get()));
    }


    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<LockedChestRecipe> getSerializer() {
        return SERIALIZER;
    }

}
