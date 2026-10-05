package com.grim3212.assorted.levelupgrades.api;

import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class LevelUpgradesTags {

    public static class Items {
        public static final TagKey<Item> PAPER = commonTag("paper");

        // Crates accept level upgrades by this tag, so they need not know this mod.
        public static final TagKey<Item> STORAGE_LEVEL_UPGRADES = commonTag("storage/level_upgrades");
        public static final TagKey<Item> STORAGE_LEVEL_0_UPGRADES = commonTag("storage/level_0_upgrades");
        public static final TagKey<Item> STORAGE_LEVEL_1_UPGRADES = commonTag("storage/level_1_upgrades");
        public static final TagKey<Item> STORAGE_LEVEL_2_UPGRADES = commonTag("storage/level_2_upgrades");
        public static final TagKey<Item> STORAGE_LEVEL_3_UPGRADES = commonTag("storage/level_3_upgrades");
        public static final TagKey<Item> STORAGE_LEVEL_4_UPGRADES = commonTag("storage/level_4_upgrades");
        public static final TagKey<Item> STORAGE_LEVEL_5_UPGRADES = commonTag("storage/level_5_upgrades");

        private static TagKey<Item> commonTag(String name) {
            return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(LibCommonTags.COMMON_NAMESPACE, name));
        }
    }
}
