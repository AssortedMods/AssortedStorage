package com.grim3212.assorted.containers.client.blockentity;

import com.grim3212.assorted.lib.core.storage.IStorage;
import com.grim3212.assorted.containers.client.model.CabinetModel;
import com.grim3212.assorted.containers.client.model.ContainersModelLayers;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;

public class GlassCabinetBlockEntityRenderer<T extends BlockEntity & IStorage> extends ContainersBlockEntityRenderer<T> {

    public GlassCabinetBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context, new CabinetModel(context.bakeLayer(ContainersModelLayers.GLASS_CABINET)), WoodCabinetBlockEntityRenderer.CABINET_TEXTURE);
    }

}
