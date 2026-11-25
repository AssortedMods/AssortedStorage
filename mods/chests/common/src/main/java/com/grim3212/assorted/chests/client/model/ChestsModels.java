package com.grim3212.assorted.chests.client.model;

import com.grim3212.assorted.chests.Constants;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import net.minecraft.resources.ResourceLocation;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

public class ChestsModels {

    public static final Map<StorageMaterial, ResourceLocation> CHEST_LOCATIONS = new EnumMap<>(StorageMaterial.class);

    static {
        Stream.of(StorageMaterial.values()).forEach((type) -> CHEST_LOCATIONS.put(type, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "model/chests/" + type.toString())));
    }
}
