package com.grim3212.assorted.bags.client.screen;

import com.grim3212.assorted.lib.client.screen.storage.BaseStorageScreen;
import com.grim3212.assorted.lib.client.screen.storage.StorageScreenTextures;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.bags.common.inventory.bag.BagContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * Draws the bag's background the way {@link BaseStorageScreen} does, sized to the bag's rows and columns.
 */
public class BagScreen extends AbstractContainerScreen<BagContainer> {

    private static final ResourceLocation CHEST_GUI_TEXTURE_9_COLS = StorageScreenTextures.generic(9);
    private static final ResourceLocation CHEST_GUI_TEXTURE_10_COLS = StorageScreenTextures.generic(10);
    private static final ResourceLocation CHEST_GUI_TEXTURE_11_COLS = StorageScreenTextures.generic(11);
    private static final ResourceLocation CHEST_GUI_TEXTURE_12_COLS = StorageScreenTextures.generic(12);
    private static final ResourceLocation CHEST_GUI_TEXTURE_13_COLS = StorageScreenTextures.generic(13);
    private static final ResourceLocation CHEST_GUI_TEXTURE_14_COLS = StorageScreenTextures.generic(14);

    private static final int START_OF_PLAYER_INVENTORY_Y = 180;
    private static final int HEIGHT_OF_PLAYER_INVENTORY = 96;

    private final int xRows;
    private final int textureXSize;
    private final int textureYSize;
    private final ResourceLocation inventoryTexture;

    public BagScreen(BagContainer container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);

        StorageMaterial storageMaterial = container.getStorageMaterial();
        this.imageWidth = 26 + 14 + (storageMaterial == null ? 9 : storageMaterial.getYCols()) * 18;
        this.imageHeight = 114 + (storageMaterial == null ? 3 : storageMaterial.getXRows()) * 18;
        this.inventoryLabelY = this.imageHeight - 94;
        this.xRows = storageMaterial == null ? 3 : storageMaterial.getXRows();

        int yCols = storageMaterial == null ? 9 : storageMaterial.getYCols();
        switch (yCols) {
            case (10):
                this.textureXSize = 256;
                this.textureYSize = 276;
                this.inventoryTexture = CHEST_GUI_TEXTURE_10_COLS;
                break;
            case (11):
                this.textureXSize = 256;
                this.textureYSize = 276;
                this.inventoryTexture = CHEST_GUI_TEXTURE_11_COLS;
                break;
            case (12):
                this.textureXSize = 256;
                this.textureYSize = 276;
                this.inventoryTexture = CHEST_GUI_TEXTURE_12_COLS;
                break;
            case (13):
                this.textureXSize = 276;
                this.textureYSize = 276;
                this.inventoryTexture = CHEST_GUI_TEXTURE_13_COLS;
                break;
            case (14):
                this.textureXSize = 296;
                this.textureYSize = 276;
                this.inventoryTexture = CHEST_GUI_TEXTURE_14_COLS;
                break;
            default:
                this.textureXSize = 256;
                this.textureYSize = 276;
                this.inventoryTexture = CHEST_GUI_TEXTURE_9_COLS;
                break;
        }
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

        graphics.blit(this.inventoryTexture, i, j, 0.0F, 0.0F, this.imageWidth, this.xRows * 18 + 17, this.textureXSize, this.textureYSize);
        graphics.blit(this.inventoryTexture, i, j + this.xRows * 18 + 17, 0.0F, START_OF_PLAYER_INVENTORY_Y, this.imageWidth, HEIGHT_OF_PLAYER_INVENTORY, this.textureXSize, this.textureYSize);
    }
}
