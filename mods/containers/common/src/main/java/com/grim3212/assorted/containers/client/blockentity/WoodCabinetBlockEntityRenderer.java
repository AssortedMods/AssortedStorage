package com.grim3212.assorted.containers.client.blockentity;

import com.grim3212.assorted.containers.Constants;
import com.grim3212.assorted.lib.core.storage.IStorage;
import com.grim3212.assorted.containers.client.model.CabinetModel;
import com.grim3212.assorted.containers.client.model.ContainersModelLayers;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

public class WoodCabinetBlockEntityRenderer<T extends BlockEntity & IStorage> extends ContainersBlockEntityRenderer<T> {

    public static final ResourceLocation CABINET_TEXTURE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/model/cabinet.png");

    public WoodCabinetBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context, new CabinetModel(context.bakeLayer(ContainersModelLayers.CABINET)), CABINET_TEXTURE);
    }

}
