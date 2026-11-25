package com.grim3212.assorted.bags.client.properties;

import com.grim3212.assorted.bags.Constants;
import com.grim3212.assorted.bags.common.item.BagItem;
import com.grim3212.assorted.lib.core.inventory.locking.StorageUtil;
import com.grim3212.assorted.lib.util.NBTHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * What a bag's item model overrides select on: whether the stack is dyed, and whether it is locked, each 1 or 0.
 * Both live in {@code custom_data}, which no vanilla property can test for an arbitrary value.
 */
public enum HasStorageTagProperty implements ClampedItemPropertyFunction {
    DYED(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "dyed")) {
        @Override
        boolean test(ItemStack stack) {
            // A stored -1 means "no dye", same as BagColor treats it, so it must not select the
            // greyscale body texture.
            return NBTHelper.hasTag(stack, BagItem.TAG_PRIMARY_COLOR) && NBTHelper.getInt(stack, BagItem.TAG_PRIMARY_COLOR) >= 0;
        }
    },
    LOCKED(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "locked")) {
        @Override
        boolean test(ItemStack stack) {
            return StorageUtil.hasCode(stack);
        }
    };

    private final ResourceLocation id;

    HasStorageTagProperty(ResourceLocation id) {
        this.id = id;
    }

    public ResourceLocation id() {
        return this.id;
    }

    abstract boolean test(ItemStack stack);

    @Override
    public float unclampedCall(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed) {
        return this.test(stack) ? 1.0F : 0.0F;
    }
}
