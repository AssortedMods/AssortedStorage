package com.grim3212.assorted.locks.client;

import com.grim3212.assorted.lib.client.screen.storage.GenericStorageScreen;
import com.grim3212.assorted.lib.client.screen.storage.LockedHopperScreen;
import com.grim3212.assorted.lib.client.screen.storage.LockedMaterialScreen;
import com.grim3212.assorted.lib.client.storage.ChestModel;
import com.grim3212.assorted.lib.client.storage.LockedChestRenderer;
import com.grim3212.assorted.lib.client.storage.LockedChestSpecialRenderer;
import com.grim3212.assorted.lib.client.storage.LockedModel;
import com.grim3212.assorted.lib.client.storage.LockedShulkerBoxRenderer;
import com.grim3212.assorted.lib.client.storage.LockedShulkerBoxSpecialRenderer;
import com.grim3212.assorted.lib.client.storage.ShulkerBoxModel;
import com.grim3212.assorted.lib.platform.ClientServices;
import com.grim3212.assorted.locks.client.model.LocksStorageModels;
import com.grim3212.assorted.locks.client.screen.KeyRingScreen;
import com.grim3212.assorted.locks.client.screen.LocksmithWorkbenchScreen;
import com.grim3212.assorted.locks.common.block.blockentity.LocksBlockEntityTypes;
import com.grim3212.assorted.locks.common.inventory.LocksContainerTypes;

public class LocksClient {

    public static void init() {
        ClientServices.CLIENT.registerScreen(LocksContainerTypes.LOCKSMITH_WORKBENCH::get, LocksmithWorkbenchScreen::new);
        ClientServices.CLIENT.registerScreen(LocksContainerTypes.KEY_RING::get, KeyRingScreen::new);

        initLockedContainers();
    }

    private static void initLockedContainers() {
        ClientServices.CLIENT.registerEntityLayer(LocksStorageModels.CHEST_LAYER, ChestModel::createBaseMeshDefinition);
        ClientServices.CLIENT.registerEntityLayer(LocksStorageModels.SHULKER_BOX_LAYER, ShulkerBoxModel::createBaseMeshDefinition);

        ClientServices.CLIENT.registerScreen(LocksContainerTypes.LOCKED_ENDER_CHEST::get, GenericStorageScreen::new);
        ClientServices.CLIENT.registerScreen(LocksContainerTypes.LOCKED_CHEST::get, LockedMaterialScreen::new);
        ClientServices.CLIENT.registerScreen(LocksContainerTypes.LOCKED_BARREL::get, LockedMaterialScreen::new);
        ClientServices.CLIENT.registerScreen(LocksContainerTypes.LOCKED_HOPPER::get, LockedHopperScreen::new);
        ClientServices.CLIENT.registerScreen(LocksContainerTypes.LOCKED_SHULKER_BOX::get, LockedMaterialScreen::new);

        ClientServices.CLIENT.registerBlockEntityRenderer(LocksBlockEntityTypes.LOCKED_ENDER_CHEST, context -> new LockedChestRenderer<>(context, LocksStorageModels.CHEST_LAYER, block -> LocksStorageModels.ENDER_CHEST_SPRITE, true));
        ClientServices.CLIENT.registerBlockEntityRenderer(LocksBlockEntityTypes.LOCKED_CHEST, context -> new LockedChestRenderer<>(context, LocksStorageModels.CHEST_LAYER, block -> LocksStorageModels.CHEST_SPRITE, false));
        ClientServices.CLIENT.registerBlockEntityRenderer(LocksBlockEntityTypes.LOCKED_SHULKER_BOX, context -> new LockedShulkerBoxRenderer(context, LocksStorageModels.SHULKER_BOX_LAYER, block -> LocksStorageModels.SHULKER_BOX_SPRITE));

        // The barrel and hopper are block models that pick their locked half from the block entity's model data.
        ClientServices.CLIENT.registerModelLoader(LocksStorageModels.LOCKED_MODEL_LOADER, LockedModel.Loader.INSTANCE);
        ClientServices.CLIENT.registerConditionalItemModelProperty(LocksStorageModels.LOCKED_PROPERTY_ID, LocksStorageModels.LOCKED_PROPERTY.type());

        ClientServices.CLIENT.registerSpecialModelRenderers((register) -> {
            register.registerSpecialModelRenderer(LocksStorageModels.CHEST_ITEM_RENDERER, LockedChestSpecialRenderer.Unbaked.codec(LocksStorageModels.CHEST_LAYER));
            register.registerSpecialModelRenderer(LocksStorageModels.SHULKER_BOX_ITEM_RENDERER, LockedShulkerBoxSpecialRenderer.Unbaked.codec(LocksStorageModels.SHULKER_BOX_LAYER));
        });
    }
}
