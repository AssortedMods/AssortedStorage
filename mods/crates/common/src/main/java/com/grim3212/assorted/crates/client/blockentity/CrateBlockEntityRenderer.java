package com.grim3212.assorted.crates.client.blockentity;

import com.grim3212.assorted.lib.core.inventory.locking.LockItems;
import com.grim3212.assorted.lib.client.util.RenderHelper;
import com.grim3212.assorted.crates.api.LargeItemStack;
import com.grim3212.assorted.crates.api.StreamHelper;
import com.grim3212.assorted.crates.client.CratesClient;
import com.grim3212.assorted.crates.client.blockentity.crateupgrades.AmountUpgradeRenderer;
import com.grim3212.assorted.crates.client.blockentity.crateupgrades.PadlockUpgradeRenderer;
import com.grim3212.assorted.crates.client.blockentity.crateupgrades.VoidUpgradeRenderer;
import com.grim3212.assorted.crates.client.blockentity.state.CrateRenderState;
import com.grim3212.assorted.crates.client.util.ClientResources;
import com.grim3212.assorted.crates.common.block.CrateBlock;
import com.grim3212.assorted.crates.common.block.blockentity.CrateBlockEntity;
import com.grim3212.assorted.crates.common.inventory.crates.CrateSidedInv;
import com.grim3212.assorted.crates.common.item.upgrades.AmountUpgradeItem;
import com.grim3212.assorted.crates.common.item.upgrades.VoidUpgradeItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class CrateBlockEntityRenderer implements BlockEntityRenderer<CrateBlockEntity> {

    private final ItemRenderer itemRenderer;
    private final int viewDistance;

    public static final RenderType ICONS = RenderType.text(ClientResources.CRATE_ICONS_LOCATION);

    public CrateBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
        this.viewDistance = CratesClient.CLIENT_CONFIG.crateMaxRenderDistance.get();
    }

    @Override
    public int getViewDistance() {
        return this.viewDistance;
    }

    /** Reads everything this frame draws off the crate. */
    private static CrateRenderState extractRenderState(CrateBlockEntity crate, int packedLight, int packedOverlay) {
        CrateRenderState state = new CrateRenderState();
        CrateSidedInv inventory = crate.getItemStackStorageHandler();
        int slots = crate.getLayout().getNumStacks();

        state.facing = crate.getBlockState().getValue(CrateBlock.FACING);
        state.layout = crate.getLayout();
        state.lightCoords = packedLight;
        state.overlayCoords = packedOverlay;
        state.itemLightCoords = inventory.hasGlowUpgrade() ? LightTexture.FULL_BRIGHT : packedLight;
        state.anySlotLocked = inventory.anySlotsLocked();
        state.seed = (int) crate.getBlockPos().asLong();

        List<ItemStack> items = new ArrayList<>(slots);
        state.itemRotations = new float[slots];
        state.slotLocked = new boolean[slots];
        state.slotAmounts = new int[slots];
        state.slotCapacities = new int[slots];

        for (int slot = 0; slot < slots; slot++) {
            LargeItemStack largeStack = inventory.getLargeItemStack(slot);
            items.add(largeStack.getStack());
            state.itemRotations[slot] = largeStack.getRotation();
            state.slotLocked[slot] = inventory.isSlotLocked(slot);
            state.slotAmounts[slot] = largeStack.getAmount();
            state.slotCapacities[slot] = inventory.getMaxStackSizeForSlot(slot);
        }
        state.items = items;

        // Unique upgrades only
        state.upgrades = inventory.getEnhancements().stream().filter(StreamHelper.distinctByKey(ItemStack::getItem)).map(ItemStack::copy).toList();
        return state;
    }

    @Override
    public void render(CrateBlockEntity crate, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        CrateRenderState state = extractRenderState(crate, packedLight, packedOverlay);
        renderItems(state, crate.getLevel(), poseStack, buffer);
        renderUpgrades(state, poseStack, buffer);
        renderSlotLocks(state, poseStack, buffer);
    }

    private void renderItems(CrateRenderState state, Level level, PoseStack poseStack, MultiBufferSource buffer) {
        poseStack.pushPose();
        faceCrate(state.facing, poseStack);
        poseStack.translate(0.0D, 0.0D, 0.449D);

        poseStack.scale(0.5f, 0.5f, 0.5f);
        // Flatten the item so it sits against the crate face rather than sticking out of it.
        poseStack.scale(1.0F, 1.0F, 0.001F);

        switch (state.layout) {
            case SINGLE:
                renderItemInSlot(state, 0, level, poseStack, buffer, 0.0D, 0.0D);
                break;
            case DOUBLE:
                poseStack.scale(0.5f, 0.5f, 0.5f);
                renderItemInSlot(state, 0, level, poseStack, buffer, 0.0D, 0.875D);
                renderItemInSlot(state, 1, level, poseStack, buffer, 0.0D, -0.875D);
                break;
            case TRIPLE:
                poseStack.scale(0.5f, 0.5f, 0.5f);
                renderItemInSlot(state, 0, level, poseStack, buffer, 0.0D, 0.875D);
                renderItemInSlot(state, 1, level, poseStack, buffer, -0.875D, -0.875D);
                renderItemInSlot(state, 2, level, poseStack, buffer, 0.875D, -0.875D);
                break;
            case QUADRUPLE:
                poseStack.scale(0.5f, 0.5f, 0.5f);
                renderItemInSlot(state, 0, level, poseStack, buffer, -0.875D, 0.875D);
                renderItemInSlot(state, 1, level, poseStack, buffer, 0.875D, 0.875D);
                renderItemInSlot(state, 2, level, poseStack, buffer, -0.875D, -0.875D);
                renderItemInSlot(state, 3, level, poseStack, buffer, 0.875D, -0.875D);
                break;
        }

        poseStack.popPose();
    }

    private void renderItemInSlot(CrateRenderState state, int slot, Level level, PoseStack poseStack, MultiBufferSource buffer, double x, double y) {
        ItemStack stack = state.items.get(slot);
        if (stack.isEmpty()) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(x, y, 0.0D);
        poseStack.mulPose(Axis.ZP.rotationDegrees(-state.itemRotations[slot] * 360.0F / 16.0F));
        this.itemRenderer.renderStatic(stack, ItemDisplayContext.GUI, state.itemLightCoords, OverlayTexture.NO_OVERLAY, poseStack, buffer, level, state.seed + slot);
        poseStack.popPose();
    }

    private void renderUpgrades(CrateRenderState state, PoseStack poseStack, MultiBufferSource buffer) {
        poseStack.pushPose();
        for (ItemStack stack : state.upgrades) {
            if (LockItems.isLock(stack)) {
                PadlockUpgradeRenderer.INSTANCE.render(state, stack, poseStack, buffer);
            } else if (stack.getItem() instanceof VoidUpgradeItem) {
                VoidUpgradeRenderer.INSTANCE.render(state, stack, poseStack, buffer);
            } else if (stack.getItem() instanceof AmountUpgradeItem) {
                AmountUpgradeRenderer.INSTANCE.render(state, stack, poseStack, buffer);
            }
        }
        poseStack.popPose();
    }

    private void renderSlotLocks(CrateRenderState state, PoseStack poseStack, MultiBufferSource buffer) {
        if (!state.anySlotLocked) {
            return;
        }

        poseStack.pushPose();
        faceCrate(state.facing, poseStack);
        poseStack.translate(-0.5D, 0.5D, 0.45D);
        float scale = 0.012F * 0.6666667F;
        poseStack.scale(scale, -scale, scale);

        VertexConsumer consumer = buffer.getBuffer(ICONS);
        switch (state.layout) {
            case SINGLE:
                renderLockIcons(consumer, poseStack, state.lightCoords, state.slotLocked, new int[]{102}, new int[]{18});
                break;
            case DOUBLE:
                renderLockIcons(consumer, poseStack, state.lightCoords, state.slotLocked, new int[]{102, 102}, new int[]{18, 72});
                break;
            case TRIPLE:
                renderLockIcons(consumer, poseStack, state.lightCoords, state.slotLocked, new int[]{102, 47, 102}, new int[]{18, 72, 72});
                break;
            case QUADRUPLE:
                renderLockIcons(consumer, poseStack, state.lightCoords, state.slotLocked, new int[]{47, 102, 47, 102}, new int[]{18, 18, 72, 72});
                break;
        }

        poseStack.popPose();
    }

    private static void renderLockIcons(VertexConsumer consumer, PoseStack poseStack, int packedLight, boolean[] locked, int[] xs, int[] ys) {
        for (int slot = 0; slot < xs.length; slot++) {
            if (slot < locked.length && locked[slot]) {
                RenderHelper.lightedBlit(consumer, poseStack, xs[slot], ys[slot], 0, 2, 0, 5, 5, 8, 8, packedLight);
            }
        }
    }

    /**
     * Rotates into the crate's face. Shared by the item, upgrade and lock passes.
     */
    public static void faceCrate(Direction facing, PoseStack poseStack) {
        poseStack.translate(0.5D, 0.5D, 0.5D);
        if (facing.getAxis().isHorizontal()) {
            poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
        } else {
            poseStack.mulPose(Axis.XP.rotationDegrees(facing == Direction.DOWN ? 90F : 270F));
        }
    }
}
