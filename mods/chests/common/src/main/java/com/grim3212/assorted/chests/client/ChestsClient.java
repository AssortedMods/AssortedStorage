package com.grim3212.assorted.chests.client;

import com.grim3212.assorted.chests.client.model.ChestsModelLayers;
import com.grim3212.assorted.chests.client.model.ChestsModels;
import com.grim3212.assorted.chests.common.block.blockentity.ChestsBlockEntityTypes;
import com.grim3212.assorted.chests.common.inventory.ChestsContainerTypes;
import com.grim3212.assorted.lib.client.screen.storage.LockedMaterialScreen;
import com.grim3212.assorted.lib.client.storage.ChestModel;
import com.grim3212.assorted.lib.client.storage.LockedChestRenderer;
import com.grim3212.assorted.lib.client.storage.LockedChestSpecialRenderer;
import com.grim3212.assorted.lib.core.storage.chest.LockedChestBlock;
import com.grim3212.assorted.lib.platform.ClientServices;

public class ChestsClient {

    public static void init() {
        ClientServices.CLIENT.registerEntityLayer(ChestsModelLayers.LOCKED_CHEST, ChestModel::createBaseMeshDefinition);

        ClientServices.CLIENT.registerScreen(ChestsContainerTypes.LOCKED_CHEST::get, LockedMaterialScreen::new);

        ClientServices.CLIENT.registerBlockEntityRenderer(ChestsBlockEntityTypes.LOCKED_CHEST, context -> new LockedChestRenderer<>(context, ChestsModelLayers.LOCKED_CHEST, block -> ChestsModels.CHEST_LOCATIONS.get(((LockedChestBlock) block).getStorageMaterial()), false));

        // The chests' item model jsons name this renderer, so code only registers the id to codec pair.
        ClientServices.CLIENT.registerSpecialModelRenderers((register) -> register.registerSpecialModelRenderer(ChestsModels.ITEM_RENDERER, LockedChestSpecialRenderer.Unbaked.codec(ChestsModelLayers.LOCKED_CHEST)));
    }
}
