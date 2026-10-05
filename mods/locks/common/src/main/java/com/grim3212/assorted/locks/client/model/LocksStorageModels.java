package com.grim3212.assorted.locks.client.model;

import com.grim3212.assorted.lib.client.storage.LockedItemProperty;
import com.grim3212.assorted.locks.Constants;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;

/** The ids the locked containers are drawn with, which the client registers and the model datagen names. */
public final class LocksStorageModels {

    public static final ModelLayerLocation CHEST_LAYER = new ModelLayerLocation(id("locked_chest"), "main");
    public static final ModelLayerLocation SHULKER_BOX_LAYER = new ModelLayerLocation(id("locked_shulker_box"), "main");

    public static final Identifier CHEST_SPRITE = id("model/chests/normal");
    public static final Identifier ENDER_CHEST_SPRITE = id("model/locked_ender_chest");
    public static final Identifier SHULKER_BOX_SPRITE = id("model/shulkers/normal");

    public static final Identifier CHEST_ITEM_RENDERER = id("locked_chest");
    public static final Identifier SHULKER_BOX_ITEM_RENDERER = id("locked_shulker_box");

    public static final Identifier LOCKED_MODEL_LOADER = id("locked");
    public static final Identifier LOCKED_PROPERTY_ID = id("locked");
    public static final LockedItemProperty LOCKED_PROPERTY = new LockedItemProperty();

    private LocksStorageModels() {
    }

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, path);
    }
}
