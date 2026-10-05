package com.grim3212.assorted.crates.common.inventory;

import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.crates.Constants;
import com.grim3212.assorted.crates.common.inventory.crates.CrateCompactingContainer;
import com.grim3212.assorted.crates.common.inventory.crates.CrateContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

public class CratesContainerTypes {
    public static final RegistryProvider<MenuType<?>> CONTAINERS = RegistryProvider.create(Registries.MENU, Constants.MOD_ID);

    public static final IRegistryObject<MenuType<CrateContainer>> CRATE = CONTAINERS.register("crate", () -> Services.PLATFORM.createMenuType(CrateContainer::createCrateContainer, BlockPos.STREAM_CODEC));
    public static final IRegistryObject<MenuType<CrateCompactingContainer>> CRATE_COMPACTING = CONTAINERS.register("crate_compacting", () -> Services.PLATFORM.createMenuType(CrateCompactingContainer::createCrateContainer, BlockPos.STREAM_CODEC));

    public static void init() {

    }
}
