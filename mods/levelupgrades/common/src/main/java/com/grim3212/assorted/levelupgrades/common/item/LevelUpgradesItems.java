package com.grim3212.assorted.levelupgrades.common.item;

import com.google.common.collect.Maps;
import com.grim3212.assorted.levelupgrades.Constants;
import com.grim3212.assorted.levelupgrades.common.item.upgrades.LevelUpgradeItem;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class LevelUpgradesItems {

    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);

    public static final Map<StorageMaterial, IRegistryObject<LevelUpgradeItem>> LEVEL_UPGRADES = Maps.newEnumMap(StorageMaterial.class);

    static {
        Stream.of(StorageMaterial.values()).forEach((type) -> LEVEL_UPGRADES.put(type, register("level_upgrade_" + type.toString(), props -> new LevelUpgradeItem(props, type))));
    }

    private static <T extends Item> IRegistryObject<T> register(final String name, final Function<Item.Properties, T> factory) {
        return ITEMS.register(name, () -> factory.apply(new Item.Properties()));
    }

    public static void init() {

    }
}
