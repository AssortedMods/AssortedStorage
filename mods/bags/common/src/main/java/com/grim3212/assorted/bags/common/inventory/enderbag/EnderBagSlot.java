package com.grim3212.assorted.bags.common.inventory.enderbag;

import com.grim3212.assorted.bags.common.item.EnderBagItem;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/** A slot of the ender bag's current inventory, which never takes another ender bag. */
public class EnderBagSlot extends Slot {

    public EnderBagSlot(Container inventory, int index, int xPosition, int yPosition) {
        super(inventory, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return !(stack.getItem() instanceof EnderBagItem);
    }
}
