package com.grim3212.assorted.chests;

import com.grim3212.assorted.lib.core.inventory.IInventoryBlockEntity;
import com.grim3212.assorted.lib.inventory.FabricPlatformInventoryStorageHandlerUnsided;
import com.grim3212.assorted.chests.common.block.blockentity.ChestsBlockEntityTypes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;

public class AssortedChestsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ChestsCommonMod.init();

        ItemStorage.SIDED.registerForBlockEntities((be, direction) ->
                {
                    if (be instanceof IInventoryBlockEntity inv)
                        return ((FabricPlatformInventoryStorageHandlerUnsided) inv.getStorageHandler()).getFabricInventory();
                    return null;
                },
                ChestsBlockEntityTypes.LOCKED_CHEST.get()
        );
    }
}
