package com.grim3212.assorted.containers.common.inventory;

import com.grim3212.assorted.containers.Constants;
import com.grim3212.assorted.lib.core.storage.StorageContainer;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

public class ContainersContainerTypes {
    public static final RegistryProvider<MenuType<?>> CONTAINERS = RegistryProvider.create(Registries.MENU, Constants.MOD_ID);

    public static final IRegistryObject<MenuType<StorageContainer>> WOOD_CABINET = CONTAINERS.register("wood_cabinet", () -> Services.PLATFORM.createMenuType(ContainersMenus::createWoodCabinetContainer));
    public static final IRegistryObject<MenuType<StorageContainer>> GLASS_CABINET = CONTAINERS.register("glass_cabinet", () -> Services.PLATFORM.createMenuType(ContainersMenus::createGlassCabinetContainer));
    public static final IRegistryObject<MenuType<StorageContainer>> WAREHOUSE_CRATE = CONTAINERS.register("warehouse_crate", () -> Services.PLATFORM.createMenuType(ContainersMenus::createWarehouseCrateContainer));
    public static final IRegistryObject<MenuType<StorageContainer>> GOLD_SAFE = CONTAINERS.register("gold_safe", () -> Services.PLATFORM.createMenuType(ContainersMenus::createGoldSafeContainer));
    public static final IRegistryObject<MenuType<StorageContainer>> OBSIDIAN_SAFE = CONTAINERS.register("obsidian_safe", () -> Services.PLATFORM.createMenuType(ContainersMenus::createObsidianSafeContainer));
    public static final IRegistryObject<MenuType<LockerContainer>> LOCKER = CONTAINERS.register("locker", () -> Services.PLATFORM.createMenuType(LockerContainer::createLockerContainer));
    public static final IRegistryObject<MenuType<LockerContainer>> DUAL_LOCKER = CONTAINERS.register("dual_locker", () -> Services.PLATFORM.createMenuType(LockerContainer::createDualLockerContainer));
    public static final IRegistryObject<MenuType<ItemTowerContainer>> ITEM_TOWER = CONTAINERS.register("item_tower", () -> Services.PLATFORM.createMenuType(ItemTowerContainer::create, BlockPos.STREAM_CODEC));

    public static void init() {

    }
}
