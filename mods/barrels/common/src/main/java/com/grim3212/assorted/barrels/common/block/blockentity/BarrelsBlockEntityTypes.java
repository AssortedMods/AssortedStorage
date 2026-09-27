package com.grim3212.assorted.barrels.common.block.blockentity;

import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.barrels.Constants;
import com.grim3212.assorted.barrels.Family;
import com.grim3212.assorted.barrels.common.block.BarrelsBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;
import java.util.stream.Collectors;

public class BarrelsBlockEntityTypes {
    public static final RegistryProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistryProvider.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID).aliasFrom(Family.ID);

    public static final IRegistryObject<BlockEntityType<LockedBarrelBlockEntity>> LOCKED_BARREL = BLOCK_ENTITIES.register("locked_barrel", () -> Services.PLATFORM.createBlockEntityType(LockedBarrelBlockEntity::new, getBarrels()));

    private static Block[] getBarrels() {
        Set<Block> barrels = BarrelsBlocks.BARRELS.values().stream().map((b) -> b.get()).collect(Collectors.toSet());
        barrels.add(BarrelsBlocks.LOCKED_BARREL.get());
        return barrels.toArray(new Block[0]);
    }

    public static void init() {

    }
}
