package com.grim3212.assorted.locks.common.crafting;

import com.grim3212.assorted.lib.core.inventory.locking.LockItems;
import com.grim3212.assorted.lib.core.inventory.locking.StorageUtil;
import com.grim3212.assorted.lib.util.NBTHelper;
import com.grim3212.assorted.locks.common.block.LocksBlocks;
import net.minecraft.world.item.AirItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;

/** A vanilla shulker box, dyed or not, and a coded padlock make a locked shulker box that keeps its contents. */
public class LockedShulkerBoxRecipe extends CustomRecipe {

    public static final RecipeSerializer<LockedShulkerBoxRecipe> SERIALIZER = new SimpleCraftingRecipeSerializer<>(LockedShulkerBoxRecipe::new);

    public LockedShulkerBoxRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput inv, Level worldIn) {
        ItemStack shulker = ItemStack.EMPTY;
        ItemStack lock = ItemStack.EMPTY;

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty())
                continue;
            Item item = stack.getItem();
            if (item instanceof AirItem)
                continue;
            if (Block.byItem(item) instanceof ShulkerBoxBlock && shulker.isEmpty())
                shulker = stack;
            else if (LockItems.isLock(stack) && lock.isEmpty() && StorageUtil.hasCode(stack))
                lock = stack;
            else
                return false;
        }
        return !shulker.isEmpty() && !lock.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider registries) {
        ItemStack shulker = ItemStack.EMPTY;
        ItemStack lock = ItemStack.EMPTY;

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            Item item = stack.getItem();
            if (Block.byItem(item) instanceof ShulkerBoxBlock && shulker.isEmpty())
                shulker = stack;
            else if (LockItems.isLock(stack) && lock.isEmpty() && StorageUtil.hasCode(stack))
                lock = stack;
        }

        if (shulker.isEmpty() || lock.isEmpty())
            return ItemStack.EMPTY;

        DyeColor color = Block.byItem(shulker.getItem()) instanceof ShulkerBoxBlock shulkerBlock ? shulkerBlock.getColor() : null;

        String lockCode = StorageUtil.getCode(lock);
        ItemStack output = new ItemStack(LocksBlocks.LOCKED_SHULKER_BOX.get());
        // The shulker box's component patch carries its contents and name onto the locked one.
        output.applyComponents(shulker.getComponentsPatch());

        StorageUtil.writeCodeToStack(lockCode, output);
        NBTHelper.putInt(output, "Color", color == null ? -1 : color.getId());
        return output;
    }


    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<LockedShulkerBoxRecipe> getSerializer() {
        return SERIALIZER;
    }

}
