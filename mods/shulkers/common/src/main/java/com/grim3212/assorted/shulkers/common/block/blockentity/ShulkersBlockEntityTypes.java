package com.grim3212.assorted.shulkers.common.block.blockentity;

import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.shulkers.Constants;
import com.grim3212.assorted.shulkers.Family;
import com.grim3212.assorted.shulkers.common.block.ShulkersBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;
import java.util.stream.Collectors;

public class ShulkersBlockEntityTypes {
    public static final RegistryProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistryProvider.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID).aliasFrom(Family.ID);

    public static final IRegistryObject<BlockEntityType<LockedShulkerBoxBlockEntity>> LOCKED_SHULKER_BOX = BLOCK_ENTITIES.register("locked_shulker_box", () -> Services.PLATFORM.createBlockEntityType(LockedShulkerBoxBlockEntity::new, getShulkers()));

    public static Block[] getShulkers() {
        Set<Block> shulkers = ShulkersBlocks.SHULKERS.values().stream().map((b) -> b.get()).collect(Collectors.toSet());
        shulkers.add(ShulkersBlocks.LOCKED_SHULKER_BOX.get());
        return shulkers.toArray(new Block[0]);
    }

    public static void init() {

    }
}
