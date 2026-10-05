package com.grim3212.assorted.crates.client;

import com.grim3212.assorted.lib.platform.ClientServices;
import com.grim3212.assorted.crates.client.blockentity.CrateBlockEntityRenderer;
import com.grim3212.assorted.crates.client.model.baked.LockedModel;
import com.grim3212.assorted.crates.client.screen.CrateCompactingScreen;
import com.grim3212.assorted.crates.client.screen.CrateScreen;
import com.grim3212.assorted.crates.common.block.blockentity.CratesBlockEntityTypes;
import com.grim3212.assorted.crates.common.inventory.CratesContainerTypes;
import com.grim3212.assorted.crates.config.CratesClientConfig;

public class CratesClient {

    public static final CratesClientConfig CLIENT_CONFIG = new CratesClientConfig();

    public static void init() {
        ClientServices.CLIENT.registerModelLoader(LockedModel.LOADER_NAME, LockedModel.Loader.INSTANCE);

        ClientServices.CLIENT.registerScreen(CratesContainerTypes.CRATE::get, CrateScreen::new);
        ClientServices.CLIENT.registerScreen(CratesContainerTypes.CRATE_COMPACTING::get, CrateCompactingScreen::new);

        ClientServices.CLIENT.registerBlockEntityRenderer(CratesBlockEntityTypes.CRATE, CrateBlockEntityRenderer::new);
        ClientServices.CLIENT.registerBlockEntityRenderer(CratesBlockEntityTypes.CRATE_COMPACTING, CrateBlockEntityRenderer::new);
    }
}
