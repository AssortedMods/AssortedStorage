package com.grim3212.assorted.locks.common.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.grim3212.assorted.lib.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;
import java.util.function.Consumer;

/**
 * A loot entry naming an item that may not exist, so an optional dependency can be missing without
 * breaking the table.
 */
public class OptionalLootItem extends LootPoolSingletonContainer {

    public static final MapCodec<OptionalLootItem> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(ResourceLocation.CODEC.fieldOf("name").forGetter(entry -> entry.lootItem))
                    .and(singletonFields(instance))
                    .apply(instance, OptionalLootItem::new));

    private final ResourceLocation lootItem;

    protected OptionalLootItem(ResourceLocation lootItem, int weight, int quality, List<LootItemCondition> condition, List<LootItemFunction> modifier) {
        super(weight, quality, condition, modifier);
        this.lootItem = lootItem;
    }

    @Override
    protected void createItemStack(Consumer<ItemStack> itemStacks, LootContext context) {
        Services.PLATFORM.getRegistry(Registries.ITEM).getValue(this.lootItem).ifPresent((item) -> {
            itemStacks.accept(new ItemStack(item));
        });
    }

    @Override
    public LootPoolEntryType getType() {
        return LocksLootEntries.OPTIONAL_ITEM.get();
    }

    public static LootPoolSingletonContainer.Builder<?> optionalLootTableItem(ResourceLocation location) {
        return simpleBuilder((weight, quality, conditions, functions) -> new OptionalLootItem(location, weight, quality, conditions, functions));
    }
}
