package com.grim3212.assorted.locks.client;

import com.grim3212.assorted.lib.platform.ClientServices;
import com.grim3212.assorted.locks.client.screen.KeyRingScreen;
import com.grim3212.assorted.locks.client.screen.LocksmithWorkbenchScreen;
import com.grim3212.assorted.locks.common.inventory.LocksContainerTypes;

public class LocksClient {

    public static void init() {
        ClientServices.CLIENT.registerScreen(LocksContainerTypes.LOCKSMITH_WORKBENCH::get, LocksmithWorkbenchScreen::new);
        ClientServices.CLIENT.registerScreen(LocksContainerTypes.KEY_RING::get, KeyRingScreen::new);
    }
}
