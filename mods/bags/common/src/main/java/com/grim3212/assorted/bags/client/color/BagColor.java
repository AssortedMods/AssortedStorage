package com.grim3212.assorted.bags.client.color;

import com.grim3212.assorted.bags.common.item.BagItem;
import com.grim3212.assorted.lib.util.NBTHelper;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

/**
 * Tints a bag's body (layer 0) and strap (layer 1) from the two dye colours on the stack; the other layers, the
 * material trim and the lock, keep their own colours.
 */
public final class BagColor implements ItemColor {

    public static final BagColor INSTANCE = new BagColor();

    // The item renderer reads an alpha channel, so an opaque white is -1 rather than 0xFFFFFF.
    private static final int NO_TINT = -1;

    private BagColor() {
    }

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        String tag = switch (tintIndex) {
            case 0 -> BagItem.TAG_PRIMARY_COLOR;
            case 1 -> BagItem.TAG_SECONDARY_COLOR;
            default -> null;
        };
        if (tag == null || !NBTHelper.hasTag(stack, tag)) {
            return NO_TINT;
        }

        int dyeColor = NBTHelper.getInt(stack, tag);
        // getFireworkColor is an unpacked RGB, so it has to be made opaque explicitly.
        return dyeColor == -1 ? NO_TINT : FastColor.ARGB32.opaque(DyeColor.byId(dyeColor).getFireworkColor());
    }
}
