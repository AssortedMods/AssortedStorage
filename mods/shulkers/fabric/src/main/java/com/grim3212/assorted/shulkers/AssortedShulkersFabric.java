package com.grim3212.assorted.shulkers;

import com.grim3212.assorted.lib.core.inventory.IInventoryBlockEntity;
import com.grim3212.assorted.lib.inventory.FabricPlatformInventoryStorageHandlerUnsided;
import com.grim3212.assorted.shulkers.common.block.ShulkersBlocks;
import com.grim3212.assorted.shulkers.common.block.blockentity.ShulkersBlockEntityTypes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;

public class AssortedShulkersFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ShulkersCommonMod.init();

        ShulkersBlocks.initDispenserHandlers();

        ItemStorage.SIDED.registerForBlockEntities((be, direction) ->
                {
                    if (be instanceof IInventoryBlockEntity inv)
                        return ((FabricPlatformInventoryStorageHandlerUnsided) inv.getStorageHandler()).getFabricInventory();
                    return null;
                },
                ShulkersBlockEntityTypes.LOCKED_SHULKER_BOX.get()
        );
    }
}
