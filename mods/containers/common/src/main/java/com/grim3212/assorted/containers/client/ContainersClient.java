package com.grim3212.assorted.containers.client;

import com.grim3212.assorted.containers.client.blockentity.GlassCabinetBlockEntityRenderer;
import com.grim3212.assorted.containers.client.blockentity.GoldSafeBlockEntityRenderer;
import com.grim3212.assorted.containers.client.blockentity.ItemTowerBlockEntityRenderer;
import com.grim3212.assorted.containers.client.blockentity.LockerBlockEntityRenderer;
import com.grim3212.assorted.containers.client.blockentity.ObsidianSafeBlockEntityRenderer;
import com.grim3212.assorted.containers.client.blockentity.WarehouseCrateBlockEntityRenderer;
import com.grim3212.assorted.containers.client.blockentity.WoodCabinetBlockEntityRenderer;
import com.grim3212.assorted.containers.client.blockentity.item.ContainersSpecialRenderer;
import com.grim3212.assorted.containers.client.blockentity.item.ItemTowerSpecialRenderer;
import com.grim3212.assorted.containers.client.model.CabinetModel;
import com.grim3212.assorted.containers.client.model.ContainersModelLayers;
import com.grim3212.assorted.containers.client.model.DualLockerModel;
import com.grim3212.assorted.containers.client.model.ItemTowerModel;
import com.grim3212.assorted.containers.client.model.LockerModel;
import com.grim3212.assorted.containers.client.model.SafeModel;
import com.grim3212.assorted.containers.client.model.WarehouseCrateModel;
import com.grim3212.assorted.containers.client.screen.DualLockerScreen;
import com.grim3212.assorted.containers.client.screen.GoldSafeScreen;
import com.grim3212.assorted.containers.client.screen.ItemTowerScreen;
import com.grim3212.assorted.containers.client.screen.LockerScreen;
import com.grim3212.assorted.containers.common.block.blockentity.ContainersBlockEntityTypes;
import com.grim3212.assorted.containers.common.inventory.ContainersContainerTypes;
import com.grim3212.assorted.lib.client.screen.storage.GenericStorageScreen;
import com.grim3212.assorted.lib.platform.ClientServices;

public class ContainersClient {

    public static void init() {
        ClientServices.CLIENT.registerEntityLayer(ContainersModelLayers.CABINET, () -> CabinetModel.createBaseMeshDefinition(false));
        ClientServices.CLIENT.registerEntityLayer(ContainersModelLayers.GLASS_CABINET, () -> CabinetModel.createBaseMeshDefinition(true));
        ClientServices.CLIENT.registerEntityLayer(ContainersModelLayers.LOCKER, LockerModel::createBaseMeshDefinition);
        ClientServices.CLIENT.registerEntityLayer(ContainersModelLayers.DUAL_LOCKER, DualLockerModel::createBaseMeshDefinition);
        ClientServices.CLIENT.registerEntityLayer(ContainersModelLayers.SAFE, SafeModel::createBaseMeshDefinition);
        ClientServices.CLIENT.registerEntityLayer(ContainersModelLayers.WAREHOUSE_CRATE, WarehouseCrateModel::createBaseMeshDefinition);
        ClientServices.CLIENT.registerEntityLayer(ContainersModelLayers.ITEM_TOWER, ItemTowerModel::createBaseMeshDefinition);

        ClientServices.CLIENT.registerScreen(ContainersContainerTypes.WOOD_CABINET::get, GenericStorageScreen::new);
        ClientServices.CLIENT.registerScreen(ContainersContainerTypes.GLASS_CABINET::get, GenericStorageScreen::new);
        ClientServices.CLIENT.registerScreen(ContainersContainerTypes.WAREHOUSE_CRATE::get, GenericStorageScreen::new);
        ClientServices.CLIENT.registerScreen(ContainersContainerTypes.GOLD_SAFE::get, GoldSafeScreen::new);
        ClientServices.CLIENT.registerScreen(ContainersContainerTypes.OBSIDIAN_SAFE::get, GenericStorageScreen::new);
        ClientServices.CLIENT.registerScreen(ContainersContainerTypes.LOCKER::get, LockerScreen::new);
        ClientServices.CLIENT.registerScreen(ContainersContainerTypes.DUAL_LOCKER::get, DualLockerScreen::new);
        ClientServices.CLIENT.registerScreen(ContainersContainerTypes.ITEM_TOWER::get, ItemTowerScreen::new);

        ClientServices.CLIENT.registerBlockEntityRenderer(ContainersBlockEntityTypes.WOOD_CABINET, WoodCabinetBlockEntityRenderer::new);
        ClientServices.CLIENT.registerBlockEntityRenderer(ContainersBlockEntityTypes.GLASS_CABINET, GlassCabinetBlockEntityRenderer::new);
        ClientServices.CLIENT.registerBlockEntityRenderer(ContainersBlockEntityTypes.WAREHOUSE_CRATE, WarehouseCrateBlockEntityRenderer::new);
        ClientServices.CLIENT.registerBlockEntityRenderer(ContainersBlockEntityTypes.GOLD_SAFE, GoldSafeBlockEntityRenderer::new);
        ClientServices.CLIENT.registerBlockEntityRenderer(ContainersBlockEntityTypes.OBSIDIAN_SAFE, ObsidianSafeBlockEntityRenderer::new);
        ClientServices.CLIENT.registerBlockEntityRenderer(ContainersBlockEntityTypes.LOCKER, LockerBlockEntityRenderer::new);
        ClientServices.CLIENT.registerBlockEntityRenderer(ContainersBlockEntityTypes.ITEM_TOWER, ItemTowerBlockEntityRenderer::new);

        // BlockEntityWithoutLevelRenderer is gone: a special item renderer is selected by the item's
        // own model json ("minecraft:special" naming one of these ids), so code only registers the id
        // to codec pairs. The generated item models for every block here point at them.
        ClientServices.CLIENT.registerSpecialModelRenderers((register) -> {
            register.registerSpecialModelRenderer(ContainersSpecialRenderer.ID, ContainersSpecialRenderer.Unbaked.MAP_CODEC);
            register.registerSpecialModelRenderer(ItemTowerSpecialRenderer.ID, ItemTowerSpecialRenderer.Unbaked.MAP_CODEC);
        });
    }
}
