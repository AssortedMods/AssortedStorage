package com.grim3212.assorted.levelupgrades.common.item;

import com.google.common.collect.Maps;
import com.grim3212.assorted.levelupgrades.Constants;
import com.grim3212.assorted.levelupgrades.Family;
import com.grim3212.assorted.levelupgrades.common.item.upgrades.LevelUpgradeItem;
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

public class LevelUpgradesItems {

    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Family.ID);

    public static final Map<StorageMaterial, IRegistryObject<LevelUpgradeItem>> LEVEL_UPGRADES = Maps.newEnumMap(StorageMaterial.class);

    static {
        Stream.of(StorageMaterial.values()).forEach((type) -> LEVEL_UPGRADES.put(type, register("level_upgrade_" + type.toString(), key -> new LevelUpgradeItem(props(key), type))));
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
