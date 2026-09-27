package com.grim3212.assorted.storage.common.inventory;

import com.grim3212.assorted.lib.core.inventory.IItemStorageHandler;
import com.grim3212.assorted.lib.core.inventory.impl.ItemStackStorageHandler;
import com.grim3212.assorted.lib.core.storage.LockedMaterialContainer;
import com.grim3212.assorted.lib.core.storage.StorageContainer;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import net.minecraft.world.entity.player.Inventory;

import java.util.Optional;

/** This mod's menus on Assorted Lib's {@link StorageContainer} and {@link LockedMaterialContainer}. */
public class StorageMenus {

    public static StorageContainer createGlassCabinetContainer(int windowId, Inventory playerInventory) {
        return new StorageContainer(StorageContainerTypes.GLASS_CABINET.get(), windowId, playerInventory, new ItemStackStorageHandler(27));
    }

    public static StorageContainer createGlassCabinetContainer(int windowId, Inventory playerInventory, IItemStorageHandler inventory) {
        return new StorageContainer(StorageContainerTypes.GLASS_CABINET.get(), windowId, playerInventory, inventory);
    }

    public static StorageContainer createWoodCabinetContainer(int windowId, Inventory playerInventory) {
        return new StorageContainer(StorageContainerTypes.WOOD_CABINET.get(), windowId, playerInventory, new ItemStackStorageHandler(27));
    }

    public static StorageContainer createWoodCabinetContainer(int windowId, Inventory playerInventory, IItemStorageHandler inventory) {
        return new StorageContainer(StorageContainerTypes.WOOD_CABINET.get(), windowId, playerInventory, inventory);
    }

    public static StorageContainer createWarehouseCrateContainer(int windowId, Inventory playerInventory) {
        return new StorageContainer(StorageContainerTypes.WAREHOUSE_CRATE.get(), windowId, playerInventory, new ItemStackStorageHandler(27));
    }

    public static StorageContainer createWarehouseCrateContainer(int windowId, Inventory playerInventory, IItemStorageHandler inventory) {
        return new StorageContainer(StorageContainerTypes.WAREHOUSE_CRATE.get(), windowId, playerInventory, inventory);
    }

    public static StorageContainer createGoldSafeContainer(int windowId, Inventory playerInventory) {
        return new StorageContainer(StorageContainerTypes.GOLD_SAFE.get(), windowId, playerInventory, new ItemStackStorageHandler(36));
    }

    public static StorageContainer createGoldSafeContainer(int windowId, Inventory playerInventory, IItemStorageHandler inventory) {
        return new StorageContainer(StorageContainerTypes.GOLD_SAFE.get(), windowId, playerInventory, inventory);
    }

    public static StorageContainer createEnderChestContainer(int windowId, Inventory playerInventory) {
        return new StorageContainer(StorageContainerTypes.LOCKED_ENDER_CHEST.get(), windowId, playerInventory, new ItemStackStorageHandler(27));
    }

    public static StorageContainer createEnderChestContainer(int windowId, Inventory playerInventory, IItemStorageHandler inventory) {
        return new StorageContainer(StorageContainerTypes.LOCKED_ENDER_CHEST.get(), windowId, playerInventory, inventory);
    }

    public static StorageContainer createObsidianSafeContainer(int windowId, Inventory playerInventory) {
        return new StorageContainer(StorageContainerTypes.OBSIDIAN_SAFE.get(), windowId, playerInventory, new ItemStackStorageHandler(27));
    }

    public static StorageContainer createObsidianSafeContainer(int windowId, Inventory playerInventory, IItemStorageHandler inventory) {
        return new StorageContainer(StorageContainerTypes.OBSIDIAN_SAFE.get(), windowId, playerInventory, inventory);
    }

    public static LockedMaterialContainer createChestContainer(int windowId, Inventory playerInventory, Optional<StorageMaterial> material) {
        return LockedMaterialContainer.createClient(StorageContainerTypes.LOCKED_CHEST.get(), windowId, playerInventory, material, false);
    }

    public static LockedMaterialContainer createShulkerContainer(int windowId, Inventory playerInventory, Optional<StorageMaterial> material) {
        return LockedMaterialContainer.createClient(StorageContainerTypes.LOCKED_SHULKER_BOX.get(), windowId, playerInventory, material, true);
    }

    public static LockedMaterialContainer createBarrelContainer(int windowId, Inventory playerInventory, Optional<StorageMaterial> material) {
        return LockedMaterialContainer.createClient(StorageContainerTypes.LOCKED_BARREL.get(), windowId, playerInventory, material, false);
    }
}
