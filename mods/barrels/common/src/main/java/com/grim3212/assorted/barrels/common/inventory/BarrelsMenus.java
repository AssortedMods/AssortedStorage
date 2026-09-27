package com.grim3212.assorted.barrels.common.inventory;

import com.grim3212.assorted.lib.core.storage.LockedMaterialContainer;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import net.minecraft.world.entity.player.Inventory;

import java.util.Optional;

/** Assorted Barrels' menu on Assorted Lib's {@link LockedMaterialContainer}. */
public class BarrelsMenus {

    public static LockedMaterialContainer createBarrelContainer(int windowId, Inventory playerInventory, Optional<StorageMaterial> material) {
        return LockedMaterialContainer.createClient(BarrelsContainerTypes.LOCKED_BARREL.get(), windowId, playerInventory, material, false);
    }
}
