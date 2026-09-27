package com.grim3212.assorted.chests.client.model;

import com.mojang.serialization.Codec;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.StringRepresentable;

import java.util.function.Function;

/**
 * The storage models a special item renderer can draw, named so an item model json can choose one.
 */
public enum ChestsModelType implements StringRepresentable {
    CHEST("chest", ChestsModelLayers.LOCKED_CHEST, ChestModel::new);

    public static final Codec<ChestsModelType> CODEC = StringRepresentable.fromEnum(ChestsModelType::values);

    private final String name;
    private final ModelLayerLocation layer;
    private final Function<ModelPart, BaseStorageModel> factory;

    ChestsModelType(String name, ModelLayerLocation layer, Function<ModelPart, BaseStorageModel> factory) {
        this.name = name;
        this.layer = layer;
        this.factory = factory;
    }

    public BaseStorageModel bake(EntityModelSet models) {
        return this.factory.apply(models.bakeLayer(this.layer));
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
