package com.grim3212.assorted.containers.client.blockentity;

import com.grim3212.assorted.containers.Constants;
import com.grim3212.assorted.lib.core.storage.IStorage;
import com.grim3212.assorted.containers.client.model.SafeModel;
import com.grim3212.assorted.containers.client.model.ContainersModelLayers;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ObsidianSafeBlockEntityRenderer<T extends BlockEntity & IStorage> extends ContainersBlockEntityRenderer<T> {

    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/model/obsidian_safe.png");

    public ObsidianSafeBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context, new SafeModel(context.bakeLayer(ContainersModelLayers.SAFE)), TEXTURE);
    }

}
