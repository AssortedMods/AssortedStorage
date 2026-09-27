package com.grim3212.assorted.barrels.common.inventory;

import com.grim3212.assorted.lib.core.storage.LockedMaterialContainer;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.barrels.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;

public class BarrelsContainerTypes {
    public static final RegistryProvider<MenuType<?>> CONTAINERS = RegistryProvider.create(Registries.MENU, Constants.MOD_ID);

    public static final IRegistryObject<MenuType<LockedMaterialContainer>> LOCKED_BARREL = CONTAINERS.register("locked_barrel", () -> Services.PLATFORM.createMenuType(BarrelsMenus::createBarrelContainer, StorageMaterial.OPTIONAL_STREAM_CODEC));

    public static void init() {

    }
}
