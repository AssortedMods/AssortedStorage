package com.grim3212.assorted.hoppers;

import com.grim3212.assorted.hoppers.client.HoppersClient;
import net.fabricmc.api.ClientModInitializer;

public class AssortedHoppersFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HoppersClient.init();
    }

}
