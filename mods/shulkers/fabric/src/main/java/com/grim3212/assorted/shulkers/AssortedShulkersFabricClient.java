package com.grim3212.assorted.shulkers;

import com.grim3212.assorted.shulkers.client.ShulkersClient;
import net.fabricmc.api.ClientModInitializer;

public class AssortedShulkersFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ShulkersClient.init();
    }

}
