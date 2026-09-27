package com.grim3212.assorted.containers;

import com.grim3212.assorted.containers.client.ContainersClient;
import net.fabricmc.api.ClientModInitializer;

public class AssortedContainersFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ContainersClient.init();
    }

}
