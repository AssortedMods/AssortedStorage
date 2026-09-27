package com.grim3212.assorted.shulkers.common.inventory;

import com.grim3212.assorted.lib.core.storage.LockedMaterialContainer;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import net.minecraft.world.entity.player.Inventory;

import java.util.Optional;

/** The shulker box menu on Assorted Lib's {@link LockedMaterialContainer}. */
public class ShulkersMenus {

    public static LockedMaterialContainer createShulkerContainer(int windowId, Inventory playerInventory, Optional<StorageMaterial> material) {
        return LockedMaterialContainer.createClient(ShulkersContainerTypes.LOCKED_SHULKER_BOX.get(), windowId, playerInventory, material, true);
    }
}
