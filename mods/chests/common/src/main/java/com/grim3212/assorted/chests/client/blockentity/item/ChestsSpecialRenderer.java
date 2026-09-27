package com.grim3212.assorted.chests.client.blockentity.item;

import com.grim3212.assorted.lib.core.inventory.locking.StorageUtil;
import com.grim3212.assorted.chests.Constants;
import com.grim3212.assorted.chests.client.model.BaseStorageModel;
import com.grim3212.assorted.chests.client.model.ChestsModelState;
import com.grim3212.assorted.chests.client.model.ChestsModelType;
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
 * Draws the locked ender chest as an item. Whether the padlock
 * shows is read per stack in {@link #extractArgument(ItemStack)}.
 */
public class ChestsSpecialRenderer implements SpecialModelRenderer<Boolean> {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "storage");

    private final BaseStorageModel model;
    private final Identifier texture;

    public ChestsSpecialRenderer(BaseStorageModel model, Identifier texture) {
        this.model = model;
        this.texture = texture;
    }

    @Override
    public void submit(@Nullable Boolean locked, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        ChestsModelState state = Boolean.TRUE.equals(locked) ? ChestsModelState.CLOSED_LOCKED : ChestsModelState.CLOSED_UNLOCKED;
        submitNodeCollector.submitModel(this.model, state, poseStack, this.texture, lightCoords, overlayCoords, outlineColor, null);
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        PoseStack poseStack = new PoseStack();
        this.model.setupAnim(ChestsModelState.CLOSED_UNLOCKED);
        this.model.root().getExtentsForGui(poseStack, output);
    }

    @Override
    public @Nullable Boolean extractArgument(ItemStack stack) {
        return StorageUtil.hasCode(stack);
    }

    public record Unbaked(ChestsModelType model, Identifier texture) implements SpecialModelRenderer.Unbaked<Boolean> {
        public static final MapCodec<ChestsSpecialRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
                i -> i.group(
                        ChestsModelType.CODEC.fieldOf("model").forGetter(ChestsSpecialRenderer.Unbaked::model),
                        Identifier.CODEC.fieldOf("texture").forGetter(ChestsSpecialRenderer.Unbaked::texture)
                ).apply(i, ChestsSpecialRenderer.Unbaked::new)
        );

        @Override
        public MapCodec<ChestsSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public ChestsSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
            return new ChestsSpecialRenderer(this.model.bake(context.entityModelSet()), this.texture);
        }
    }
}
