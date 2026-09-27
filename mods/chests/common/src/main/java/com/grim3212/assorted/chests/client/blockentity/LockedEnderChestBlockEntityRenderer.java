package com.grim3212.assorted.chests.client.blockentity;

import com.grim3212.assorted.chests.Constants;
import com.grim3212.assorted.lib.core.storage.IStorage;
import com.grim3212.assorted.chests.client.model.ChestModel;
import com.grim3212.assorted.chests.client.model.ChestsModelLayers;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;

public class LockedEnderChestBlockEntityRenderer<T extends BlockEntity & IStorage> extends ChestsBlockEntityRenderer<T> {

    public LockedEnderChestBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context, new ChestModel(context.bakeLayer(ChestsModelLayers.LOCKED_CHEST)), Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/model/locked_ender_chest.png"));
    }

    @Override
    protected boolean alwaysLocked() {
        return true;
    }
}
