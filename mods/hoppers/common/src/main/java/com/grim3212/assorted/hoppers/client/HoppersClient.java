package com.grim3212.assorted.hoppers.client;

import com.grim3212.assorted.hoppers.Constants;
import com.grim3212.assorted.hoppers.common.block.HoppersBlocks;
import com.grim3212.assorted.hoppers.common.inventory.HoppersContainerTypes;
import com.grim3212.assorted.lib.client.screen.storage.LockedHopperScreen;
import com.grim3212.assorted.lib.client.storage.LockedModel;
import com.grim3212.assorted.lib.platform.ClientServices;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class HoppersClient {

    /** The loader of the hopper models, which pick their locked half from the block entity's model data. */
    public static final ResourceLocation LOCKED_MODEL_LOADER = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "locked");

    public static void init() {
        ClientServices.CLIENT.registerModelLoader(LOCKED_MODEL_LOADER, LockedModel.Loader.INSTANCE);

        ClientServices.CLIENT.registerScreen(HoppersContainerTypes.LOCKED_HOPPER::get, LockedHopperScreen::new);

        // The rim has see-through pixels. NeoForge reads the layer from the models; Fabric needs it named here.
        ClientServices.CLIENT.registerBlockRenderType(RenderType.cutoutMipped(), () -> HoppersBlocks.HOPPERS.values().stream().<Block>map(IRegistryObject::get).toList());
    }
}
