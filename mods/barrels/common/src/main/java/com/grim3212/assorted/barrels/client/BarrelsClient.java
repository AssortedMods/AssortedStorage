package com.grim3212.assorted.barrels.client;

import com.grim3212.assorted.lib.client.screen.storage.LockedMaterialScreen;
import com.grim3212.assorted.lib.platform.ClientServices;
import com.grim3212.assorted.barrels.client.model.baked.LockedModel;
import com.grim3212.assorted.barrels.client.properties.HasStorageTagProperty;
import com.grim3212.assorted.barrels.common.inventory.BarrelsContainerTypes;

public class BarrelsClient {

    public static void init() {
        ClientServices.CLIENT.registerModelLoader(LockedModel.LOADER_NAME, LockedModel.Loader.INSTANCE);

        ClientServices.CLIENT.registerScreen(BarrelsContainerTypes.LOCKED_BARREL::get, LockedMaterialScreen::new);

        for (HasStorageTagProperty property : HasStorageTagProperty.values()) {
            ClientServices.CLIENT.registerConditionalItemModelProperty(property.id(), property.type());
        }
    }
}
