package com.grim3212.assorted.crates.data;

import com.grim3212.assorted.lib.data.LibBlockLootProvider;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.crates.common.block.CratesBlocks;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CratesBlockLoot extends LibBlockLootProvider {

    public CratesBlockLoot(LootTableSubProvider.Context context) {
        super(context, () -> CratesBlocks.BLOCKS.getEntries().stream().map(Supplier::get).collect(Collectors.toList()));
    }

    @Override
    public void generate() {
        for (IRegistryObject<Block> block : CratesBlocks.BLOCKS.getEntries()) {
            this.dropSelf(block.get());
        }
    }
}
