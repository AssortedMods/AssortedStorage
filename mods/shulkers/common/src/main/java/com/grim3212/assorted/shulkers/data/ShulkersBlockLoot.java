package com.grim3212.assorted.shulkers.data;

import com.grim3212.assorted.lib.data.LibBlockLootProvider;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.shulkers.common.block.LockedShulkerBoxBlock;
import com.grim3212.assorted.shulkers.common.block.ShulkersBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.block.Block;
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

public class ShulkersBlockLoot extends LibBlockLootProvider {

    public ShulkersBlockLoot(HolderLookup.Provider registries) {
        super(registries, () -> ShulkersBlocks.BLOCKS.getEntries().stream().map(Supplier::get).collect(Collectors.toList()));
    }

    @Override
    public void generate() {
        this.add(ShulkersBlocks.LOCKED_SHULKER_BOX.get(), createLockedShulkerTable(ShulkersBlocks.LOCKED_SHULKER_BOX.get()));
        for (IRegistryObject<LockedShulkerBoxBlock> b : ShulkersBlocks.SHULKERS.values()) {
            this.add(b.get(), createLockedShulkerTable(b.get()));
        }
    }

    /**
     * Drops the block with its contents, name and lock carried over as data components, as
     * vanilla's shulker box does.
     */
    private LootTable.Builder createLockedShulkerTable(Block b) {
        LootPoolEntryContainer.Builder<?> entry = LootItem.lootTableItem(b).apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY).include(DataComponents.CUSTOM_NAME).include(DataComponents.CONTAINER).include(DataComponents.CUSTOM_DATA));
        LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry).when(ExplosionCondition.survivesExplosion());
        return LootTable.lootTable().withPool(pool);
    }
}
