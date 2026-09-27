package com.grim3212.assorted.chests.client.model;

import com.google.common.collect.Maps;
import com.grim3212.assorted.chests.Constants;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import net.minecraft.resources.Identifier;

import java.util.Map;
import java.util.stream.Stream;

public class ChestsModels {

    public static final Map<StorageMaterial, Identifier> CHEST_LOCATIONS = Maps.newLinkedHashMap();

    static {
        CHEST_LOCATIONS.put(null, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "model/chests/normal"));
        Stream.of(StorageMaterial.values()).forEach((type) -> CHEST_LOCATIONS.put(type, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "model/chests/" + type.toString())));
    }

}
