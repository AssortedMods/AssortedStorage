package com.grim3212.assorted.barrels;

import com.grim3212.assorted.barrels.client.BarrelsClient;
import net.fabricmc.api.ClientModInitializer;

public class AssortedBarrelsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BarrelsClient.init();
    }

}
