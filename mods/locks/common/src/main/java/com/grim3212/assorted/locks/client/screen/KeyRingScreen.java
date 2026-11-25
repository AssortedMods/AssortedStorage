package com.grim3212.assorted.locks.client.screen;

import com.grim3212.assorted.locks.Constants;
import com.grim3212.assorted.locks.common.inventory.keyring.KeyRingContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/** The key ring: its key slots above the player's inventory. */
public class KeyRingScreen extends AbstractContainerScreen<KeyRingContainer> {

    private static final ResourceLocation KEY_RING_GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/container/key_ring.png");

    public KeyRingScreen(KeyRingContainer container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.imageHeight = 168;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        graphics.blit(KEY_RING_GUI_TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, 176, 256, 256);
    }
}
