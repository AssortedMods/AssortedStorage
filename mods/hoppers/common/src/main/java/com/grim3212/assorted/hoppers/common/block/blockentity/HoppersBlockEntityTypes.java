package com.grim3212.assorted.hoppers.common.block.blockentity;

import com.grim3212.assorted.hoppers.Constants;
import com.grim3212.assorted.hoppers.Family;
import com.grim3212.assorted.hoppers.common.block.HoppersBlocks;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;
import java.util.stream.Collectors;

public class HoppersBlockEntityTypes {
    public static final RegistryProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistryProvider.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID).aliasFrom(Family.ID);

    public static final IRegistryObject<BlockEntityType<LockedHopperBlockEntity>> LOCKED_HOPPER = BLOCK_ENTITIES.register("locked_hopper", () -> Services.PLATFORM.createBlockEntityType(LockedHopperBlockEntity::new, getHoppers()));

    private static Block[] getHoppers() {
        Set<Block> hoppers = HoppersBlocks.HOPPERS.values().stream().map((b) -> b.get()).collect(Collectors.toSet());
        hoppers.add(HoppersBlocks.LOCKED_HOPPER.get());
        return hoppers.toArray(new Block[0]);
    }

    public static void init() {

    }
}
