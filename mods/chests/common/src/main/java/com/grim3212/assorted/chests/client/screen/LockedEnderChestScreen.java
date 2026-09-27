package com.grim3212.assorted.chests.client.screen;

import com.grim3212.assorted.lib.client.screen.storage.BaseStorageScreen;
import com.grim3212.assorted.lib.core.storage.StorageContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class LockedEnderChestScreen extends BaseStorageScreen<StorageContainer> {

    public LockedEnderChestScreen(StorageContainer container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title, 3);
    }
}
