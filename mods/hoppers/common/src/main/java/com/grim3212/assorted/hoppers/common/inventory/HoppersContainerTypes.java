package com.grim3212.assorted.hoppers.common.inventory;

import com.grim3212.assorted.hoppers.Constants;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.core.storage.hopper.LockedHopperContainer;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

public class HoppersContainerTypes {
    public static final RegistryProvider<MenuType<?>> CONTAINERS = RegistryProvider.create(Registries.MENU, Constants.MOD_ID);

    public static final IRegistryObject<MenuType<LockedHopperContainer>> LOCKED_HOPPER = CONTAINERS.register("locked_hopper", () -> Services.PLATFORM.createMenuType((windowId, playerInventory, material) -> LockedHopperContainer.createClient(HoppersContainerTypes.LOCKED_HOPPER.get(), windowId, playerInventory, material), StorageMaterial.OPTIONAL_STREAM_CODEC));

    public static void init() {

    }
}
