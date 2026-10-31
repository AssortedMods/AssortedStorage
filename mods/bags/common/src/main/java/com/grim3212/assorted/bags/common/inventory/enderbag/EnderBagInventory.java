package com.grim3212.assorted.bags.common.inventory.enderbag;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

/**
 * The ender bag menu's 27 slots, showing whichever ender inventory the bag's lock picks right now, so
 * placing or removing the lock switches them without reopening. One per menu, which sorting mods
 * need to see the slots as one inventory.
 */
public class EnderBagInventory implements Container {

    private final Supplier<Container> target;

    public EnderBagInventory(Supplier<Container> target) {
        this.target = target;
    }

    @Override
    public int getContainerSize() {
        return this.target.get().getContainerSize();
    }

    @Override
    public boolean isEmpty() {
        return this.target.get().isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.target.get().getItem(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return this.target.get().removeItem(slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return this.target.get().removeItemNoUpdate(slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.target.get().setItem(slot, stack);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return this.target.get().canPlaceItem(slot, stack);
    }

    @Override
    public int getMaxStackSize() {
        return this.target.get().getMaxStackSize();
    }

    @Override
    public void setChanged() {
        this.target.get().setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        this.target.get().clearContent();
    }
}
