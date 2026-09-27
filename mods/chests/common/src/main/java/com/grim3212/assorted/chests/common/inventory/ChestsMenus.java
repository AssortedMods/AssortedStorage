package com.grim3212.assorted.chests.common.inventory;

import com.grim3212.assorted.lib.core.inventory.IItemStorageHandler;
import com.grim3212.assorted.lib.core.inventory.impl.ItemStackStorageHandler;
import com.grim3212.assorted.lib.core.storage.LockedMaterialContainer;
import com.grim3212.assorted.lib.core.storage.StorageContainer;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import net.minecraft.world.entity.player.Inventory;

import java.util.Optional;

/** Assorted Chests' menus on Assorted Lib's {@link StorageContainer} and {@link LockedMaterialContainer}. */
public class ChestsMenus {

    public static StorageContainer createEnderChestContainer(int windowId, Inventory playerInventory) {
        return new StorageContainer(ChestsContainerTypes.LOCKED_ENDER_CHEST.get(), windowId, playerInventory, new ItemStackStorageHandler(27));
    }

    public static StorageContainer createEnderChestContainer(int windowId, Inventory playerInventory, IItemStorageHandler inventory) {
        return new StorageContainer(ChestsContainerTypes.LOCKED_ENDER_CHEST.get(), windowId, playerInventory, inventory);
    }

    public static LockedMaterialContainer createChestContainer(int windowId, Inventory playerInventory, Optional<StorageMaterial> material) {
        return LockedMaterialContainer.createClient(ChestsContainerTypes.LOCKED_CHEST.get(), windowId, playerInventory, material, false);
    }
}
