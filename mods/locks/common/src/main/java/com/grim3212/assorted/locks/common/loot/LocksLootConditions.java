package com.grim3212.assorted.locks.common.loot;

import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.locks.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class LocksLootConditions {

    public static final RegistryProvider<LootItemConditionType> LOOT_ITEM_CONDITIONS = RegistryProvider.create(Registries.LOOT_CONDITION_TYPE, Constants.MOD_ID);

    public static final IRegistryObject<LootItemConditionType> MOD_LOADED = LOOT_ITEM_CONDITIONS.register("mod_loaded", () -> new LootItemConditionType(ModLoadedLootCondition.MAP_CODEC));

    public static void init() {
    }
}
