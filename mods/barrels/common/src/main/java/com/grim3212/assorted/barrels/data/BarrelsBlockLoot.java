package com.grim3212.assorted.barrels.data;

import com.grim3212.assorted.lib.data.LibBlockLootProvider;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.barrels.common.block.*;
import com.grim3212.assorted.lib.core.storage.barrel.LockedBarrelBlock;
import net.minecraft.data.loot.LootTableSubProvider;

import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BarrelsBlockLoot extends LibBlockLootProvider {

    public BarrelsBlockLoot(LootTableSubProvider.Context context) {
        super(context, () -> BarrelsBlocks.BLOCKS.getEntries().stream().map(Supplier::get).collect(Collectors.toList()));
    }

    @Override
    public void generate() {
        for (IRegistryObject<LockedBarrelBlock> b : BarrelsBlocks.BARRELS.values()) {
            this.dropSelf(b.get());
        }
    }
}
