package com.grim3212.assorted.chests.common.inventory;

import com.grim3212.assorted.chests.Constants;
import com.grim3212.assorted.lib.core.storage.LockedMaterialContainer;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

public class ChestsContainerTypes {
    public static final RegistryProvider<MenuType<?>> CONTAINERS = RegistryProvider.create(Registries.MENU, Constants.MOD_ID);

    public static final IRegistryObject<MenuType<LockedMaterialContainer>> LOCKED_CHEST = CONTAINERS.register("locked_chest", () -> Services.PLATFORM.createMenuType((windowId, playerInventory, material) -> LockedMaterialContainer.createClient(ChestsContainerTypes.LOCKED_CHEST.get(), windowId, playerInventory, material, false), StorageMaterial.OPTIONAL_STREAM_CODEC));

    public static void init() {

    }
}
