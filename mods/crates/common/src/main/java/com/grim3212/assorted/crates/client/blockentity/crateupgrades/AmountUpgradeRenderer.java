package com.grim3212.assorted.crates.client.blockentity.crateupgrades;

import com.grim3212.assorted.lib.util.NBTHelper;
import com.grim3212.assorted.crates.api.crates.ICrateUpgradeRenderer;
import com.grim3212.assorted.crates.client.blockentity.CrateBlockEntityRenderer;
import com.grim3212.assorted.crates.client.blockentity.state.CrateRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

public class AmountUpgradeRenderer implements ICrateUpgradeRenderer {

    public static final AmountUpgradeRenderer INSTANCE = new AmountUpgradeRenderer();

    private static String getSlotAmount(CrateRenderState state, int slot, boolean showFull) {
        int amount = state.slotAmounts[slot];
        if (amount <= 0) {
            return "";
        }

        return showFull ? amount + "/" + state.slotCapacities[slot] : String.valueOf(amount);
    }


    private int getTextOffset(Font font, String text, float scale) {
        int scaledWidth = (int) Math.floor(1 / scale);
        int offset = (scaledWidth - font.width(text)) / 2;
        return offset - scaledWidth / 2;
    }

    @Override
    public void render(CrateRenderState state, ItemStack upgrade, PoseStack poseStack, MultiBufferSource buffer) {
        Font font = Minecraft.getInstance().font;

        boolean showFull = NBTHelper.getInt(upgrade, "Mode") > 0;

        poseStack.pushPose();
        CrateBlockEntityRenderer.faceCrate(state.facing, poseStack);
        poseStack.translate(-0.5D, 0.5D, 0.45D);
        float scale = 0.012F * 0.6666667F;
        poseStack.scale(scale, -scale, scale);

        int light = state.itemLightCoords;

        switch (state.layout) {
            case SINGLE:
                renderAmount(buffer, poseStack, font, getSlotAmount(state, 0, showFull), 63F, 90.0F, scale, light);
                break;
            case DOUBLE:
                renderAmount(buffer, poseStack, font, getSlotAmount(state, 0, showFull), 63F, 45.25F, scale, light);
                renderAmount(buffer, poseStack, font, getSlotAmount(state, 1, showFull), 63F, 100.0F, scale, light);
                break;
            case TRIPLE:
                renderAmount(buffer, poseStack, font, getSlotAmount(state, 0, showFull), 63F, 45.25F, scale, light);
                renderAmount(buffer, poseStack, font, getSlotAmount(state, 1, showFull), 35.5F, 100.0F, scale, light);
                renderAmount(buffer, poseStack, font, getSlotAmount(state, 2, showFull), 90F, 100.0F, scale, light);
                break;
            case QUADRUPLE:
                renderAmount(buffer, poseStack, font, getSlotAmount(state, 0, showFull), 35.5F, 45.25F, scale, light);
                renderAmount(buffer, poseStack, font, getSlotAmount(state, 1, showFull), 90F, 45.25F, scale, light);
                renderAmount(buffer, poseStack, font, getSlotAmount(state, 2, showFull), 35.5F, 100.0F, scale, light);
                renderAmount(buffer, poseStack, font, getSlotAmount(state, 3, showFull), 90F, 100.0F, scale, light);
                break;
        }

        poseStack.popPose();
    }

    private void renderAmount(MultiBufferSource buffer, PoseStack poseStack, Font font, String amount, float x, float y, float scale, int light) {
        if (amount.isEmpty()) {
            return;
        }

        font.drawInBatch(amount, getTextOffset(font, amount, scale) + x, y, 0xFF000000 | DyeColor.WHITE.getTextColor(), false, poseStack.last().pose(), buffer, Font.DisplayMode.NORMAL, 0, light);
    }
}
