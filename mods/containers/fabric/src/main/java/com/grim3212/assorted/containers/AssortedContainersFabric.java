package com.grim3212.assorted.containers;

import com.grim3212.assorted.containers.common.block.blockentity.ContainersBlockEntityTypes;
import com.grim3212.assorted.lib.core.inventory.IInventoryBlockEntity;
import com.grim3212.assorted.lib.inventory.FabricPlatformInventoryStorageHandlerUnsided;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;

public class AssortedContainersFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ContainersCommonMod.init();

        ItemStorage.SIDED.registerForBlockEntities((be, direction) ->
                {
                    if (be instanceof IInventoryBlockEntity inv)
                        return ((FabricPlatformInventoryStorageHandlerUnsided) inv.getStorageHandler()).getFabricInventory();
                    return null;
                },
                ContainersBlockEntityTypes.WOOD_CABINET.get(),
                ContainersBlockEntityTypes.GLASS_CABINET.get(),
                ContainersBlockEntityTypes.GOLD_SAFE.get(),
                ContainersBlockEntityTypes.OBSIDIAN_SAFE.get(),
                ContainersBlockEntityTypes.LOCKER.get(),
                ContainersBlockEntityTypes.ITEM_TOWER.get(),
                ContainersBlockEntityTypes.WAREHOUSE_CRATE.get()
        );
    }
}
