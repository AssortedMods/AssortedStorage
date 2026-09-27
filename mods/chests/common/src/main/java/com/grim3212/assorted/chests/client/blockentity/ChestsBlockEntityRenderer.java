package com.grim3212.assorted.chests.client.blockentity;

import com.grim3212.assorted.lib.core.storage.IStorage;
import com.grim3212.assorted.chests.client.blockentity.state.ChestsBlockRenderState;
import com.grim3212.assorted.chests.client.model.BaseStorageModel;
import com.grim3212.assorted.chests.client.model.ChestsModelState;
import com.grim3212.assorted.lib.core.storage.BaseStorageBlock;
import com.grim3212.assorted.lib.core.storage.BaseStorageBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * A block entity renderer is now three methods instead of one: it creates a render state, extracts
 * into it from the block entity, and later submits from that state alone. The vertex consumer is gone
 * too - {@code SubmitNodeCollector#submitModel} takes the model and its state and draws it later.
 */
public class ChestsBlockEntityRenderer<T extends BlockEntity & IStorage> implements BlockEntityRenderer<T, ChestsBlockRenderState> {

    private final BaseStorageModel model;
    private final Identifier textureLocation;

    public ChestsBlockEntityRenderer(BlockEntityRendererProvider.Context context, BaseStorageModel model, Identifier textureLocation) {
        this.model = model;
        this.textureLocation = textureLocation;
    }

    @Override
    public ChestsBlockRenderState createRenderState() {
        return new ChestsBlockRenderState();
    }

    @Override
    public void extractRenderState(T blockEntity, ChestsBlockRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

        BaseStorageBlockEntity storage = (BaseStorageBlockEntity) blockEntity;
        boolean placedInLevel = storage.getLevel() != null;
        BlockState blockstate = placedInLevel ? storage.getBlockState() : storage.getBlockState().setValue(BaseStorageBlock.FACING, Direction.SOUTH);

        state.renderModel = blockstate.getBlock() instanceof BaseStorageBlock;
        if (!state.renderModel) {
            return;
        }

        state.facing = blockstate.getValue(BaseStorageBlock.FACING);
        state.model = new ChestsModelState(storage.getRotation(partialTicks) * 90.0F, !alwaysLocked() && !storage.isLocked());
    }

    @Override
    public void submit(ChestsBlockRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (!state.renderModel) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(-state.facing.toYRot()));
        poseStack.translate(-0.5D, -0.5D, -0.5D);

        submitNodeCollector.submitModel(this.model, state.model, poseStack, this.textureLocation, state.lightCoords, OverlayTexture.NO_OVERLAY, 0, state.breakProgress);

        poseStack.popPose();
    }

    protected boolean alwaysLocked() {
        return false;
    }
}
