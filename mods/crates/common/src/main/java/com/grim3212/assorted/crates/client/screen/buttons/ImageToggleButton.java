package com.grim3212.assorted.crates.client.screen.buttons;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/**
 * A button drawn as a checkbox that stays ticked while it is on. The widget textures are sprites,
 * so the four states are four sprite ids rather than offsets into one image.
 */
public class ImageToggleButton extends Button {

    public static final ResourceLocation CHECKBOX = ResourceLocation.withDefaultNamespace("widget/checkbox");
    public static final ResourceLocation CHECKBOX_HIGHLIGHTED = ResourceLocation.withDefaultNamespace("widget/checkbox_highlighted");
    public static final ResourceLocation CHECKBOX_SELECTED = ResourceLocation.withDefaultNamespace("widget/checkbox_selected");
    public static final ResourceLocation CHECKBOX_SELECTED_HIGHLIGHTED = ResourceLocation.withDefaultNamespace("widget/checkbox_selected_highlighted");

    /** Every sprite this button can draw. */
    public static final List<ResourceLocation> SPRITES = List.of(CHECKBOX, CHECKBOX_HIGHLIGHTED, CHECKBOX_SELECTED, CHECKBOX_SELECTED_HIGHLIGHTED);

    private boolean buttonClicked;

    public ImageToggleButton(int x, int y, int width, int height, OnPress onPress, boolean clicked, Component tooltip) {
        this(x, y, width, height, onPress, clicked, CommonComponents.EMPTY, tooltip);
    }

    public ImageToggleButton(int x, int y, int width, int height, Button.OnPress onPress, boolean clicked, Component message, Component tooltip) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.buttonClicked = clicked;

        this.setTooltip(Tooltip.create(tooltip));
    }

    public void toggleButtonClicked() {
        this.buttonClicked = !this.buttonClicked;
    }

    public void setButtonClicked(boolean buttonClicked) {
        this.buttonClicked = buttonClicked;
    }

    public boolean isButtonClicked() {
        return this.buttonClicked;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        this.toggleButtonClicked();
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        boolean highlighted = this.isActive() && this.isHoveredOrFocused();
        ResourceLocation sprite;
        if (this.isButtonClicked()) {
            sprite = highlighted ? CHECKBOX_SELECTED_HIGHLIGHTED : CHECKBOX_SELECTED;
        } else {
            sprite = highlighted ? CHECKBOX_HIGHLIGHTED : CHECKBOX;
        }

        graphics.blitSprite(sprite, this.getX(), this.getY(), this.width, this.height);
    }
}
