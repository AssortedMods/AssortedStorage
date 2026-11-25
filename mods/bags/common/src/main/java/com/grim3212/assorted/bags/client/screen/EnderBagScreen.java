package com.grim3212.assorted.bags.client.screen;

import com.grim3212.assorted.lib.client.screen.storage.BaseStorageScreen;
import com.grim3212.assorted.lib.client.screen.storage.StorageScreenTextures;
import com.grim3212.assorted.bags.common.inventory.enderbag.EnderBagContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * Draws the bag's background the way {@link BaseStorageScreen} does, sized to the bag's rows and columns.
 */
public class EnderBagScreen extends AbstractContainerScreen<EnderBagContainer> {

    private static final ResourceLocation CHEST_GUI_TEXTURE_9_COLS = StorageScreenTextures.generic(9);

    private static final int X_ROWS = 3;
    private static final int Y_COLS = 9;
    private static final int TEXTURE_X_SIZE = 256;
    private static final int TEXTURE_Y_SIZE = 276;
    private static final int START_OF_PLAYER_INVENTORY_Y = 180;
    private static final int HEIGHT_OF_PLAYER_INVENTORY = 96;

    public EnderBagScreen(EnderBagContainer container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.imageWidth = 26 + 14 + Y_COLS * 18;
        this.imageHeight = 114 + X_ROWS * 18;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int i = this.leftPos;
        int j = this.topPos;

        graphics.blit(CHEST_GUI_TEXTURE_9_COLS, i, j, 0.0F, 0.0F, this.imageWidth, X_ROWS * 18 + 17, TEXTURE_X_SIZE, TEXTURE_Y_SIZE);
        graphics.blit(CHEST_GUI_TEXTURE_9_COLS, i, j + X_ROWS * 18 + 17, 0.0F, START_OF_PLAYER_INVENTORY_Y, this.imageWidth, HEIGHT_OF_PLAYER_INVENTORY, TEXTURE_X_SIZE, TEXTURE_Y_SIZE);
    }
}
