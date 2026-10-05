package com.grim3212.assorted.bags;

import com.grim3212.assorted.bags.client.BagsClient;
import net.fabricmc.api.ClientModInitializer;

public class AssortedBagsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BagsClient.init();
    }

}
