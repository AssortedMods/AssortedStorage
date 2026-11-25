package com.grim3212.assorted.containers.client.blockentity.item;

import com.grim3212.assorted.containers.client.blockentity.ItemTowerBlockEntityRenderer;
import com.grim3212.assorted.containers.client.model.ContainersModelLayers;
import com.grim3212.assorted.containers.client.model.ItemTowerModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * The item tower as an item: a lone, capped section with empty shelves. See
 * {@link ContainersItemRenderer} for how it is bound to the item.
 */
public class ItemTowerItemRenderer extends BlockEntityWithoutLevelRenderer {

    private ItemTowerModel model;

    public ItemTowerItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    private static ItemTowerModel bake() {
        return new ItemTowerModel(Minecraft.getInstance().getEntityModels().bakeLayer(ContainersModelLayers.ITEM_TOWER));
    }

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
        this.model = bake();
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (this.model == null) {
            this.model = bake();
        }

        this.model.setupAnim(ItemTowerModel.State.INVENTORY);
        this.model.renderToBuffer(poseStack, ItemRenderer.getFoilBufferDirect(bufferSource, this.model.renderType(ItemTowerBlockEntityRenderer.ITEM_TOWER_TEXTURE), false, stack.hasFoil()), packedLight, packedOverlay);
    }
}
