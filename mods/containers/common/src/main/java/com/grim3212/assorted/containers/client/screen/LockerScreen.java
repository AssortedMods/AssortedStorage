package com.grim3212.assorted.containers.client.screen;

import com.grim3212.assorted.lib.client.screen.storage.BaseStorageScreen;
import com.grim3212.assorted.containers.common.inventory.LockerContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class LockerScreen extends BaseStorageScreen<LockerContainer> {

    public LockerScreen(LockerContainer container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title, 5);
    }
}
