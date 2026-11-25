package com.grim3212.assorted.containers.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

/**
 * Base for the storage block models. Which parts draw is set as {@link ModelPart#visible} flags in
 * {@link #setupAnim(ContainersModelState)}, which has to run before each draw.
 */
public abstract class BaseStorageModel extends Model {

    private final ModelPart root;

    public BaseStorageModel(ModelPart root, Function<ResourceLocation, RenderType> renderTypeIn) {
        super(renderTypeIn);
        this.root = root;
    }

    public ModelPart root() {
        return this.root;
    }

    public abstract void setupAnim(ContainersModelState state);

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.root.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
