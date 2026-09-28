package com.grim3212.assorted.containers.common.block.blockentity;

import com.grim3212.assorted.containers.Constants;
import com.grim3212.assorted.containers.common.block.ContainersBlocks;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ContainersBlockEntityTypes {
    public static final RegistryProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistryProvider.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);

    public static final IRegistryObject<BlockEntityType<WoodCabinetBlockEntity>> WOOD_CABINET = BLOCK_ENTITIES.register("wood_cabinet", () -> Services.PLATFORM.createBlockEntityType(WoodCabinetBlockEntity::new, ContainersBlocks.WOOD_CABINET.get()));
    public static final IRegistryObject<BlockEntityType<GlassCabinetBlockEntity>> GLASS_CABINET = BLOCK_ENTITIES.register("glass_cabinet", () -> Services.PLATFORM.createBlockEntityType(GlassCabinetBlockEntity::new, ContainersBlocks.GLASS_CABINET.get()));
    public static final IRegistryObject<BlockEntityType<GoldSafeBlockEntity>> GOLD_SAFE = BLOCK_ENTITIES.register("gold_safe", () -> Services.PLATFORM.createBlockEntityType(GoldSafeBlockEntity::new, ContainersBlocks.GOLD_SAFE.get()));
    public static final IRegistryObject<BlockEntityType<ObsidianSafeBlockEntity>> OBSIDIAN_SAFE = BLOCK_ENTITIES.register("obsidian_safe", () -> Services.PLATFORM.createBlockEntityType(ObsidianSafeBlockEntity::new, ContainersBlocks.OBSIDIAN_SAFE.get()));
    public static final IRegistryObject<BlockEntityType<LockerBlockEntity>> LOCKER = BLOCK_ENTITIES.register("locker", () -> Services.PLATFORM.createBlockEntityType(LockerBlockEntity::new, ContainersBlocks.LOCKER.get()));
    public static final IRegistryObject<BlockEntityType<ItemTowerBlockEntity>> ITEM_TOWER = BLOCK_ENTITIES.register("item_tower", () -> Services.PLATFORM.createBlockEntityType(ItemTowerBlockEntity::new, ContainersBlocks.ITEM_TOWER.get()));

    public static final IRegistryObject<BlockEntityType<WarehouseCrateBlockEntity>> WAREHOUSE_CRATE = BLOCK_ENTITIES.register("warehouse_crate", () -> Services.PLATFORM.createBlockEntityType(WarehouseCrateBlockEntity::new, getWarehouseCrates()));

    public static Block[] getWarehouseCrates() {
        return ContainersBlocks.WAREHOUSE_CRATES.values().stream().map(IRegistryObject::get).toArray(Block[]::new);
    }

    public static void init() {

    }
}
