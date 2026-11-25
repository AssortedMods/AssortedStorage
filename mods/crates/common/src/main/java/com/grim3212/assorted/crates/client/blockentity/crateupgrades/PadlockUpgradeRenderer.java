package com.grim3212.assorted.crates.client.blockentity.crateupgrades;

import com.grim3212.assorted.lib.client.util.RenderHelper;
import com.grim3212.assorted.crates.api.crates.CrateLayout;
import com.grim3212.assorted.crates.api.crates.ICrateUpgradeRenderer;
import com.grim3212.assorted.crates.client.blockentity.CrateBlockEntityRenderer;
import com.grim3212.assorted.crates.client.blockentity.state.CrateRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;

public class PadlockUpgradeRenderer implements ICrateUpgradeRenderer {

    public static final PadlockUpgradeRenderer INSTANCE = new PadlockUpgradeRenderer();

    @Override
    public void render(CrateRenderState state, ItemStack upgrade, PoseStack poseStack, MultiBufferSource buffer) {
        poseStack.pushPose();
        CrateBlockEntityRenderer.faceCrate(state.facing, poseStack);

        poseStack.translate(-0.5D, 0.5D, 0.455D);
        float scale = 0.052F * 0.6666667F;
        poseStack.scale(scale, -scale, scale);
        poseStack.translate(0D, -0.5D, -0.455D);
        int yLoc = state.layout != CrateLayout.SINGLE ? 13 : 1;

        RenderHelper.lightedBlit(buffer.getBuffer(CrateBlockEntityRenderer.ICONS), poseStack, 13, yLoc, 2, 0, 4, 3, 6, 16, 16, state.lightCoords);

        poseStack.popPose();
    }
}
