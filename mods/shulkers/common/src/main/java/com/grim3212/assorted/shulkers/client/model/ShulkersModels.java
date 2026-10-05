package com.grim3212.assorted.shulkers.client.model;

import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.shulkers.Constants;
import net.minecraft.resources.Identifier;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

public class ShulkersModels {

    public static final Map<StorageMaterial, Identifier> SHULKER_LOCATIONS = new EnumMap<>(StorageMaterial.class);

    public static final Identifier ITEM_RENDERER = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "locked_shulker_box");

    static {
        Stream.of(StorageMaterial.values()).forEach((type) -> SHULKER_LOCATIONS.put(type, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "model/shulkers/" + type.toString())));
    }
}
