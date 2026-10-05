package com.grim3212.assorted.containers.client.blockentity.item;

import com.grim3212.assorted.lib.core.inventory.locking.StorageUtil;
import com.grim3212.assorted.containers.Constants;
import com.grim3212.assorted.containers.client.model.BaseStorageModel;
import com.grim3212.assorted.containers.client.model.ContainersModelState;
import com.grim3212.assorted.containers.client.model.ContainersModelType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3fc;

import java.util.function.Consumer;

/**
 * Draws a cabinet, safe, locker or warehouse crate as an item. Whether the padlock
 * shows is read per stack in {@link #extractArgument(ItemStack)}.
 */
public class ContainersSpecialRenderer implements SpecialModelRenderer<Boolean> {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "storage");

    private final BaseStorageModel model;
    private final Identifier texture;

    public ContainersSpecialRenderer(BaseStorageModel model, Identifier texture) {
        this.model = model;
        this.texture = texture;
    }

    @Override
    public void submit(@Nullable Boolean locked, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        ContainersModelState state = Boolean.TRUE.equals(locked) ? ContainersModelState.CLOSED_LOCKED : ContainersModelState.CLOSED_UNLOCKED;
        submitNodeCollector.submitModel(this.model, state, poseStack, this.texture, lightCoords, overlayCoords, outlineColor, null);
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        PoseStack poseStack = new PoseStack();
        this.model.setupAnim(ContainersModelState.CLOSED_UNLOCKED);
        this.model.root().getExtentsForGui(poseStack, output);
    }

    @Override
    public @Nullable Boolean extractArgument(ItemStack stack) {
        return StorageUtil.hasCode(stack);
    }

    public record Unbaked(ContainersModelType model, Identifier texture) implements SpecialModelRenderer.Unbaked<Boolean> {
        public static final MapCodec<ContainersSpecialRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
                i -> i.group(
                        ContainersModelType.CODEC.fieldOf("model").forGetter(ContainersSpecialRenderer.Unbaked::model),
                        Identifier.CODEC.fieldOf("texture").forGetter(ContainersSpecialRenderer.Unbaked::texture)
                ).apply(i, ContainersSpecialRenderer.Unbaked::new)
        );

        @Override
        public MapCodec<ContainersSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public ContainersSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
            return new ContainersSpecialRenderer(this.model.bake(context.entityModelSet()), this.texture);
        }
    }
}
