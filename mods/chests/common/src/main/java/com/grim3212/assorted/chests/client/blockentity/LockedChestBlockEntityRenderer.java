package com.grim3212.assorted.chests.client.blockentity;

import com.grim3212.assorted.lib.core.storage.IStorage;
import com.grim3212.assorted.chests.client.blockentity.state.SpriteStorageRenderState;
import com.grim3212.assorted.chests.client.model.ChestModel;
import com.grim3212.assorted.chests.client.model.ChestsModelLayers;
import com.grim3212.assorted.chests.client.model.ChestsModelState;
import com.grim3212.assorted.chests.client.model.ChestsModels;
import com.grim3212.assorted.lib.core.storage.BaseStorageBlock;
import com.grim3212.assorted.chests.common.block.LockedChestBlock;
import com.grim3212.assorted.lib.core.storage.BaseStorageBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class LockedChestBlockEntityRenderer<T extends BlockEntity & IStorage> implements BlockEntityRenderer<T, SpriteStorageRenderState> {

    private final ChestModel model;
    private final SpriteGetter sprites;

    public LockedChestBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new ChestModel(context.bakeLayer(ChestsModelLayers.LOCKED_CHEST));
        this.sprites = context.sprites();
    }

    @Override
    public SpriteStorageRenderState createRenderState() {
        return new SpriteStorageRenderState();
    }

    @Override
    public void extractRenderState(T blockEntity, SpriteStorageRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

        BaseStorageBlockEntity storage = (BaseStorageBlockEntity) blockEntity;
        boolean placedInLevel = storage.getLevel() != null;
        BlockState blockstate = placedInLevel ? storage.getBlockState() : storage.getBlockState().setValue(BaseStorageBlock.FACING, Direction.SOUTH);

        state.renderModel = blockstate.getBlock() instanceof LockedChestBlock;
        if (!state.renderModel) {
            return;
        }

        LockedChestBlock lockedChest = (LockedChestBlock) blockstate.getBlock();
        state.facing = blockstate.getValue(BaseStorageBlock.FACING);
        state.sprite = new SpriteId(Sheets.CHEST_SHEET, ChestsModels.CHEST_LOCATIONS.get(lockedChest.getStorageMaterial()));
        state.model = new ChestsModelState(storage.getRotation(partialTicks) * 90.0F, !storage.isLocked());
    }

    @Override
    public void submit(SpriteStorageRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (!state.renderModel) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(-state.facing.toYRot()));
        poseStack.translate(-0.5D, -0.5D, -0.5D);

        submitNodeCollector.submitModel(this.model, state.model, poseStack, state.lightCoords, OverlayTexture.NO_OVERLAY, -1, state.sprite, this.sprites, 0, state.breakProgress);

        poseStack.popPose();
    }
}
