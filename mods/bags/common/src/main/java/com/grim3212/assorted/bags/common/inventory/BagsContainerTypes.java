package com.grim3212.assorted.bags.common.inventory;

import com.grim3212.assorted.bags.Constants;
import com.grim3212.assorted.bags.common.inventory.bag.BagContainer;
import com.grim3212.assorted.bags.common.inventory.enderbag.EnderBagContainer;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

public class BagsContainerTypes {
    public static final RegistryProvider<MenuType<?>> CONTAINERS = RegistryProvider.create(Registries.MENU, Constants.MOD_ID);

    public static final IRegistryObject<MenuType<BagContainer>> BAG = CONTAINERS.register("bag", () -> Services.PLATFORM.createMenuType(BagContainer::new));
    public static final IRegistryObject<MenuType<EnderBagContainer>> ENDER_BAG = CONTAINERS.register("ender_bag", () -> Services.PLATFORM.createMenuType(EnderBagContainer::new));

    public static void init() {

    }
}
