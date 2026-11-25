package com.grim3212.assorted.crates.api.crates;

import com.grim3212.assorted.crates.client.blockentity.state.CrateRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;


public interface ICrateUpgradeRenderer {

    /**
     * Draws this upgrade's overlay on the crate face. Everything it draws comes from the state read
     * off the crate, so nothing here reaches back into the level.
     */
    void render(CrateRenderState state, ItemStack upgrade, PoseStack poseStack, MultiBufferSource buffer);
}
