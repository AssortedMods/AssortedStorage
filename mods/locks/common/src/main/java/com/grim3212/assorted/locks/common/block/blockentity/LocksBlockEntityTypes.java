package com.grim3212.assorted.locks.common.block.blockentity;

import com.grim3212.assorted.lib.core.inventory.locking.BaseLockedBlockEntity;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.locks.Constants;
import com.grim3212.assorted.locks.Family;
import com.grim3212.assorted.locks.common.block.LocksBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class LocksBlockEntityTypes {
    public static final RegistryProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistryProvider.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID).aliasFrom(Family.ID);

    public static final IRegistryObject<BlockEntityType<BaseLockedBlockEntity>> BASE_LOCKED = BLOCK_ENTITIES.register("base_locked", () -> Services.PLATFORM.createBlockEntityType((pos, state) -> new BaseLockedBlockEntity(LocksBlockEntityTypes.BASE_LOCKED.get(), pos, state), LocksBlocks.lockedDoors()));

    public static void init() {

    }
}
