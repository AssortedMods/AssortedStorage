package com.grim3212.assorted.chests.common.block.blockentity;

import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.chests.Constants;
import com.grim3212.assorted.chests.Family;
import com.grim3212.assorted.chests.common.block.ChestsBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;
import java.util.stream.Collectors;

public class ChestsBlockEntityTypes {
    public static final RegistryProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistryProvider.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID).aliasFrom(Family.ID);

    public static final IRegistryObject<BlockEntityType<LockedEnderChestBlockEntity>> LOCKED_ENDER_CHEST = BLOCK_ENTITIES.register("locked_ender_chest", () -> Services.PLATFORM.createBlockEntityType(LockedEnderChestBlockEntity::new, ChestsBlocks.LOCKED_ENDER_CHEST.get()));

    public static final IRegistryObject<BlockEntityType<LockedChestBlockEntity>> LOCKED_CHEST = BLOCK_ENTITIES.register("locked_chest", () -> Services.PLATFORM.createBlockEntityType(LockedChestBlockEntity::new, getChests()));

    public static Block[] getChests() {
        Set<Block> chests = ChestsBlocks.CHESTS.values().stream().map((b) -> b.get()).collect(Collectors.toSet());
        chests.add(ChestsBlocks.LOCKED_CHEST.get());
        return chests.toArray(new Block[0]);
    }

    public static void init() {

    }
}
