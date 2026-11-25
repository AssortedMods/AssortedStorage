package com.grim3212.assorted.containers.client.blockentity;

import com.grim3212.assorted.containers.Constants;
import com.grim3212.assorted.containers.api.LockerHalf;
import com.grim3212.assorted.containers.client.model.BaseStorageModel;
import com.grim3212.assorted.containers.client.model.ContainersModelLayers;
import com.grim3212.assorted.containers.client.model.ContainersModelState;
import com.grim3212.assorted.containers.client.model.DualLockerModel;
import com.grim3212.assorted.containers.client.model.LockerModel;
import com.grim3212.assorted.containers.common.block.LockerBlock;
import com.grim3212.assorted.containers.common.block.blockentity.LockerBlockEntity;
import com.grim3212.assorted.lib.core.storage.BaseStorageBlock;
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

public class LockerBlockEntityRenderer<T extends BlockEntity & IStorage> implements BlockEntityRenderer<T> {

    public static final ResourceLocation LOCKER_TEXTURE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/model/locker.png");

    private final LockerModel model;
    private final DualLockerModel dualModel;

    public LockerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new LockerModel(context.bakeLayer(ContainersModelLayers.LOCKER));
        this.dualModel = new DualLockerModel(context.bakeLayer(ContainersModelLayers.DUAL_LOCKER));
    }

    @Override
    public void render(T blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        LockerBlockEntity locker = (LockerBlockEntity) blockEntity;
        boolean placedInLevel = locker.getLevel() != null;

        // The bottom half of a double locker draws both halves, so the top draws nothing
        if (placedInLevel && locker.getBlockState().getValue(LockerBlock.HALF) == LockerHalf.TOP) {
            return;
        }

        BlockState blockstate = placedInLevel ? locker.getBlockState() : locker.getBlockState().setValue(BaseStorageBlock.FACING, Direction.SOUTH);
        if (!(blockstate.getBlock() instanceof BaseStorageBlock)) {
            return;
        }

        Direction facing = blockstate.getValue(BaseStorageBlock.FACING);
        boolean dual = placedInLevel && blockstate.getValue(LockerBlock.HALF) == LockerHalf.BOTTOM;

        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
        poseStack.translate(-0.5D, -0.5D, -0.5D);

        BaseStorageModel model = dual ? this.dualModel : this.model;
        model.setupAnim(new ContainersModelState(locker.getRotation(partialTicks) * 90.0F, !locker.isLocked()));
        model.renderToBuffer(poseStack, bufferSource.getBuffer(model.renderType(LOCKER_TEXTURE)), packedLight, packedOverlay);

        poseStack.popPose();
    }

}
