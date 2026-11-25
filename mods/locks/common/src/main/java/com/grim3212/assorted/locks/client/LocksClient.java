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
import com.grim3212.assorted.locks.common.block.LocksBlocks;
import com.grim3212.assorted.locks.common.block.blockentity.LocksBlockEntityTypes;
import com.grim3212.assorted.locks.common.inventory.LocksContainerTypes;
import net.minecraft.client.renderer.RenderType;

import java.util.List;

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
        // The barrel item has no block entity for that model data, so its item model's overrides branch on the stack's lock instead.
        ClientServices.CLIENT.registerItemProperty(() -> LocksBlocks.LOCKED_BARREL.get().asItem(), LocksStorageModels.LOCKED_PROPERTY_ID, LocksStorageModels.LOCKED_PROPERTY);

        // The chests and the shulker box are builtin/entity items, each drawn by its own renderer with the sprite its block entity renderer uses.
        ClientServices.CLIENT.registerSpecialModelRenderers((register) -> {
            register.registerItemRenderer(() -> LocksBlocks.LOCKED_CHEST.get().asItem(), () -> new LockedChestSpecialRenderer(LocksStorageModels.CHEST_SPRITE, LocksStorageModels.CHEST_LAYER));
            register.registerItemRenderer(() -> LocksBlocks.LOCKED_ENDER_CHEST.get().asItem(), () -> new LockedChestSpecialRenderer(LocksStorageModels.ENDER_CHEST_SPRITE, LocksStorageModels.CHEST_LAYER));
            register.registerItemRenderer(() -> LocksBlocks.LOCKED_SHULKER_BOX.get().asItem(), () -> new LockedShulkerBoxSpecialRenderer(LocksStorageModels.SHULKER_BOX_SPRITE, LocksStorageModels.SHULKER_BOX_LAYER));
        });

        // 1.21.1 picks a block's chunk layer from code, not its textures; NeoForge reads the same from the models' render_type.
        ClientServices.CLIENT.registerBlockRenderType(RenderType.cutout(), () -> List.of(LocksBlocks.lockedDoors()));
        ClientServices.CLIENT.registerBlockRenderType(RenderType.cutoutMipped(), () -> List.of(LocksBlocks.LOCKED_HOPPER.get()));
    }
}
