package com.grim3212.assorted.crates.common.item;

import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.crates.Constants;
import com.grim3212.assorted.crates.common.block.CratesBlocks;
import com.grim3212.assorted.crates.common.item.upgrades.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class CratesItems {

    public static final IRegistryObject<Item> BLANK_UPGRADE = register("blank_upgrade", key -> new Item(props(key)));

    public static final IRegistryObject<RotatorMajigItem> ROTATOR_MAJIG = register("rotator_majig", key -> new RotatorMajigItem(props(key)));

    public static final IRegistryObject<VoidUpgradeItem> VOID_UPGRADE = register("void_upgrade", key -> new VoidUpgradeItem(props(key).stacksTo(16)));
    public static final IRegistryObject<AmountUpgradeItem> AMOUNT_UPGRADE = register("amount_upgrade", key -> new AmountUpgradeItem(props(key).stacksTo(16)));
    public static final IRegistryObject<RedstoneUpgradeItem> REDSTONE_UPGRADE = register("redstone_upgrade", key -> new RedstoneUpgradeItem(props(key).stacksTo(16)));
    public static final IRegistryObject<BasicCrateUpgradeItem> GLOW_UPGRADE = register("glow_upgrade", key -> new BasicCrateUpgradeItem(props(key).stacksTo(16)));

    // Since 1.21.2 an item has to know its own registry id before it is constructed, so the
    // registration name is turned into a ResourceKey here and put on the properties. Without it
    // registration dies with "Item id not set"; CratesBlocks already does the same for blocks.
    private static Item.Properties props(ResourceKey<Item> key) {
        return new Item.Properties().setId(key);
    }

    private static <T extends Item> IRegistryObject<T> register(final String name, final Function<ResourceKey<Item>, T> factory) {
        final ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        return CratesBlocks.ITEMS.register(name, () -> factory.apply(key));
    }

    public static void init() {

    }
}
