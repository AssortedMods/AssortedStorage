package com.grim3212.assorted.shulkers.client;

import com.grim3212.assorted.lib.client.screen.storage.LockedMaterialScreen;
import com.grim3212.assorted.lib.platform.ClientServices;
import com.grim3212.assorted.shulkers.client.blockentity.LockedShulkerBoxBlockEntityRenderer;
import com.grim3212.assorted.shulkers.client.blockentity.item.LockedShulkerBoxSpecialRenderer;
import com.grim3212.assorted.shulkers.client.model.ShulkerBoxModel;
import com.grim3212.assorted.shulkers.client.model.ShulkersModelLayers;
import com.grim3212.assorted.shulkers.common.block.blockentity.ShulkersBlockEntityTypes;
import com.grim3212.assorted.shulkers.common.inventory.ShulkersContainerTypes;

public class ShulkersClient {

    public static void init() {
        ClientServices.CLIENT.registerEntityLayer(ShulkersModelLayers.LOCKED_SHULKER_BOX, ShulkerBoxModel::createBaseMeshDefinition);

        ClientServices.CLIENT.registerScreen(ShulkersContainerTypes.LOCKED_SHULKER_BOX::get, LockedMaterialScreen::new);

        ClientServices.CLIENT.registerBlockEntityRenderer(ShulkersBlockEntityTypes.LOCKED_SHULKER_BOX, LockedShulkerBoxBlockEntityRenderer::new);

        // The item model json names this renderer, so code only registers the id to codec pair.
        ClientServices.CLIENT.registerSpecialModelRenderers((register) -> {
            register.registerSpecialModelRenderer(LockedShulkerBoxSpecialRenderer.ID, LockedShulkerBoxSpecialRenderer.Unbaked.MAP_CODEC);
        });
    }
}
