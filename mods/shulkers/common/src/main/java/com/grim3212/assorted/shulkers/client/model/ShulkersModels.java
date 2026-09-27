package com.grim3212.assorted.shulkers.client.model;

import com.google.common.collect.Maps;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.shulkers.Constants;
import net.minecraft.resources.Identifier;

import java.util.Map;
import java.util.stream.Stream;

public class ShulkersModels {

    public static final Map<StorageMaterial, Identifier> SHULKER_LOCATIONS = Maps.newLinkedHashMap();

    static {
        SHULKER_LOCATIONS.put(null, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "model/shulkers/normal"));
        Stream.of(StorageMaterial.values()).forEach((type) -> SHULKER_LOCATIONS.put(type, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "model/shulkers/" + type.toString())));
    }

}
