package com.grim3212.assorted.chests.client.model;

import com.grim3212.assorted.chests.Constants;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;

public class ChestsModelLayers {
    public static final ModelLayerLocation LOCKED_CHEST = new ModelLayerLocation(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "locked_chest"), "main");
}
