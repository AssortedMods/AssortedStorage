package com.grim3212.assorted.containers.data;

import com.grim3212.assorted.containers.common.block.ContainersBlocks;
import com.grim3212.assorted.containers.common.block.WarehouseCrateBlock;
import com.grim3212.assorted.lib.data.LibBlockLootProvider;
import com.grim3212.assorted.lib.registry.IRegistryObject;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ContainersBlockLoot extends LibBlockLootProvider {

    private final List<Block> blocks = new ArrayList<>();

    public ContainersBlockLoot(HolderLookup.Provider registries) {
        super(registries, () -> ContainersBlocks.BLOCKS.getEntries().stream().map(Supplier::get).collect(Collectors.toList()));

        blocks.add(ContainersBlocks.WOOD_CABINET.get());
        blocks.add(ContainersBlocks.GLASS_CABINET.get());
        blocks.add(ContainersBlocks.OBSIDIAN_SAFE.get());
        blocks.add(ContainersBlocks.LOCKER.get());
        blocks.add(ContainersBlocks.ITEM_TOWER.get());

        for (IRegistryObject<WarehouseCrateBlock> b : ContainersBlocks.WAREHOUSE_CRATES.values()) {
            blocks.add(b.get());
        }
    }

    @Override
    public void generate() {
        for (Block b : blocks) {
            this.dropSelf(b);
        }

        this.add(ContainersBlocks.GOLD_SAFE.get(), createGoldSafeTable(ContainersBlocks.GOLD_SAFE.get()));
    }

    /**
     * The safe drops its padlock separately (the block entity does that on removal), so the item
     * itself only carries the name and the contents, not the lock.
     */
    private LootTable.Builder createGoldSafeTable(Block b) {
        LootPoolEntryContainer.Builder<?> entry = LootItem.lootTableItem(b).apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY).include(DataComponents.CUSTOM_NAME).include(DataComponents.CONTAINER));
        LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry).when(ExplosionCondition.survivesExplosion());
        return LootTable.lootTable().withPool(pool);
    }
}
