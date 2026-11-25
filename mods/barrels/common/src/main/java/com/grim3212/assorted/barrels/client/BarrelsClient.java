package com.grim3212.assorted.barrels.client;

import com.grim3212.assorted.barrels.Constants;
import com.grim3212.assorted.barrels.common.block.BarrelsBlocks;
import com.grim3212.assorted.barrels.common.inventory.BarrelsContainerTypes;
import com.grim3212.assorted.lib.client.screen.storage.LockedMaterialScreen;
import com.grim3212.assorted.lib.client.storage.LockedItemProperty;
import com.grim3212.assorted.lib.client.storage.LockedModel;
import com.grim3212.assorted.lib.platform.ClientServices;
import net.minecraft.resources.ResourceLocation;

public class BarrelsClient {

    /** The loader of the closed barrel models, which pick their locked half from the block entity's model data. */
    public static final ResourceLocation LOCKED_MODEL_LOADER = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "locked");
    /** What a barrel's item model's {@code overrides} branch on, as an item has no block entity to read the lock from. */
    public static final ResourceLocation LOCKED_PROPERTY_ID = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "locked");
    public static final LockedItemProperty LOCKED_PROPERTY = new LockedItemProperty();

    public static void init() {
        ClientServices.CLIENT.registerModelLoader(LOCKED_MODEL_LOADER, LockedModel.Loader.INSTANCE);

        ClientServices.CLIENT.registerScreen(BarrelsContainerTypes.LOCKED_BARREL::get, LockedMaterialScreen::new);

        BarrelsBlocks.BARRELS.values().forEach(barrel -> ClientServices.CLIENT.registerItemProperty(() -> barrel.get().asItem(), LOCKED_PROPERTY_ID, LOCKED_PROPERTY));
    }
}
