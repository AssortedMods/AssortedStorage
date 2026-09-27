package com.grim3212.assorted.chests.common.block.blockentity;

import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.chests.Constants;
import com.grim3212.assorted.chests.Family;
import com.grim3212.assorted.chests.common.block.ChestsBlocks;
import com.grim3212.assorted.lib.core.storage.chest.LockedChestBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;


public class ChestsBlockEntityTypes {
    public static final RegistryProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistryProvider.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID).aliasFrom(Family.ID);

    public static final IRegistryObject<BlockEntityType<LockedChestBlockEntity>> LOCKED_CHEST = BLOCK_ENTITIES.register("locked_chest", () -> Services.PLATFORM.createBlockEntityType(LockedChestBlockEntity::new, getChests()));

    public static Block[] getChests() {
        return ChestsBlocks.CHESTS.values().stream().map(IRegistryObject::get).toArray(Block[]::new);
    }

    public static void init() {

    }
}
