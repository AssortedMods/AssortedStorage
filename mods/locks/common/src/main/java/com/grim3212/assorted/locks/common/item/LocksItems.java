package com.grim3212.assorted.locks.common.item;

import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.locks.Constants;
import com.grim3212.assorted.locks.common.block.LocksBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class LocksItems {

    public static final IRegistryObject<PadlockItem> LOCKSMITH_LOCK = register("locksmith_lock", key -> new PadlockItem(props(key)));
    public static final IRegistryObject<CombinationItem> LOCKSMITH_KEY = register("locksmith_key", key -> new CombinationItem(props(key)));
    public static final IRegistryObject<KeyRingItem> KEY_RING = register("key_ring", key -> new KeyRingItem(props(key)));

    // Since 1.21.2 an item has to know its own registry id before it is constructed, so the
    // registration name is turned into a ResourceKey here and put on the properties. Without it
    // registration dies with "Item id not set"; LocksBlocks already does the same for blocks.
    private static Item.Properties props(ResourceKey<Item> key) {
        return new Item.Properties().setId(key);
    }

    private static <T extends Item> IRegistryObject<T> register(final String name, final Function<ResourceKey<Item>, T> factory) {
        final ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        return LocksBlocks.ITEMS.register(name, () -> factory.apply(key));
    }

    public static void init() {

    }
}
