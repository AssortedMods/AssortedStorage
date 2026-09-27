package com.grim3212.assorted.containers.client.model;

import com.mojang.serialization.Codec;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.StringRepresentable;

import java.util.function.Function;

/**
 * The storage models a special item renderer can draw, named so an item model json can choose one.
 */
public enum ContainersModelType implements StringRepresentable {
    CABINET("cabinet", ContainersModelLayers.CABINET, CabinetModel::new),
    GLASS_CABINET("glass_cabinet", ContainersModelLayers.GLASS_CABINET, CabinetModel::new),
    SAFE("safe", ContainersModelLayers.SAFE, SafeModel::new),
    LOCKER("locker", ContainersModelLayers.LOCKER, LockerModel::new),
    DUAL_LOCKER("dual_locker", ContainersModelLayers.DUAL_LOCKER, DualLockerModel::new),
    WAREHOUSE_CRATE("warehouse_crate", ContainersModelLayers.WAREHOUSE_CRATE, WarehouseCrateModel::new);

    public static final Codec<ContainersModelType> CODEC = StringRepresentable.fromEnum(ContainersModelType::values);

    private final String name;
    private final ModelLayerLocation layer;
    private final Function<ModelPart, BaseStorageModel> factory;

    ContainersModelType(String name, ModelLayerLocation layer, Function<ModelPart, BaseStorageModel> factory) {
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
