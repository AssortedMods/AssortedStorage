package com.grim3212.assorted.crates.common.inventory.crates;

import com.grim3212.assorted.lib.core.inventory.IItemStorageHandler;
import com.grim3212.assorted.lib.core.inventory.IValueSerializable;
import com.grim3212.assorted.lib.core.inventory.locking.LockedStorageHandler;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.util.NBTHelper;
import com.grim3212.assorted.crates.api.LargeItemStack;
import com.grim3212.assorted.lib.core.storage.ICrateUpgrade;
import com.grim3212.assorted.crates.common.block.blockentity.CrateBlockEntity;
import com.grim3212.assorted.crates.common.block.blockentity.CrateCompactingBlockEntity;
import com.grim3212.assorted.crates.common.item.CratesItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import java.util.stream.IntStream;

public class CrateSidedInv implements IItemStorageHandler, LockedStorageHandler, IValueSerializable {
    protected final CrateBlockEntity inv;

    // One for each slots on the face of a Storage Crate
    protected NonNullList<LargeItemStack> slotContents;
    // Upgrade slots as well as lock slot
    protected NonNullList<ItemStack> enhancements;

    public CrateSidedInv(CrateBlockEntity inv) {
        this.inv = inv;

        this.slotContents = NonNullList.withSize(this.inv.getLayout().getNumStacks(), LargeItemStack.empty());
        // 8 upgrades and 1 lock slot
        this.enhancements = NonNullList.<ItemStack>withSize(9, ItemStack.EMPTY);
    }

    public CrateBlockEntity getInv() {
        return inv;
    }

    private boolean codeMatches(String s) {
        return !this.inv.isLocked() || this.inv.getLockCode().equals(s);
    }

    public NonNullList<ItemStack> getEnhancements() {
        return enhancements;
    }

    public NonNullList<LargeItemStack> getSlotContents() {
        return slotContents;
    }

    /**
     * Puts an upgrade in an enhancement slot. The block update is what sends the crate's new
     * enhancements to every client that can see it; the renderer draws them straight off that list,
     * so a crate upgraded without this only changed for the player who upgraded it.
     */
    public void setEnhancement(int index, ItemStack stack) {
        this.enhancements.set(index, stack);
        this.inv.setChanged();
        this.inv.modelUpdate();
    }

    public ItemStack removeEnhancement(int index, int count) {
        ItemStack stack = ContainerHelper.removeItem(this.enhancements, index, count);
        if (!stack.isEmpty()) {
            this.inv.setChanged();
            this.inv.modelUpdate();
        }
        return stack;
    }

    @Override
    public int getSlots() {
        return this.slotContents.size();
    }

    /**
     * The whole slot, past a stack: {@link IItemStorageHandler} and both loaders' item handlers allow
     * an oversized stack, which is how readout mods see a crate's real count. A locked slot that has
     * run empty reports empty; the crate screen shows its reserved item from
     * {@link #getLargeItemStack(int)}.
     */
    @Override
    @NotNull
    public ItemStack getStackInSlot(int inSlot) {
        int slot = getSlot(inSlot);
        if (slot == -1) {
            return ItemStack.EMPTY;
        }

        LargeItemStack largeStack = this.slotContents.get(slot);

        if (largeStack.getStack().isEmpty() || largeStack.getAmount() <= 0) {
            return ItemStack.EMPTY;
        }

        return largeStack.getStack().copyWithCount(largeStack.getAmount());
    }

    public LargeItemStack getLargeItemStack(int index) {
        LargeItemStack largeStack = this.slotContents.get(index);
        return !largeStack.isEmpty() && !largeStack.getStack().isEmpty() ? largeStack : LargeItemStack.empty();
    }

    public void setSlotLocked(int index, boolean lock) {
        LargeItemStack largeStack = this.slotContents.get(index);

        if (largeStack.getStack().isEmpty()) {
            // No locking an empty slot
            return;
        }

        largeStack.setLock(lock);
        if (!lock && largeStack.getAmount() <= 0) {
            largeStack.setEmpty();
        }

        this.inv.setSlotChanged(index);
    }

    public void setAllSlotsLocked(boolean lock) {
        for (int i = 0; i < this.slotContents.size(); i++) {
            this.setSlotLocked(i, lock);
        }
    }

    public boolean isSlotLocked(int index) {
        LargeItemStack largeStack = this.slotContents.get(index);
        return largeStack.isLocked() && !largeStack.isEmpty();
    }

    public boolean anySlotsLocked() {
        return this.slotContents.stream().anyMatch(stack -> stack.isLocked());
    }

    @Override
    @NotNull
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return this.insertItem(slot, stack, simulate, "", false);
    }

    @Override
    @NotNull
    public ItemStack insertItem(int inSlot, @NotNull ItemStack stack, boolean simulate, String inLockCode, boolean ignoreLock) {
        if (stack.isEmpty())
            return ItemStack.EMPTY;

        if (!isItemValid(inSlot, stack) || (!codeMatches(inLockCode) && !ignoreLock))
            return stack;

        int slot = getSlot(inSlot);
        if (slot == -1)
            return stack;

        LargeItemStack stackInSlot = getLargeItemStack(slot);

        int m;
        if (!stackInSlot.isEmpty()) {
            if (stackInSlot.getAmount() >= getSlotLimit(slot) && !hasVoidUpgrade())
                return stack;

            if (!Services.INVENTORY.canItemStacksStack(stack, stackInSlot.getStack()))
                return stack;

            m = getSlotLimit(slot) - stackInSlot.getAmount();

            if (stack.getCount() <= m || hasVoidUpgrade()) {
                if (!simulate) {
                    addItem(slot, stack.copy());
                }

                return ItemStack.EMPTY;
            } else {
                // copy the stack to not modify the original one
                stack = stack.copy();
                if (!simulate) {
                    addItem(slot, stack.split(m));
                    return stack;
                } else {
                    stack.shrink(m);
                    return stack;
                }
            }
        } else {
            if (inv instanceof CrateCompactingBlockEntity compactingCrate) {
                if (!compactingCrate.getItemStackStorageHandler().isItemValid(slot, stack)) {
                    return stack;
                }
            }

            m = Math.min(stack.getMaxStackSize(), getSlotLimit(slot));
            if (m < stack.getCount()) {
                // copy the stack to not modify the original one
                stack = stack.copy();
                if (!simulate) {
                    addItem(slot, stack.split(m));
                    return stack;
                } else {
                    stack.shrink(m);
                    return stack;
                }
            } else {
                if (!simulate)
                    addItem(slot, stack);
                return ItemStack.EMPTY;
            }
        }

    }

    @Override
    @NotNull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return this.extractItem(slot, amount, simulate, "", false);
    }

    @Override
    @NotNull
    public ItemStack extractItem(int inSlot, int amount, boolean simulate, String inLockCode, boolean ignoreLock) {
        if (amount == 0 || (!codeMatches(inLockCode) && !ignoreLock))
            return ItemStack.EMPTY;

        int slot = getSlot(inSlot);
        if (slot == -1)
            return ItemStack.EMPTY;

        LargeItemStack stackInSlot = getLargeItemStack(slot);

        if (stackInSlot.isEmpty())
            return ItemStack.EMPTY;

        if (simulate) {
            if (stackInSlot.getAmount() < amount) {
                return stackInSlot.getStack().copyWithCount(stackInSlot.getAmount());
            } else {
                // A copy: the slot's own stack is shared with the slot and must not be resized.
                return stackInSlot.getStack().copyWithCount(amount);
            }
        } else {
            int m = Math.min(stackInSlot.getAmount(), amount);
            ItemStack ret = removeItem(slot, m);
            return ret;
        }
    }

    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack;
        if (slot >= 0 && slot < this.slotContents.size() && !this.slotContents.get(slot).isEmpty() && amount > 0) {
            // Not getStackInSlot: it reports the whole slot, which is no limit on one move.
            int maxStackSize = this.slotContents.get(slot).getStack().getMaxStackSize();

            stack = this.slotContents.get(slot).split(Math.min(maxStackSize, amount), true);
        } else {
            stack = ItemStack.EMPTY;
        }

        if (!stack.isEmpty()) {
            this.inv.setSlotChanged(slot);
        }
        return stack;
    }

    @Override
    public int getSlotLimit(int slot) {
        return getMaxStackSizeForSlot(slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        int slot1 = getSlot(slot);
        return slot1 != -1;
    }

    /**
     * Lossy: keeps the slot's rotation and lock but takes its amount from {@code stack}. Rollbacks go
     * through {@link #captureSlot(int)} instead.
     */
    @Override
    public void setStackInSlot(int inSlot, @NotNull ItemStack stack) {
        int slot = getSlot(inSlot);
        if (slot == -1) {
            return;
        }

        if (slot >= 0 && slot < this.slotContents.size()) {
            LargeItemStack largeStack = this.getLargeItemStack(slot).withStack(stack);

            if (largeStack.getAmount() > this.getMaxStackSizeForSlot(slot)) {
                largeStack.setAmount(this.getMaxStackSizeForSlot(slot));
            }

            this.slotContents.set(slot, largeStack);
        }

        this.inv.setSlotChanged(slot);
    }

    /** Snapshots the LargeItemStack: amount, rotation and lock do not survive an ItemStack. */
    @Override
    @NotNull
    public Runnable captureSlot(int inSlot) {
        int slot = getSlot(inSlot);
        if (slot == -1) {
            return () -> {
            };
        }

        LargeItemStack snapshot = this.slotContents.get(slot).copy();
        return () -> this.restoreSlot(slot, snapshot);
    }

    /**
     * Applies a slot verbatim, with no re-capping: a compactor's cap comes from the recipe manager,
     * which a client does not have.
     */
    public void restoreSlot(int slot, LargeItemStack stack) {
        if (slot >= 0 && slot < this.slotContents.size()) {
            this.slotContents.set(slot, stack);
        }

        this.inv.setSlotChanged(slot);
    }

    /** Sets a slot, capped at what the slot can hold. */
    public void setItem(int slot, LargeItemStack stack) {
        if (slot >= 0 && slot < this.slotContents.size()) {

            if (stack.getAmount() > this.getMaxStackSizeForSlot(slot)) {
                stack.setAmount(this.getMaxStackSizeForSlot(slot));
            }

            this.slotContents.set(slot, stack);
        }

        this.inv.setSlotChanged(slot);
    }

    /** Applies a slot exactly as the server sent it. See {@link #restoreSlot(int, LargeItemStack)}. */
    public void applySyncedItem(int slot, LargeItemStack stack) {
        this.restoreSlot(slot, stack);
    }

    @Override
    public boolean isEmpty() {
        for (LargeItemStack itemstack : this.slotContents) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        for (ItemStack itemstack : this.enhancements) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public boolean areSlotsEmpty() {
        for (LargeItemStack itemstack : this.slotContents) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void onContentsChanged(int slot) {
        inv.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        BlockPos cratePos = inv.getBlockPos();
        if (this.inv.getLevel().getBlockEntity(cratePos) != this.inv) {
            return false;
        } else {
            return !(player.distanceToSqr((double) cratePos.getX() + 0.5D, (double) cratePos.getY() + 0.5D, (double) cratePos.getZ() + 0.5D) > 64.0D);
        }
    }

    public int getMaxStackSizeForSlot(int slot) {
        if (slot < 0 || slot >= this.slotContents.size()) {
            return 0;
        }

        int baseStackSize = this.getBaseStackSize(slot);
        return baseStackSize + this.getExtraStorage(baseStackSize);
    }

    /**
     * A slot holds a number of stacks of whatever item is in it. An empty slot has no item to size
     * against, and an empty ItemStack answers 1 to getMaxStackSize (the component default), so an
     * empty slot would only accept its stack count in items. Fall back to the normal stack size
     * until the slot knows what it holds.
     */
    public int getBaseStackSize(int slot) {
        int slotBase = this.inv.getLayout().getSlotsBaseStacks()[slot];
        // Not getStackInSlot: a locked slot that has run empty still sizes against its item.
        ItemStack inSlot = this.slotContents.get(slot).getStack();
        int stackSizeMultiplier = inSlot.isEmpty() ? Item.DEFAULT_MAX_STACK_SIZE : inSlot.getMaxStackSize();
        return stackSizeMultiplier * slotBase;
    }

    public int getExtraStorage(int baseStackSize) {
        int extra = 0;
        for (ItemStack stack : this.enhancements) {
            if (stack.getItem() instanceof ICrateUpgrade upgrade) {
                extra += upgrade.getStorageModifier() * baseStackSize;
            }
        }
        return extra;
    }

    public int getStorageModifier() {
        int base = 2;
        int total = base + this.getExtraStorage(base);
        return total / base;
    }

    public void cycleSlotRotation(int slot) {
        this.getLargeItemStack(slot).cycleRotation();
    }

    public ItemStack getLockStack() {
        // Lock is always slot 0
        return this.enhancements.get(0);
    }

    public ItemStack setLockStack(ItemStack stack) {
        // Lock is always slot 0
        return this.enhancements.set(0, stack);
    }

    public boolean hasVoidUpgrade() {
        return this.enhancements.stream().anyMatch(stack -> stack.getItem() == CratesItems.VOID_UPGRADE.get());
    }

    public boolean hasGlowUpgrade() {
        return this.enhancements.stream().anyMatch(stack -> stack.getItem() == CratesItems.GLOW_UPGRADE.get());
    }

    private int getSignalFromPercentage(float percentage) {
        return Math.round(0 + percentage * (15 - 0));
    }

    public int getSignalStrength() {
        ItemStack redstoneUpgrade = this.enhancements.stream().filter(stack -> stack.getItem() == CratesItems.REDSTONE_UPGRADE.get()).findAny().orElse(ItemStack.EMPTY);

        if (redstoneUpgrade == ItemStack.EMPTY) {
            return 0;
        }

        int mode = NBTHelper.getInt(redstoneUpgrade, "Mode");
        if (mode == 4) {
            // 4: Based on all slots
            int runningTotal = 0;
            int maxAllowed = 0;
            for (int i = 0; i < this.slotContents.size(); i++) {
                runningTotal += this.getLargeItemStack(i).getAmount();
                maxAllowed += this.getMaxStackSizeForSlot(i);
            }
            return getSignalFromPercentage((float) runningTotal / maxAllowed);
        } else if (mode == 5) {
            // 5: Based on most full slot
            int maxAmountSlot = IntStream.range(0, this.slotContents.size()).reduce((a, b) -> this.slotContents.get(a).getAmount() < this.slotContents.get(b).getAmount() ? b : a).orElse(-1);
            if (maxAmountSlot == -1) {
                return 0;
            }

            int amount = this.getLargeItemStack(maxAmountSlot).getAmount();
            int maxAllowed = this.getMaxStackSizeForSlot(maxAmountSlot);
            return getSignalFromPercentage((float) amount / maxAllowed);
        } else if (mode == 6) {
            // 6: Based on least full slot
            int minAmountSlot = IntStream.range(0, this.slotContents.size()).reduce((a, b) -> this.slotContents.get(a).getAmount() > this.slotContents.get(b).getAmount() ? b : a).orElse(-1);
            if (minAmountSlot == -1) {
                return 0;
            }

            int amount = this.getLargeItemStack(minAmountSlot).getAmount();
            int maxAllowed = this.getMaxStackSizeForSlot(minAmountSlot);
            return getSignalFromPercentage((float) amount / maxAllowed);
        } else {
            // 0: Based on slot 0
            // 1: Based on slot 1
            // 2: Based on slot 2
            // 3: Based on slot 3
            // Bail early if the slot doesn't exist in this storage crate
            if (mode < 0 || mode >= this.slotContents.size()) {
                return 0;
            }

            int amount = this.getLargeItemStack(mode).getAmount();
            int maxAllowed = this.getMaxStackSizeForSlot(mode);
            return getSignalFromPercentage((float) amount / maxAllowed);
        }
    }

    /**
     * @return -1 if nothing was added, 0 if everything was, otherwise the count left over
     */
    public int addItem(int index, ItemStack stack) {
        if (index >= 0 && index < this.slotContents.size() && !stack.isEmpty()) {
            LargeItemStack largeStack = this.slotContents.get(index);

            if (Services.INVENTORY.canItemStacksStack(stack, largeStack.getStack())) {
                int currentAmount = largeStack.getAmount();
                int amount = Math.min(this.getMaxStackSizeForSlot(index), currentAmount + stack.getCount());
                largeStack.setAmount(amount);
                this.inv.setSlotChanged(index);
                // If too many items were given this will have a positive value otherwise 0
                // If void force that we say all items were added
                return hasVoidUpgrade() ? 0 : stack.getCount() + currentAmount - amount;
            } else if (largeStack.isEmpty()) {
                int amount = Math.min(this.getMaxStackSizeForSlot(index), stack.getCount());
                this.slotContents.set(index, largeStack.withStack(stack.copyWithCount(amount)));
                this.inv.setSlotChanged(index);
                // We tried to initialize this slot
                // If too many items were given this will have a positive value otherwise 0
                // If void force that we say all items were added
                return hasVoidUpgrade() ? 0 : stack.getCount() - amount;
            } else {
                // Stack did not match what was currently in this slot
                return -1;
            }
        }

        // Invalid input
        return -1;
    }

    /**
     * One slot's item on its own: the slot's real count is {@code SlotAmount}, and an item stack
     * refuses to encode a count past 99, so the item goes down as a single item.
     */
    protected static Tag saveItem(ItemStack stack, HolderLookup.Provider registries) {
        return stack.copyWithCount(1).save(registries);
    }

    protected static ItemStack loadItem(CompoundTag tag, String key, HolderLookup.Provider registries) {
        return tag.contains(key, Tag.TAG_COMPOUND) ? ItemStack.parse(registries, tag.getCompound(key)).orElse(ItemStack.EMPTY) : ItemStack.EMPTY;
    }

    @Override
    public void serialize(CompoundTag tag, HolderLookup.Provider registries) {
        // ItemStack#save refuses an empty stack, so a slot with no item is left out entirely and
        // deserialize's fresh empty list covers it. A locked slot still has its item.
        ListTag items = new ListTag();
        for (int i = 0; i < this.slotContents.size(); i++) {
            LargeItemStack stack = this.slotContents.get(i);
            if (stack.getStack().isEmpty()) {
                continue;
            }

            CompoundTag slot = new CompoundTag();
            slot.putByte("Slot", (byte) i);
            slot.putInt("SlotAmount", stack.getAmount());
            slot.putInt("SlotRotation", stack.getRotation());
            slot.putBoolean("SlotLocked", stack.isLocked());
            slot.put("Item", saveItem(stack.getStack(), registries));
            items.add(slot);
        }
        tag.put("Items", items);

        ListTag enhancementItems = new ListTag();
        for (int i = 0; i < this.enhancements.size(); ++i) {
            ItemStack itemstack = this.enhancements.get(i);
            if (!itemstack.isEmpty()) {
                CompoundTag slot = new CompoundTag();
                slot.putByte("Slot", (byte) i);
                slot.put("Item", itemstack.save(registries));
                enhancementItems.add(slot);
            }
        }
        tag.put("Enhancements", enhancementItems);
    }

    @Override
    public void deserialize(CompoundTag tag, HolderLookup.Provider registries) {
        this.slotContents = NonNullList.withSize(this.getSlots(), LargeItemStack.empty());

        ListTag items = tag.getList("Items", Tag.TAG_COMPOUND);
        for (int i = 0; i < items.size(); i++) {
            CompoundTag slot = items.getCompound(i);
            int slotIdx = (slot.contains("Slot", Tag.TAG_BYTE) ? slot.getByte("Slot") : -1) & 255;
            if (slotIdx < this.slotContents.size()) {
                int amount = slot.getInt("SlotAmount");
                int rotation = slot.getInt("SlotRotation");
                boolean locked = slot.getBoolean("SlotLocked");
                ItemStack stack = loadItem(slot, "Item", registries);
                this.slotContents.set(slotIdx, new LargeItemStack(stack, amount, rotation, locked));
            }
        }

        ListTag enhancementItems = tag.getList("Enhancements", Tag.TAG_COMPOUND);
        for (int i = 0; i < enhancementItems.size(); i++) {
            CompoundTag slot = enhancementItems.getCompound(i);
            int slotIdx = (slot.contains("Slot", Tag.TAG_BYTE) ? slot.getByte("Slot") : -1) & 255;
            if (slotIdx < this.enhancements.size()) {
                this.enhancements.set(slotIdx, loadItem(slot, "Item", registries));
            }
        }
    }

    public int getSlot(int slot) {
        int[] slots = this.inv.getLayout().getSlots();
        if (slot < slots.length)
            return slots[slot];
        return -1;
    }
}