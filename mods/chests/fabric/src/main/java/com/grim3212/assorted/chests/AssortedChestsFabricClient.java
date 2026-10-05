package com.grim3212.assorted.chests;

import com.grim3212.assorted.chests.client.ChestsClient;
import net.fabricmc.api.ClientModInitializer;

public class AssortedChestsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ChestsClient.init();
    }

}
