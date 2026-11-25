package com.grim3212.assorted.shulkers.common.crafting;

import com.grim3212.assorted.lib.util.DyeHelper;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.lib.util.NBTHelper;
import com.grim3212.assorted.lib.core.storage.shulker.LockedShulkerBoxBlock;
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

public class LockedShulkerBoxColoring extends CustomRecipe {

    public static final RecipeSerializer<LockedShulkerBoxColoring> SERIALIZER = new SimpleCraftingRecipeSerializer<>(LockedShulkerBoxColoring::new);

    public LockedShulkerBoxColoring(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput container, Level level) {
        int i = 0;
        int j = 0;

        for (int k = 0; k < container.size(); ++k) {
            ItemStack itemstack = container.getItem(k);
            if (!itemstack.isEmpty()) {
                if (isMaterialShulkerBox(itemstack)) {
                    ++i;
                } else {
                    if (!itemstack.is(LibCommonTags.Items.DYES)) {
                        return false;
                    }

                    ++j;
                }

                if (j > 1 || i > 1) {
                    return false;
                }
            }
        }

        return i == 1 && j == 1;
    }

    @Override
    public ItemStack assemble(CraftingInput container, HolderLookup.Provider registries) {
        ItemStack itemstack = ItemStack.EMPTY;
        DyeColor dyecolor = DyeColor.WHITE;

        for (int i = 0; i < container.size(); ++i) {
            ItemStack itemstack1 = container.getItem(i);
            if (!itemstack1.isEmpty()) {
                Item item = itemstack1.getItem();
                if (isMaterialShulkerBox(itemstack1)) {
                    itemstack = itemstack1;
                } else {
                    DyeColor tmp = DyeHelper.getColor(itemstack1);
                    if (tmp != null)
                        dyecolor = tmp;
                }
            }
        }

        ItemStack copy = itemstack.copyWithCount(1);
        NBTHelper.putInt(copy, "Color", dyecolor.getId());
        return copy;
    }


    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    /** Assorted Locks colors its own locked shulker box. */
    private static boolean isMaterialShulkerBox(ItemStack stack) {
        return Block.byItem(stack.getItem()) instanceof LockedShulkerBoxBlock shulker && shulker.getStorageMaterial() != null;
    }

    @Override
    public RecipeSerializer<LockedShulkerBoxColoring> getSerializer() {
        return SERIALIZER;
    }

}
