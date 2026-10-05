package com.grim3212.assorted.crates.common.block.blockentity;

import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.crates.Constants;
import com.grim3212.assorted.crates.common.block.CratesBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.HashSet;
import java.util.Set;

public class CratesBlockEntityTypes {
    public static final RegistryProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistryProvider.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);

    public static final IRegistryObject<BlockEntityType<CrateBlockEntity>> CRATE = BLOCK_ENTITIES.register("crate", () -> Services.PLATFORM.createBlockEntityType(CrateBlockEntity::new, getCrates()));
    public static final IRegistryObject<BlockEntityType<CrateCompactingBlockEntity>> CRATE_COMPACTING = BLOCK_ENTITIES.register("crate_compacting", () -> Services.PLATFORM.createBlockEntityType(CrateCompactingBlockEntity::new, CratesBlocks.CRATE_COMPACTING.get()));
    public static final IRegistryObject<BlockEntityType<CrateControllerBlockEntity>> CRATE_CONTROLLER = BLOCK_ENTITIES.register("crate_controller", () -> Services.PLATFORM.createBlockEntityType(CrateControllerBlockEntity::new, CratesBlocks.CRATE_CONTROLLER.get()));

    private static Block[] getCrates() {
        Set<Block> crates = new HashSet<>();
        CratesBlocks.CRATES.forEach(x -> {
            crates.add(x.SINGLE.get());
            crates.add(x.DOUBLE.get());
            crates.add(x.TRIPLE.get());
            crates.add(x.QUADRUPLE.get());
        });
        return crates.toArray(new Block[0]);
    }

    public static void init() {

    }
}
