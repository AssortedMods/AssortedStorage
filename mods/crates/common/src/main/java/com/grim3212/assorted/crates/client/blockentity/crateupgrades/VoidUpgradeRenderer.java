package com.grim3212.assorted.crates.client.blockentity.crateupgrades;

import com.grim3212.assorted.lib.client.util.RenderHelper;
import com.grim3212.assorted.crates.api.crates.ICrateUpgradeRenderer;
import com.grim3212.assorted.crates.client.blockentity.CrateBlockEntityRenderer;
import com.grim3212.assorted.crates.client.blockentity.state.CrateRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;

public class VoidUpgradeRenderer implements ICrateUpgradeRenderer {

    public static final VoidUpgradeRenderer INSTANCE = new VoidUpgradeRenderer();

    @Override
    public void render(CrateRenderState state, ItemStack upgrade, PoseStack poseStack, MultiBufferSource buffer) {
        poseStack.pushPose();
        CrateBlockEntityRenderer.faceCrate(state.facing, poseStack);

        poseStack.translate(-0.5D, 0.5D, 0.45D);
        float scale = 0.012F * 0.6666667F;
        poseStack.scale(scale, -scale, scale);

        RenderHelper.lightedBlit(buffer.getBuffer(CrateBlockEntityRenderer.ICONS), poseStack, 116, 116, 7, 0, 0, 4, 4, 16, 16, state.lightCoords);

        poseStack.popPose();
    }
}
