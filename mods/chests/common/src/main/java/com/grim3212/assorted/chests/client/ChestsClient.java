package com.grim3212.assorted.chests.client;

import com.grim3212.assorted.lib.client.screen.storage.LockedMaterialScreen;
import com.grim3212.assorted.lib.platform.ClientServices;
import com.grim3212.assorted.chests.client.blockentity.LockedChestBlockEntityRenderer;
import com.grim3212.assorted.chests.client.blockentity.LockedEnderChestBlockEntityRenderer;
import com.grim3212.assorted.chests.client.blockentity.item.ChestsSpecialRenderer;
import com.grim3212.assorted.chests.client.blockentity.item.LockedChestSpecialRenderer;
import com.grim3212.assorted.chests.client.model.ChestModel;
import com.grim3212.assorted.chests.client.model.ChestsModelLayers;
import com.grim3212.assorted.chests.client.screen.LockedEnderChestScreen;
import com.grim3212.assorted.chests.common.block.blockentity.ChestsBlockEntityTypes;
import com.grim3212.assorted.chests.common.inventory.ChestsContainerTypes;

public class ChestsClient {

    public static void init() {
        ClientServices.CLIENT.registerEntityLayer(ChestsModelLayers.LOCKED_CHEST, ChestModel::createBaseMeshDefinition);

        ClientServices.CLIENT.registerScreen(ChestsContainerTypes.LOCKED_ENDER_CHEST::get, LockedEnderChestScreen::new);
        ClientServices.CLIENT.registerScreen(ChestsContainerTypes.LOCKED_CHEST::get, LockedMaterialScreen::new);

        ClientServices.CLIENT.registerBlockEntityRenderer(ChestsBlockEntityTypes.LOCKED_ENDER_CHEST, LockedEnderChestBlockEntityRenderer::new);
        ClientServices.CLIENT.registerBlockEntityRenderer(ChestsBlockEntityTypes.LOCKED_CHEST, LockedChestBlockEntityRenderer::new);

        // BlockEntityWithoutLevelRenderer is gone: a special item renderer is selected by the item's
        // own model json ("minecraft:special" naming one of these ids), so code only registers the id
        // to codec pairs. The generated item models for the ender chest and the chests point at them.
        ClientServices.CLIENT.registerSpecialModelRenderers((register) -> {
            register.registerSpecialModelRenderer(ChestsSpecialRenderer.ID, ChestsSpecialRenderer.Unbaked.MAP_CODEC);
            register.registerSpecialModelRenderer(LockedChestSpecialRenderer.ID, LockedChestSpecialRenderer.Unbaked.MAP_CODEC);
        });
    }
}
