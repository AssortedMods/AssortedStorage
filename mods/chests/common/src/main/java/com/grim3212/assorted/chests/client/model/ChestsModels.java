package com.grim3212.assorted.chests.client.model;

import com.grim3212.assorted.chests.Constants;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import net.minecraft.resources.Identifier;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

public class ChestsModels {

    public static final Map<StorageMaterial, Identifier> CHEST_LOCATIONS = new EnumMap<>(StorageMaterial.class);

    public static final Identifier ITEM_RENDERER = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "locked_chest");

    static {
        Stream.of(StorageMaterial.values()).forEach((type) -> CHEST_LOCATIONS.put(type, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "model/chests/" + type.toString())));
    }
}
