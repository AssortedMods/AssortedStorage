package com.grim3212.assorted.hoppers.data;

import com.grim3212.assorted.hoppers.common.block.HoppersBlocks;
import com.grim3212.assorted.lib.core.storage.hopper.LockedHopperBlock;
import com.grim3212.assorted.lib.data.LibBlockLootProvider;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import net.minecraft.data.loot.LootTableSubProvider;

import java.util.function.Supplier;
import java.util.stream.Collectors;

public class HoppersBlockLoot extends LibBlockLootProvider {

    public HoppersBlockLoot(LootTableSubProvider.Context context) {
        super(context, () -> HoppersBlocks.BLOCKS.getEntries().stream().map(Supplier::get).collect(Collectors.toList()));
    }

    @Override
    public void generate() {
        for (IRegistryObject<LockedHopperBlock> b : HoppersBlocks.HOPPERS.values()) {
            this.dropSelf(b.get());
        }
    }
}
