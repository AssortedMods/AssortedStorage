package com.grim3212.assorted.locks.data;

import com.grim3212.assorted.lib.data.LibBlockLootProvider;
import com.grim3212.assorted.locks.common.block.LocksBlocks;
import com.grim3212.assorted.locks.common.loot.ModLoadedLootCondition;
import com.grim3212.assorted.locks.common.loot.OptionalLootItem;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.function.Supplier;
import java.util.stream.Collectors;

public class LocksBlockLoot extends LibBlockLootProvider {

    public LocksBlockLoot(HolderLookup.Provider registries) {
        super(registries, () -> LocksBlocks.BLOCKS.getEntries().stream().map(Supplier::get).collect(Collectors.toList()));
    }

    @Override
    public void generate() {
        this.dropSelf(LocksBlocks.LOCKSMITH_WORKBENCH.get());

        // A locked container drops the vanilla one it stands in for, and the padlock drops beside it.
        this.dropOther(LocksBlocks.LOCKED_CHEST.get(), Blocks.CHEST);
        this.dropOther(LocksBlocks.LOCKED_BARREL.get(), Blocks.BARREL);
        this.dropOther(LocksBlocks.LOCKED_HOPPER.get(), Blocks.HOPPER);
        this.add(LocksBlocks.LOCKED_ENDER_CHEST.get(), createInventoryCodeTable(LocksBlocks.LOCKED_ENDER_CHEST.get()));
        this.add(LocksBlocks.LOCKED_SHULKER_BOX.get(), createLockedShulkerTable(LocksBlocks.LOCKED_SHULKER_BOX.get()));

        // Every locked door drops the vanilla door it stands in for, the one the padlock was put on.
        LocksBlocks.VANILLA_DOORS.forEach((door, locked) -> this.add(locked.get(), createLockedDoorTable(locked.get(), door)));

        this.add(LocksBlocks.LOCKED_STEEL_DOOR.get(), createBuildingBlocksTable(LocksBlocks.LOCKED_STEEL_DOOR.get(), LocksBlocks.STEEL_DOOR));
        this.add(LocksBlocks.LOCKED_CHAIN_LINK_DOOR.get(), createBuildingBlocksTable(LocksBlocks.LOCKED_CHAIN_LINK_DOOR.get(), LocksBlocks.CHAIN_LINK_DOOR));
        this.add(LocksBlocks.LOCKED_QUARTZ_DOOR.get(), createBuildingBlocksTable(LocksBlocks.LOCKED_QUARTZ_DOOR.get(), LocksBlocks.QUARTZ_DOOR));
        this.add(LocksBlocks.LOCKED_GLASS_DOOR.get(), createBuildingBlocksTable(LocksBlocks.LOCKED_GLASS_DOOR.get(), LocksBlocks.GLASS_DOOR));
    }

    /** Keeps the combination on the dropped item, which is what picks the ender storage it opens. */
    private LootTable.Builder createInventoryCodeTable(Block b) {
        LootPoolEntryContainer.Builder<?> entry = LootItem.lootTableItem(b).apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY).include(DataComponents.CUSTOM_DATA));
        LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry).when(ExplosionCondition.survivesExplosion());
        return LootTable.lootTable().withPool(pool);
    }

    /** Drops the box with its contents, name and lock carried over as data components, as vanilla's does. */
    private LootTable.Builder createLockedShulkerTable(Block b) {
        LootPoolEntryContainer.Builder<?> entry = LootItem.lootTableItem(b).apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY).include(DataComponents.CUSTOM_NAME).include(DataComponents.CONTAINER).include(DataComponents.CUSTOM_DATA));
        LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry).when(ExplosionCondition.survivesExplosion());
        return LootTable.lootTable().withPool(pool);
    }

    private LootTable.Builder createLockedDoorTable(Block b, Block out) {
        return LootTable.lootTable().withPool(applyExplosionCondition(b, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(out).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(b).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoorBlock.HALF, DoubleBlockHalf.LOWER))))));
    }

    private LootTable.Builder createBuildingBlocksTable(Block b, Identifier door) {
        return LootTable.lootTable().withPool(applyExplosionCondition(b, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).when(ModLoadedLootCondition.isModLoaded(LocksBlocks.BUILDING_BLOCKS_ID)).add(OptionalLootItem.optionalLootTableItem(door).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(b).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoorBlock.HALF, DoubleBlockHalf.LOWER))))));
    }
}
