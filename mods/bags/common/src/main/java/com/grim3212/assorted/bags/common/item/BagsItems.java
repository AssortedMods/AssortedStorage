package com.grim3212.assorted.bags.common.item;

import com.google.common.collect.Maps;
import com.grim3212.assorted.bags.Constants;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class BagsItems {

    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);

    public static final IRegistryObject<EnderBagItem> ENDER_BAG = register("ender_bag", key -> new EnderBagItem(props(key)));
    public static final IRegistryObject<BagItem> BAG = register("bag", key -> new BagItem(props(key), null));

    public static final Map<StorageMaterial, IRegistryObject<BagItem>> BAGS = Maps.newEnumMap(StorageMaterial.class);

    static {
        Stream.of(StorageMaterial.values()).forEach((type) -> BAGS.put(type, register("bag_" + type.toString(), key -> new BagItem(props(key), type))));
    }

    // Since 1.21.2 an item has to know its own registry id before it is constructed, so the
    // registration name is turned into a ResourceKey here and put on the properties. Without it
    // registration dies with "Item id not set".
    private static Item.Properties props(ResourceKey<Item> key) {
        return new Item.Properties().setId(key);
    }

    private static <T extends Item> IRegistryObject<T> register(final String name, final Function<ResourceKey<Item>, T> factory) {
        final ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        return ITEMS.register(name, () -> factory.apply(key));
    }

    public static void init() {

    }
}
