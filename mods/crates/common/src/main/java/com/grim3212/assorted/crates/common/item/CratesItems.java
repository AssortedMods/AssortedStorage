package com.grim3212.assorted.crates.common.item;

import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.crates.common.block.CratesBlocks;
import com.grim3212.assorted.crates.common.item.upgrades.*;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class CratesItems {

    public static final IRegistryObject<Item> BLANK_UPGRADE = register("blank_upgrade", () -> new Item(props()));

    public static final IRegistryObject<RotatorMajigItem> ROTATOR_MAJIG = register("rotator_majig", () -> new RotatorMajigItem(props()));

    public static final IRegistryObject<VoidUpgradeItem> VOID_UPGRADE = register("void_upgrade", () -> new VoidUpgradeItem(props().stacksTo(16)));
    public static final IRegistryObject<AmountUpgradeItem> AMOUNT_UPGRADE = register("amount_upgrade", () -> new AmountUpgradeItem(props().stacksTo(16)));
    public static final IRegistryObject<RedstoneUpgradeItem> REDSTONE_UPGRADE = register("redstone_upgrade", () -> new RedstoneUpgradeItem(props().stacksTo(16)));
    public static final IRegistryObject<BasicCrateUpgradeItem> GLOW_UPGRADE = register("glow_upgrade", () -> new BasicCrateUpgradeItem(props().stacksTo(16)));

    private static Item.Properties props() {
        return new Item.Properties();
    }

    private static <T extends Item> IRegistryObject<T> register(final String name, final Supplier<T> factory) {
        return CratesBlocks.ITEMS.register(name, factory);
    }

    public static void init() {

    }
}
