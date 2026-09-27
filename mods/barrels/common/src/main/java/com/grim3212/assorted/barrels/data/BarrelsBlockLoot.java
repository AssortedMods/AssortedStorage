package com.grim3212.assorted.barrels.data;

import com.grim3212.assorted.lib.data.LibBlockLootProvider;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.barrels.common.block.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BarrelsBlockLoot extends LibBlockLootProvider {

    public BarrelsBlockLoot(HolderLookup.Provider registries) {
        super(registries, () -> BarrelsBlocks.BLOCKS.getEntries().stream().map(Supplier::get).collect(Collectors.toList()));
    }

    @Override
    public void generate() {
        for (IRegistryObject<LockedBarrelBlock> b : BarrelsBlocks.BARRELS.values()) {
            this.dropSelf(b.get());
        }
        this.dropOther(BarrelsBlocks.LOCKED_BARREL.get(), Blocks.BARREL);
    }
}
