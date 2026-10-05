package com.grim3212.assorted.locks;

import com.grim3212.assorted.locks.client.LocksClient;
import net.fabricmc.api.ClientModInitializer;

public class AssortedLocksFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        LocksClient.init();
    }

}
