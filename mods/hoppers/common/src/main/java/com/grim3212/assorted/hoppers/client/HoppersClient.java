package com.grim3212.assorted.hoppers.client;

import com.grim3212.assorted.hoppers.Constants;
import com.grim3212.assorted.hoppers.common.inventory.HoppersContainerTypes;
import com.grim3212.assorted.lib.client.screen.storage.LockedHopperScreen;
import com.grim3212.assorted.lib.client.storage.LockedModel;
import com.grim3212.assorted.lib.platform.ClientServices;
import net.minecraft.resources.Identifier;

public class HoppersClient {

    /** The loader of the hopper models, which pick their locked half from the block entity's model data. */
    public static final Identifier LOCKED_MODEL_LOADER = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "locked");

    public static void init() {
        ClientServices.CLIENT.registerModelLoader(LOCKED_MODEL_LOADER, LockedModel.Loader.INSTANCE);

        ClientServices.CLIENT.registerScreen(HoppersContainerTypes.LOCKED_HOPPER::get, LockedHopperScreen::new);
    }
}
