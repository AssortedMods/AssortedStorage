package com.grim3212.assorted.containers.client.blockentity;

import com.grim3212.assorted.containers.Constants;
import com.grim3212.assorted.lib.core.storage.IStorage;
import com.grim3212.assorted.containers.client.blockentity.state.ItemTowerRenderState;
import com.grim3212.assorted.containers.client.model.ItemTowerModel;
import com.grim3212.assorted.containers.client.model.ContainersModelLayers;
import com.grim3212.assorted.lib.core.storage.BaseStorageBlock;
import com.grim3212.assorted.containers.common.block.blockentity.ItemTowerBlockEntity;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ItemTowerBlockEntityRenderer<T extends BlockEntity & IStorage> implements BlockEntityRenderer<T, ItemTowerRenderState> {

    private final ItemTowerModel model;
    private static final Identifier ITEM_TOWER_TEXTURE = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/model/item_tower.png");

    public ItemTowerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new ItemTowerModel(context.bakeLayer(ContainersModelLayers.ITEM_TOWER));
    }

    @Override
    public ItemTowerRenderState createRenderState() {
        return new ItemTowerRenderState();
    }

    @Override
    public void extractRenderState(T blockEntity, ItemTowerRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

        ItemTowerBlockEntity tower = (ItemTowerBlockEntity) blockEntity;
        Level level = tower.getLevel();
        boolean placedInLevel = level != null;
        BlockState blockstate = placedInLevel ? tower.getBlockState() : tower.getBlockState().setValue(BaseStorageBlock.FACING, Direction.SOUTH);

        state.renderModel = blockstate.getBlock() instanceof BaseStorageBlock;
        if (!state.renderModel) {
            return;
        }

        state.facing = blockstate.getValue(BaseStorageBlock.FACING);

        if (placedInLevel) {
            state.animatedModel = tower.model;
            state.model = new ItemTowerModel.State(level.getBlockEntity(tower.getBlockPos().above()) instanceof ItemTowerBlockEntity, level.getBlockEntity(tower.getBlockPos().below()) instanceof ItemTowerBlockEntity);
        } else {
            state.animatedModel = null;
            state.model = ItemTowerModel.State.INVENTORY;
        }
    }

    @Override
    public void submit(ItemTowerRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (!state.renderModel) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(-state.facing.toYRot()));
        poseStack.translate(-0.5D, -0.5D, -0.5D);

        ItemTowerModel towerModel = state.animatedModel != null ? state.animatedModel : this.model;
        submitNodeCollector.submitModel(towerModel, state.model, poseStack, ITEM_TOWER_TEXTURE, state.lightCoords, OverlayTexture.NO_OVERLAY, 0, state.breakProgress);

        poseStack.popPose();
    }
}
