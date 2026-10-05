package com.grim3212.assorted.barrels.client;

import com.grim3212.assorted.barrels.Constants;
import com.grim3212.assorted.barrels.common.inventory.BarrelsContainerTypes;
import com.grim3212.assorted.lib.client.screen.storage.LockedMaterialScreen;
import com.grim3212.assorted.lib.client.storage.LockedItemProperty;
import com.grim3212.assorted.lib.client.storage.LockedModel;
import com.grim3212.assorted.lib.platform.ClientServices;
import net.minecraft.resources.Identifier;

public class BarrelsClient {

    /** The loader of the closed barrel models, which pick their locked half from the block entity's model data. */
    public static final Identifier LOCKED_MODEL_LOADER = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "locked");
    /** What a barrel's item model branches on, as an item has no block entity to read the lock from. */
    public static final Identifier LOCKED_PROPERTY_ID = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "locked");
    public static final LockedItemProperty LOCKED_PROPERTY = new LockedItemProperty();

    public static void init() {
        ClientServices.CLIENT.registerModelLoader(LOCKED_MODEL_LOADER, LockedModel.Loader.INSTANCE);

        ClientServices.CLIENT.registerScreen(BarrelsContainerTypes.LOCKED_BARREL::get, LockedMaterialScreen::new);

        ClientServices.CLIENT.registerConditionalItemModelProperty(LOCKED_PROPERTY_ID, LOCKED_PROPERTY.type());
    }
}
