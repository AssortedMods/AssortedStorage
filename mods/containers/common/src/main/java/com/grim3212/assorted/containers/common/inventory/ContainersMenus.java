package com.grim3212.assorted.containers.common.inventory;

import com.grim3212.assorted.lib.core.inventory.IItemStorageHandler;
import com.grim3212.assorted.lib.core.inventory.impl.ItemStackStorageHandler;
import com.grim3212.assorted.lib.core.storage.StorageContainer;
import net.minecraft.world.entity.player.Inventory;

/** This mod's menus on Assorted Lib's {@link StorageContainer}. */
public class ContainersMenus {

    public static StorageContainer createGlassCabinetContainer(int windowId, Inventory playerInventory) {
        return new StorageContainer(ContainersContainerTypes.GLASS_CABINET.get(), windowId, playerInventory, new ItemStackStorageHandler(27));
    }

    public static StorageContainer createGlassCabinetContainer(int windowId, Inventory playerInventory, IItemStorageHandler inventory) {
        return new StorageContainer(ContainersContainerTypes.GLASS_CABINET.get(), windowId, playerInventory, inventory);
    }

    public static StorageContainer createWoodCabinetContainer(int windowId, Inventory playerInventory) {
        return new StorageContainer(ContainersContainerTypes.WOOD_CABINET.get(), windowId, playerInventory, new ItemStackStorageHandler(27));
    }

    public static StorageContainer createWoodCabinetContainer(int windowId, Inventory playerInventory, IItemStorageHandler inventory) {
        return new StorageContainer(ContainersContainerTypes.WOOD_CABINET.get(), windowId, playerInventory, inventory);
    }

    public static StorageContainer createWarehouseCrateContainer(int windowId, Inventory playerInventory) {
        return new StorageContainer(ContainersContainerTypes.WAREHOUSE_CRATE.get(), windowId, playerInventory, new ItemStackStorageHandler(27));
    }

    public static StorageContainer createWarehouseCrateContainer(int windowId, Inventory playerInventory, IItemStorageHandler inventory) {
        return new StorageContainer(ContainersContainerTypes.WAREHOUSE_CRATE.get(), windowId, playerInventory, inventory);
    }

    public static StorageContainer createGoldSafeContainer(int windowId, Inventory playerInventory) {
        return new StorageContainer(ContainersContainerTypes.GOLD_SAFE.get(), windowId, playerInventory, new ItemStackStorageHandler(36));
    }

    public static StorageContainer createGoldSafeContainer(int windowId, Inventory playerInventory, IItemStorageHandler inventory) {
        return new StorageContainer(ContainersContainerTypes.GOLD_SAFE.get(), windowId, playerInventory, inventory);
    }

    public static StorageContainer createObsidianSafeContainer(int windowId, Inventory playerInventory) {
        return new StorageContainer(ContainersContainerTypes.OBSIDIAN_SAFE.get(), windowId, playerInventory, new ItemStackStorageHandler(27));
    }

    public static StorageContainer createObsidianSafeContainer(int windowId, Inventory playerInventory, IItemStorageHandler inventory) {
        return new StorageContainer(ContainersContainerTypes.OBSIDIAN_SAFE.get(), windowId, playerInventory, inventory);
    }
}
