package com.grim3212.assorted.containers.client.blockentity;

import com.grim3212.assorted.containers.client.model.BaseStorageModel;
import com.grim3212.assorted.containers.client.model.ContainersModelState;
import com.grim3212.assorted.lib.core.storage.BaseStorageBlock;
import com.grim3212.assorted.lib.core.storage.BaseStorageBlockEntity;
import com.grim3212.assorted.lib.core.storage.IStorage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Draws a storage block as one {@link BaseStorageModel}, turned to the block's facing, with the
 * padlock in place of the handle while it is locked. The block breaking overlay needs nothing here:
 * the dispatcher hands a renderer a buffer source that already draws it over whatever is rendered.
 */
public class ContainersBlockEntityRenderer<T extends BlockEntity & IStorage> implements BlockEntityRenderer<T> {

    private final BaseStorageModel model;
    private final ResourceLocation textureLocation;

    public ContainersBlockEntityRenderer(BlockEntityRendererProvider.Context context, BaseStorageModel model, ResourceLocation textureLocation) {
        this.model = model;
        this.textureLocation = textureLocation;
    }

    @Override
    public void render(T blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BaseStorageBlockEntity storage = (BaseStorageBlockEntity) blockEntity;
        BlockState blockstate = storage.getLevel() != null ? storage.getBlockState() : storage.getBlockState().setValue(BaseStorageBlock.FACING, Direction.SOUTH);
        if (!(blockstate.getBlock() instanceof BaseStorageBlock)) {
            return;
        }

        Direction facing = blockstate.getValue(BaseStorageBlock.FACING);

        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
        poseStack.translate(-0.5D, -0.5D, -0.5D);

        this.model.setupAnim(new ContainersModelState(storage.getRotation(partialTicks) * 90.0F, !alwaysLocked() && !storage.isLocked()));
        this.model.renderToBuffer(poseStack, bufferSource.getBuffer(this.model.renderType(this.textureLocation)), packedLight, packedOverlay);

        poseStack.popPose();
    }

    protected boolean alwaysLocked() {
        return false;
    }
}
