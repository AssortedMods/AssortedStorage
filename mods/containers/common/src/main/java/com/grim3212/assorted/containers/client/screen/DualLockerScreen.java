package com.grim3212.assorted.containers.client.screen;

import com.grim3212.assorted.containers.Constants;
import com.grim3212.assorted.containers.common.inventory.LockerContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;

/**
 * The double locker: six rows of its inventory at a time, scrolled with the arrows or the mouse wheel.
 */
public class DualLockerScreen extends AbstractContainerScreen<LockerContainer> {

    private static final ResourceLocation LOCKER_TEXTURE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/container/locker.png");
    private int rowId = 0;

    public DualLockerScreen(LockerContainer container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.imageWidth = 176 + 17;
        this.imageHeight = 204;
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

        graphics.blit(LOCKER_TEXTURE, i, j, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
        graphics.blit(LOCKER_TEXTURE, i + (this.imageWidth - 18), j + 20 + this.rowId, this.imageWidth, 0.0F, 10, 5, 256, 256);
    }

    public void scrollInventory(boolean directionDown, boolean playSound) {
        int prevRowID = this.rowId;

        if (directionDown) {
            if (this.rowId < 5)
                this.rowId += 1;
        } else if (this.rowId > 0)
            this.rowId -= 1;

        if (prevRowID != this.rowId) {
            this.menu.setDisplayRow(this.rowId);
            if (playSound)
                this.minecraft.player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1.0F, 1.0F);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        double modx = mouseX - (this.width - this.imageWidth) / 2;
        double mody = mouseY - (this.height - this.imageHeight) / 2;

        if (modx >= 173 && modx < 186 && mody >= 7 && mody < 20)
            scrollInventory(false, true);

        if (modx >= 173 && modx < 186 && mody >= 30 && mody < 43)
            scrollInventory(true, true);

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
        if (scrollY >= 1.0D) {
            scrollInventory(false, true);
        } else {
            scrollInventory(true, true);
        }

        return super.mouseScrolled(x, y, scrollX, scrollY);
    }
}
