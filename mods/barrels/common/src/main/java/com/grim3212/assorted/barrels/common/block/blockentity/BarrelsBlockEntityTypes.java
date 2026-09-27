package com.grim3212.assorted.barrels.common.block.blockentity;

import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.barrels.Constants;
import com.grim3212.assorted.barrels.Family;
import com.grim3212.assorted.barrels.common.block.BarrelsBlocks;
import com.grim3212.assorted.lib.core.storage.barrel.LockedBarrelBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;


public class BarrelsBlockEntityTypes {
    public static final RegistryProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistryProvider.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID).aliasFrom(Family.ID);

    public static final IRegistryObject<BlockEntityType<LockedBarrelBlockEntity>> LOCKED_BARREL = BLOCK_ENTITIES.register("locked_barrel", () -> Services.PLATFORM.createBlockEntityType(LockedBarrelBlockEntity::new, getBarrels()));

    private static Block[] getBarrels() {
        return BarrelsBlocks.BARRELS.values().stream().map(IRegistryObject::get).toArray(Block[]::new);
    }

    public static void init() {

    }
}
