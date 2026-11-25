package com.grim3212.assorted.containers.client.blockentity.item;

import com.grim3212.assorted.containers.client.model.BaseStorageModel;
import com.grim3212.assorted.containers.client.model.ContainersModelState;
import com.grim3212.assorted.containers.client.model.ContainersModelType;
import com.grim3212.assorted.lib.core.inventory.locking.StorageUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Draws a cabinet, safe, locker or warehouse crate as an item, closed, with its padlock when the
 * stack carries a lock. The item's model must be special ({@code "parent": "builtin/entity"}), and
 * the renderer registered for it through {@code ISpecialModelRendererRegistry}.
 */
public class ContainersItemRenderer extends BlockEntityWithoutLevelRenderer {

    private final ContainersModelType type;
    private final ResourceLocation texture;
    private BaseStorageModel model;

    public ContainersItemRenderer(ContainersModelType type, ResourceLocation texture) {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        this.type = type;
        this.texture = texture;
    }

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
        this.model = this.type.bake(Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (this.model == null) {
            this.model = this.type.bake(Minecraft.getInstance().getEntityModels());
        }

        this.model.setupAnim(StorageUtil.hasCode(stack) ? ContainersModelState.CLOSED_LOCKED : ContainersModelState.CLOSED_UNLOCKED);
        this.model.renderToBuffer(poseStack, ItemRenderer.getFoilBufferDirect(bufferSource, this.model.renderType(this.texture), false, stack.hasFoil()), packedLight, packedOverlay);
    }
}
