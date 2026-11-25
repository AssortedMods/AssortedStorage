package com.grim3212.assorted.crates.common.block.blockentity;

import com.grim3212.assorted.lib.core.inventory.locking.LockItems;
import com.grim3212.assorted.lib.core.inventory.IInventoryBlockEntity;
import com.grim3212.assorted.lib.core.inventory.INamed;
import com.grim3212.assorted.lib.core.inventory.IPlatformInventoryStorageHandler;
import com.grim3212.assorted.lib.core.inventory.locking.ILockable;
import com.grim3212.assorted.lib.core.inventory.locking.StorageUtil;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.crates.Constants;
import com.grim3212.assorted.crates.api.crates.CrateLayout;
import com.grim3212.assorted.crates.common.block.CrateBlock;
import com.grim3212.assorted.crates.common.inventory.CratesContainerTypes;
import com.grim3212.assorted.crates.common.inventory.crates.CrateContainer;
import com.grim3212.assorted.crates.common.inventory.crates.CrateSidedInv;
import com.grim3212.assorted.crates.common.item.CratesItems;
import com.grim3212.assorted.crates.common.network.SyncCrate;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.grim3212.assorted.lib.core.inventory.IMenuDataProvider;
import net.minecraft.server.level.ServerPlayer;

public class CrateBlockEntity extends BlockEntity implements IMenuDataProvider<BlockPos>, INamed, ILockable, IInventoryBlockEntity {

    private final CrateLayout layout;
    private Component customName;
    private UUID playerTimerUUID;
    private long playerTimerMillis;
    private ItemStack playerTimerStack = ItemStack.EMPTY;
    private IPlatformInventoryStorageHandler handler;
    protected CrateSidedInv storageHandler;

    public CrateBlockEntity(BlockPos pos, BlockState state) {
        this(CratesBlockEntityTypes.CRATE.get(), pos, state);
    }

    public CrateBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        if (state.getBlock() instanceof CrateBlock storageCrate) {
            layout = storageCrate.getLayout();
        } else {
            this.layout = CrateLayout.SINGLE;
        }

        this.storageHandler = new CrateSidedInv(this);
    }

    @Override
    public IPlatformInventoryStorageHandler getStorageHandler() {
        if (this.handler == null) {
            this.handler = this.createStorageHandler();
        }

        return this.handler;
    }

    private IPlatformInventoryStorageHandler createStorageHandler() {
        return Services.INVENTORY.createStorageInventoryHandler(this.storageHandler);
    }

    public CrateSidedInv getItemStackStorageHandler() {
        return this.storageHandler;
    }

    public CrateLayout getLayout() {
        return layout;
    }

    @Override
    public boolean isLocked() {
        return StorageUtil.hasCode(this.getItemStackStorageHandler().getLockStack());
    }

    @Override
    public String getLockCode() {
        return StorageUtil.getCode(this.getItemStackStorageHandler().getLockStack());
    }

    @Override
    public void setLockCode(String s) {
        if (s != null && !s.isEmpty()) {
            this.getItemStackStorageHandler().setLockStack(LockItems.createLock(s));

            this.setChanged();
            this.modelUpdate();
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        // The pre-"Inventory" flat layout is no longer read: nothing needs to interoperate with a 1.20.1 world.
        if (tag.contains("Inventory", Tag.TAG_COMPOUND)) {
            this.getItemStackStorageHandler().deserialize(tag.getCompound("Inventory"), registries);
        }

        this.customName = tag.contains("CustomName", Tag.TAG_STRING) ? parseCustomNameSafe(tag.getString("CustomName"), registries) : null;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        // Save all the items
        CompoundTag inventory = new CompoundTag();
        this.getItemStackStorageHandler().serialize(inventory, registries);
        tag.put("Inventory", inventory);

        if (this.customName != null) {
            tag.putString("CustomName", Component.Serializer.toJson(this.customName, registries));
        }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    /**
     * Drops the contents and the upgrades. Block entities have no removal hook here, so the block
     * calls this from its {@code onRemove} while the block entity still exists, and only when the
     * block itself changes.
     */
    public void onBlockRemoved(Level level, BlockPos pos) {
        this.dropContents(level, pos);
        Containers.dropContents(level, pos, this.getItemStackStorageHandler().getEnhancements());
    }

    protected void dropContents(Level level, BlockPos pos) {
        for (int slot = 0; slot < this.getItemStackStorageHandler().getSlots(); slot++) {
            Containers.dropContents(level, pos, this.getItemStackStorageHandler().getLargeItemStack(slot).asItemStacks());
        }
    }

    /**
     * Re-renders the crate. On a server the block update is also what puts the crate's new block
     * entity data - its lock and its upgrades - on the wire for every player who can see it.
     */
    public void modelUpdate() {
        if (this.level != null) {
            this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    @Override
    public void setCustomName(Component name) {
        this.customName = name;
    }

    @Override
    public Component getName() {
        return this.customName != null ? this.customName : this.getDefaultName();
    }

    @Override
    public Component getDisplayName() {
        return this.getName();
    }

    @Override
    @Nullable
    public Component getCustomName() {
        return this.customName;
    }

    protected Component getDefaultName() {
        return Component.translatable(Constants.MOD_ID + ".container.storage_crate");
    }


    public void setSlotChanged(int slot) {
        this.setChanged();
        this.level.updateNeighborsAt(getBlockPos(), getBlockState().getBlock());

        if (this.level != null && !this.level.isClientSide()) {
            Services.NETWORK.sendToNearby(level, getBlockPos(), new SyncCrate(getBlockPos(), slot, this.getItemStackStorageHandler().getLargeItemStack(slot)));
        }
    }


    /** Left-click drops items from the slot hit: a whole stack when sneaking, otherwise one. */
    public boolean attack(Player player, BlockHitResult hit) {
        int hitSlot = getHitSlot(hit);
        if (hitSlot < 0)
            return false;

        int maxAmount = player.isShiftKeyDown() ? 64 : 1;

        ItemStack stack = this.getItemStackStorageHandler().removeItem(hitSlot, maxAmount);
        if (!stack.isEmpty()) {
            if (!player.getInventory().add(stack)) {
                Containers.dropItemStack(level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), stack);
            }
            return true;
        }
        return !stack.isEmpty();
    }

    /**
     * Right-click: a rotate item rotates the hit slot; otherwise the first use inserts the held
     * item and a second inserts all of that item from the inventory.
     */
    public ItemInteractionResult use(Player player, InteractionHand handIn, BlockHitResult hit) {
        int hitSlot = getHitSlot(hit);

        if (hitSlot < 0) {
            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }

        ItemStack playerStack = player.getItemInHand(handIn).copyWithCount(1);

        if (playerStack.getItem() == CratesItems.ROTATOR_MAJIG.get()) {
            this.getItemStackStorageHandler().cycleSlotRotation(hitSlot);
            this.setSlotChanged(hitSlot);
            return ItemInteractionResult.SUCCESS;
        }

        if (player.getUUID().equals(playerTimerUUID) && Util.getMillis() - playerTimerMillis < 275 && playerTimerStack != ItemStack.EMPTY) {
            List<Integer> slots = findMatchingStacks(player, playerTimerStack);
            for (int slot : slots) {
                ItemStack slotItem = player.getInventory().getItem(slot).copy();
                int addRet = this.getItemStackStorageHandler().addItem(hitSlot, slotItem);
                if (addRet < 0) {
                    // We can't add anymore to this storage crate
                    break;
                } else if (addRet == 0) {
                    player.getInventory().setItem(slot, ItemStack.EMPTY);
                } else {
                    player.getInventory().getItem(slot).shrink(slotItem.getCount() - addRet);
                }
            }

            playerTimerUUID = null;
            playerTimerMillis = 0;
            playerTimerStack = ItemStack.EMPTY;
            return ItemInteractionResult.SUCCESS;
        }

        ItemStack playerItem = player.getItemInHand(handIn).copy();
        int addRet = this.getItemStackStorageHandler().addItem(hitSlot, playerItem);
        if (addRet < 0) {
            // We can't add anymore to this storage crate
            return ItemInteractionResult.SUCCESS;
        } else if (addRet == 0) {
            player.getItemInHand(handIn).shrink(playerItem.getCount());

            playerTimerUUID = player.getUUID();
            playerTimerMillis = Util.getMillis();
            playerTimerStack = playerStack;
        } else {
            player.getItemInHand(handIn).shrink(playerItem.getCount() - addRet);
        }

        return ItemInteractionResult.SUCCESS;
    }

    public static List<Integer> findMatchingStacks(Player player, ItemStack stack) {
        List<Integer> slots = new ArrayList<>();

        for (int slot = 0; slot < player.getInventory().getContainerSize(); ++slot) {
            ItemStack itemstack = player.getInventory().getItem(slot);

            if (!itemstack.isEmpty()) {
                if (Services.INVENTORY.canItemStacksStack(stack, itemstack)) {
                    slots.add(slot);
                }
            }
        }

        return slots;
    }

    private int getHitSlot(BlockHitResult hit) {
        if (hit.getDirection() != this.getBlockState().getValue(CrateBlock.FACING)) {
            return -1;
        }

        return this.layout.getHitSlot(hit);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (this.handler != null) {
            this.handler.invalidate();
        }
    }

    @Override
    public BlockPos getMenuData(ServerPlayer player) {
        return this.worldPosition;
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory player, Player playerEntity) {
        return new CrateContainer(CratesContainerTypes.CRATE.get(), windowId, player, this);
    }
}
