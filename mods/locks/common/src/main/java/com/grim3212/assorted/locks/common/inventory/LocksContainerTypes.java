package com.grim3212.assorted.locks.common.inventory;

import com.grim3212.assorted.lib.core.inventory.impl.ItemStackStorageHandler;
import com.grim3212.assorted.lib.core.storage.LockedMaterialContainer;
import com.grim3212.assorted.lib.core.storage.StorageContainer;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.core.storage.hopper.LockedHopperContainer;
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

    public static final IRegistryObject<MenuType<StorageContainer>> LOCKED_ENDER_CHEST = CONTAINERS.register("locked_ender_chest", () -> Services.PLATFORM.createMenuType((windowId, playerInventory) -> new StorageContainer(LocksContainerTypes.LOCKED_ENDER_CHEST.get(), windowId, playerInventory, new ItemStackStorageHandler(27))));
    public static final IRegistryObject<MenuType<LockedMaterialContainer>> LOCKED_CHEST = CONTAINERS.register("locked_chest", () -> Services.PLATFORM.createMenuType((windowId, playerInventory, material) -> LockedMaterialContainer.createClient(LocksContainerTypes.LOCKED_CHEST.get(), windowId, playerInventory, material, false), StorageMaterial.OPTIONAL_STREAM_CODEC));
    public static final IRegistryObject<MenuType<LockedMaterialContainer>> LOCKED_BARREL = CONTAINERS.register("locked_barrel", () -> Services.PLATFORM.createMenuType((windowId, playerInventory, material) -> LockedMaterialContainer.createClient(LocksContainerTypes.LOCKED_BARREL.get(), windowId, playerInventory, material, false), StorageMaterial.OPTIONAL_STREAM_CODEC));
    public static final IRegistryObject<MenuType<LockedHopperContainer>> LOCKED_HOPPER = CONTAINERS.register("locked_hopper", () -> Services.PLATFORM.createMenuType((windowId, playerInventory, material) -> LockedHopperContainer.createClient(LocksContainerTypes.LOCKED_HOPPER.get(), windowId, playerInventory, material), StorageMaterial.OPTIONAL_STREAM_CODEC));
    public static final IRegistryObject<MenuType<LockedMaterialContainer>> LOCKED_SHULKER_BOX = CONTAINERS.register("locked_shulker_box", () -> Services.PLATFORM.createMenuType((windowId, playerInventory, material) -> LockedMaterialContainer.createClient(LocksContainerTypes.LOCKED_SHULKER_BOX.get(), windowId, playerInventory, material, true), StorageMaterial.OPTIONAL_STREAM_CODEC));

    public static void init() {

    }
}
