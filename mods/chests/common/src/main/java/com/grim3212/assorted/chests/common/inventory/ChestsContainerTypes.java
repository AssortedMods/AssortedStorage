package com.grim3212.assorted.chests.common.inventory;

import com.grim3212.assorted.lib.core.storage.LockedMaterialContainer;
import com.grim3212.assorted.lib.core.storage.StorageContainer;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.chests.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;

public class ChestsContainerTypes {
    public static final RegistryProvider<MenuType<?>> CONTAINERS = RegistryProvider.create(Registries.MENU, Constants.MOD_ID);

    public static final IRegistryObject<MenuType<StorageContainer>> LOCKED_ENDER_CHEST = CONTAINERS.register("locked_ender_chest", () -> Services.PLATFORM.createMenuType(ChestsMenus::createEnderChestContainer));
    public static final IRegistryObject<MenuType<LockedMaterialContainer>> LOCKED_CHEST = CONTAINERS.register("locked_chest", () -> Services.PLATFORM.createMenuType(ChestsMenus::createChestContainer, StorageMaterial.OPTIONAL_STREAM_CODEC));

    public static void init() {

    }
}
