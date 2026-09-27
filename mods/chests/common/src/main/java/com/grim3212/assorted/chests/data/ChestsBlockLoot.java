package com.grim3212.assorted.chests.data;

import com.grim3212.assorted.lib.data.LibBlockLootProvider;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.chests.common.block.*;
import com.grim3212.assorted.lib.core.storage.chest.LockedChestBlock;
import net.minecraft.core.HolderLookup;

import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ChestsBlockLoot extends LibBlockLootProvider {

    public ChestsBlockLoot(HolderLookup.Provider registries) {
        super(registries, () -> ChestsBlocks.BLOCKS.getEntries().stream().map(Supplier::get).collect(Collectors.toList()));
    }

    @Override
    public void generate() {
        for (IRegistryObject<LockedChestBlock> b : ChestsBlocks.CHESTS.values()) {
            this.dropSelf(b.get());
        }
    }
}
