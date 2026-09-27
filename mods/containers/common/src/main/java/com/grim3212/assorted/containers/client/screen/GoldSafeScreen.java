package com.grim3212.assorted.containers.client.screen;

import com.grim3212.assorted.lib.client.screen.storage.BaseStorageScreen;
import com.grim3212.assorted.lib.core.storage.StorageContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GoldSafeScreen extends BaseStorageScreen<StorageContainer> {

    public GoldSafeScreen(StorageContainer container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title, 4);
    }
}
