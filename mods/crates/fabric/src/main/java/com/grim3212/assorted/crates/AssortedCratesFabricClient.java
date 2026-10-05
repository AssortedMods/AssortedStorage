package com.grim3212.assorted.crates;

import com.grim3212.assorted.crates.client.CratesClient;
import net.fabricmc.api.ClientModInitializer;

public class AssortedCratesFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CratesClient.init();
    }

}
