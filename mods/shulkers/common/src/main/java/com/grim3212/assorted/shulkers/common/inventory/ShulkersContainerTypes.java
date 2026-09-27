package com.grim3212.assorted.shulkers.common.inventory;

import com.grim3212.assorted.lib.core.storage.LockedMaterialContainer;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.shulkers.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

public class ShulkersContainerTypes {
    public static final RegistryProvider<MenuType<?>> CONTAINERS = RegistryProvider.create(Registries.MENU, Constants.MOD_ID);

    public static final IRegistryObject<MenuType<LockedMaterialContainer>> LOCKED_SHULKER_BOX = CONTAINERS.register("locked_shulker_box", () -> Services.PLATFORM.createMenuType(ShulkersMenus::createShulkerContainer, StorageMaterial.OPTIONAL_STREAM_CODEC));

    public static void init() {

    }
}
