package com.grim3212.assorted.locks.common.crafting;

import com.grim3212.assorted.lib.util.DyeHelper;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.lib.util.NBTHelper;
import com.grim3212.assorted.locks.common.block.LocksBlocks;
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

/** A locked shulker box and a dye recolor the box, lock and contents kept. */
public class LockedShulkerBoxColoring extends CustomRecipe {

    public static final RecipeSerializer<LockedShulkerBoxColoring> SERIALIZER = new SimpleCraftingRecipeSerializer<>(LockedShulkerBoxColoring::new);

    public LockedShulkerBoxColoring(CraftingBookCategory category) {
        super(category);
    }

    public boolean matches(CraftingInput container, Level level) {
        int i = 0;
        int j = 0;

        for (int k = 0; k < container.size(); ++k) {
            ItemStack itemstack = container.getItem(k);
            if (!itemstack.isEmpty()) {
                if (itemstack.is(LocksBlocks.LOCKED_SHULKER_BOX.get().asItem())) {
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
                if (itemstack1.is(LocksBlocks.LOCKED_SHULKER_BOX.get().asItem())) {
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

    public RecipeSerializer<LockedShulkerBoxColoring> getSerializer() {
        return SERIALIZER;
    }

}
