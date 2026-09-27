package com.grim3212.assorted.hoppers.common.block.blockentity;

import com.grim3212.assorted.hoppers.Constants;
import com.grim3212.assorted.hoppers.Family;
import com.grim3212.assorted.hoppers.common.block.HoppersBlocks;
import com.grim3212.assorted.lib.core.storage.hopper.LockedHopperBlockEntity;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;


public class HoppersBlockEntityTypes {
    public static final RegistryProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistryProvider.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID).aliasFrom(Family.ID);

    public static final IRegistryObject<BlockEntityType<LockedHopperBlockEntity>> LOCKED_HOPPER = BLOCK_ENTITIES.register("locked_hopper", () -> Services.PLATFORM.createBlockEntityType(LockedHopperBlockEntity::new, getHoppers()));

    private static Block[] getHoppers() {
        return HoppersBlocks.HOPPERS.values().stream().map(IRegistryObject::get).toArray(Block[]::new);
    }

    public static void init() {

    }
}
