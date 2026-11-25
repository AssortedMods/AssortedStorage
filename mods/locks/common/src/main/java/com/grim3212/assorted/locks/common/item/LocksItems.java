package com.grim3212.assorted.locks.common.item;

import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.locks.common.block.LocksBlocks;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class LocksItems {

    public static final IRegistryObject<PadlockItem> LOCKSMITH_LOCK = register("locksmith_lock", () -> new PadlockItem(new Item.Properties()));
    public static final IRegistryObject<CombinationItem> LOCKSMITH_KEY = register("locksmith_key", () -> new CombinationItem(new Item.Properties()));
    public static final IRegistryObject<KeyRingItem> KEY_RING = register("key_ring", () -> new KeyRingItem(new Item.Properties()));

    private static <T extends Item> IRegistryObject<T> register(final String name, final Supplier<T> factory) {
        return LocksBlocks.ITEMS.register(name, factory);
    }

    public static void init() {

    }
}
