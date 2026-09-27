package com.grim3212.assorted.containers.client.blockentity;

import com.grim3212.assorted.containers.Constants;
import com.grim3212.assorted.lib.core.storage.IStorage;
import com.grim3212.assorted.containers.client.model.SafeModel;
import com.grim3212.assorted.containers.client.model.ContainersModelLayers;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;

public class GoldSafeBlockEntityRenderer<T extends BlockEntity & IStorage> extends ContainersBlockEntityRenderer<T> {

    public GoldSafeBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context, new SafeModel(context.bakeLayer(ContainersModelLayers.SAFE)), Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/model/gold_safe.png"));
    }

}
