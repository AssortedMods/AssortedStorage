package com.grim3212.assorted.containers.client.blockentity;

import com.grim3212.assorted.containers.Constants;
import com.grim3212.assorted.containers.api.Wood;
import com.grim3212.assorted.containers.client.model.ContainersModelLayers;
import com.grim3212.assorted.containers.client.model.ContainersModelState;
import com.grim3212.assorted.containers.client.model.WarehouseCrateModel;
import com.grim3212.assorted.containers.common.block.WarehouseCrateBlock;
import com.grim3212.assorted.containers.common.block.blockentity.WarehouseCrateBlockEntity;
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

import java.util.EnumMap;
import java.util.Map;

public class WarehouseCrateBlockEntityRenderer<T extends BlockEntity & IStorage> implements BlockEntityRenderer<T> {

    private final WarehouseCrateModel model;

    /**
     * The one place a warehouse crate's texture path is spelled out. The block item's renderer
     * draws with the same path, so the item and the placed block always draw the same picture.
     */
    private static final Map<Wood, ResourceLocation> TEXTURES = new EnumMap<>(Wood.class);

    static {
        for (Wood wood : Wood.values()) {
            TEXTURES.put(wood, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/model/warehouse_crate/" + wood + ".png"));
        }
    }

    public static ResourceLocation texture(Wood wood) {
        return TEXTURES.get(wood);
    }

    public WarehouseCrateBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new WarehouseCrateModel(context.bakeLayer(ContainersModelLayers.WAREHOUSE_CRATE));
    }

    @Override
    public void render(T blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        WarehouseCrateBlockEntity crate = (WarehouseCrateBlockEntity) blockEntity;
        BlockState blockstate = crate.getLevel() != null ? crate.getBlockState() : crate.getBlockState().setValue(BaseStorageBlock.FACING, Direction.SOUTH);
        if (!(blockstate.getBlock() instanceof WarehouseCrateBlock crateBlock)) {
            return;
        }

        Direction facing = blockstate.getValue(BaseStorageBlock.FACING);

        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
        poseStack.translate(-0.5D, -0.5D, -0.5D);

        this.model.setupAnim(new ContainersModelState(crate.getRotation(partialTicks) * 90.0F, !crate.isLocked()));
        this.model.renderToBuffer(poseStack, bufferSource.getBuffer(this.model.renderType(texture(crateBlock.getWoodType()))), packedLight, packedOverlay);

        poseStack.popPose();
    }
}
