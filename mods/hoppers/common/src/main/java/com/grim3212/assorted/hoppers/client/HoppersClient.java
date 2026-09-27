package com.grim3212.assorted.hoppers.client;

import com.grim3212.assorted.hoppers.client.model.baked.LockedModel;
import com.grim3212.assorted.hoppers.client.screen.LockedHopperScreen;
import com.grim3212.assorted.hoppers.common.inventory.HoppersContainerTypes;
import com.grim3212.assorted.lib.platform.ClientServices;

public class HoppersClient {

    public static void init() {
        ClientServices.CLIENT.registerModelLoader(LockedModel.LOADER_NAME, LockedModel.Loader.INSTANCE);

        ClientServices.CLIENT.registerScreen(HoppersContainerTypes.LOCKED_HOPPER::get, LockedHopperScreen::new);
    }
}
