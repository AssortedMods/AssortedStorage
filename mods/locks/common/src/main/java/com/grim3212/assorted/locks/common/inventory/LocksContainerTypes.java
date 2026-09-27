package com.grim3212.assorted.locks.common.inventory;

import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.locks.Constants;
import com.grim3212.assorted.locks.common.inventory.keyring.KeyRingContainer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

public class LocksContainerTypes {
    public static final RegistryProvider<MenuType<?>> CONTAINERS = RegistryProvider.create(Registries.MENU, Constants.MOD_ID);

    public static final IRegistryObject<MenuType<LocksmithWorkbenchContainer>> LOCKSMITH_WORKBENCH = CONTAINERS.register("locksmith_workbench", () -> Services.PLATFORM.createMenuType(LocksmithWorkbenchContainer::createContainer));
    public static final IRegistryObject<MenuType<KeyRingContainer>> KEY_RING = CONTAINERS.register("key_ring", () -> Services.PLATFORM.createMenuType(KeyRingContainer::new));

    public static void init() {

    }
}
