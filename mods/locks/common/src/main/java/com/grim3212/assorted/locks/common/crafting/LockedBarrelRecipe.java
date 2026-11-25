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

/** A wooden barrel and a coded padlock make a locked barrel. */
public class LockedBarrelRecipe extends CustomRecipe {

    public static final RecipeSerializer<LockedBarrelRecipe> SERIALIZER = new SimpleCraftingRecipeSerializer<>(LockedBarrelRecipe::new);

    public LockedBarrelRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput inv, Level worldIn) {
        ItemStack barrel = ItemStack.EMPTY;
        ItemStack lock = ItemStack.EMPTY;

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty())
                continue;
            Item item = stack.getItem();
            if (item instanceof AirItem)
                continue;
            if (stack.is(LibCommonTags.Items.BARRELS_WOODEN) && barrel.isEmpty())
                barrel = stack;
            else if (LockItems.isLock(stack) && lock.isEmpty() && StorageUtil.hasCode(stack))
                lock = stack;
            else
                return false;
        }
        return !barrel.isEmpty() && !lock.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider registries) {
        ItemStack barrel = ItemStack.EMPTY;
        ItemStack lock = ItemStack.EMPTY;

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            Item item = stack.getItem();
            if (stack.is(LibCommonTags.Items.BARRELS_WOODEN) && barrel.isEmpty())
                barrel = stack;
            else if (LockItems.isLock(stack) && lock.isEmpty() && StorageUtil.hasCode(stack))
                lock = stack;
        }

        if (barrel.isEmpty() || lock.isEmpty())
            return ItemStack.EMPTY;

        String lockCode = StorageUtil.getCode(lock);
        return StorageUtil.setCodeOnStack(lockCode, new ItemStack(LocksBlocks.LOCKED_BARREL.get()));
    }


    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<LockedBarrelRecipe> getSerializer() {
        return SERIALIZER;
    }

}
