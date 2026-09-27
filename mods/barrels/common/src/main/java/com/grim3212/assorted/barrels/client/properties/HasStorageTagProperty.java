package com.grim3212.assorted.barrels.client.properties;

import com.grim3212.assorted.lib.core.inventory.locking.StorageUtil;
import com.grim3212.assorted.barrels.Constants;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * What a barrel's item model branches on: whether the stack is locked. The code lives in {@code
 * custom_data}, which no vanilla property can test for an arbitrary value.
 */
public enum HasStorageTagProperty implements ConditionalItemModelProperty {
    LOCKED(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "locked")) {
        @Override
        public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext context) {
            return StorageUtil.hasCode(stack);
        }
    };

    private final Identifier id;
    private final MapCodec<HasStorageTagProperty> codec;

    HasStorageTagProperty(Identifier id) {
        this.id = id;
        this.codec = MapCodec.unit(this);
    }

    public Identifier id() {
        return this.id;
    }

    @Override
    public MapCodec<HasStorageTagProperty> type() {
        return this.codec;
    }
}
