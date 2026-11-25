package com.grim3212.assorted.containers.client.blockentity;

import com.grim3212.assorted.containers.Constants;
import com.grim3212.assorted.containers.client.model.ContainersModelLayers;
import com.grim3212.assorted.containers.client.model.ItemTowerModel;
import com.grim3212.assorted.containers.common.block.blockentity.ItemTowerBlockEntity;
import com.grim3212.assorted.lib.core.storage.BaseStorageBlock;
import com.grim3212.assorted.lib.core.storage.IStorage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ItemTowerBlockEntityRenderer<T extends BlockEntity & IStorage> implements BlockEntityRenderer<T> {

    public static final ResourceLocation ITEM_TOWER_TEXTURE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/model/item_tower.png");

    private final ItemTowerModel model;

    public ItemTowerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new ItemTowerModel(context.bakeLayer(ContainersModelLayers.ITEM_TOWER));
    }

    @Override
    public void render(T blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemTowerBlockEntity tower = (ItemTowerBlockEntity) blockEntity;
        Level level = tower.getLevel();
        BlockState blockstate = level != null ? tower.getBlockState() : tower.getBlockState().setValue(BaseStorageBlock.FACING, Direction.SOUTH);
        if (!(blockstate.getBlock() instanceof BaseStorageBlock)) {
            return;
        }

        Direction facing = blockstate.getValue(BaseStorageBlock.FACING);

        // A placed tower draws its own model, whose shelf scroll carries over from frame to frame.
        ItemTowerModel towerModel;
        ItemTowerModel.State state;
        if (level != null) {
            towerModel = tower.model != null ? tower.model : this.model;
            state = new ItemTowerModel.State(level.getBlockEntity(tower.getBlockPos().above()) instanceof ItemTowerBlockEntity, level.getBlockEntity(tower.getBlockPos().below()) instanceof ItemTowerBlockEntity);
        } else {
            towerModel = this.model;
            state = ItemTowerModel.State.INVENTORY;
        }

        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
        poseStack.translate(-0.5D, -0.5D, -0.5D);

        towerModel.setupAnim(state);
        towerModel.renderToBuffer(poseStack, bufferSource.getBuffer(towerModel.renderType(ITEM_TOWER_TEXTURE)), packedLight, packedOverlay);

        poseStack.popPose();
    }
}
