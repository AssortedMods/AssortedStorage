package com.grim3212.assorted.chests.data;

import com.grim3212.assorted.lib.data.LibBlockLootProvider;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.chests.common.block.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

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
        this.dropOther(ChestsBlocks.LOCKED_CHEST.get(), Blocks.CHEST);

        this.add(ChestsBlocks.LOCKED_ENDER_CHEST.get(), createInventoryCodeTable(ChestsBlocks.LOCKED_ENDER_CHEST.get()));
    }

    private LootTable.Builder createInventoryCodeTable(Block b) {
        LootPoolEntryContainer.Builder<?> entry = LootItem.lootTableItem(b).apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY).include(DataComponents.CUSTOM_DATA));
        LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry).when(ExplosionCondition.survivesExplosion());
        return LootTable.lootTable().withPool(pool);
    }
}
