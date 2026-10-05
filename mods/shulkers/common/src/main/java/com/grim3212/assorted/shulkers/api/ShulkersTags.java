package com.grim3212.assorted.shulkers.api;

import com.grim3212.assorted.shulkers.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ShulkersTags {

    public static class Blocks {
        public static final TagKey<Block> SHULKERS_NORMAL = shulkersTag("shulkers/normal");
        public static final TagKey<Block> SHULKERS_LEVEL_0 = shulkersTag("shulkers/level_0");
        public static final TagKey<Block> SHULKERS_LEVEL_1 = shulkersTag("shulkers/level_1");
        public static final TagKey<Block> SHULKERS_LEVEL_2 = shulkersTag("shulkers/level_2");
        public static final TagKey<Block> SHULKERS_LEVEL_3 = shulkersTag("shulkers/level_3");
        public static final TagKey<Block> SHULKERS_LEVEL_4 = shulkersTag("shulkers/level_4");
        public static final TagKey<Block> SHULKERS_LEVEL_5 = shulkersTag("shulkers/level_5");

        private static TagKey<Block> shulkersTag(String name) {
            return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> CAN_UPGRADE_LEVEL_0 = shulkersTag("can_upgrade/level_0");
        public static final TagKey<Item> CAN_UPGRADE_LEVEL_1 = shulkersTag("can_upgrade/level_1");
        public static final TagKey<Item> CAN_UPGRADE_LEVEL_2 = shulkersTag("can_upgrade/level_2");
        public static final TagKey<Item> CAN_UPGRADE_LEVEL_3 = shulkersTag("can_upgrade/level_3");
        public static final TagKey<Item> CAN_UPGRADE_LEVEL_4 = shulkersTag("can_upgrade/level_4");
        public static final TagKey<Item> CAN_UPGRADE_LEVEL_5 = shulkersTag("can_upgrade/level_5");

        public static final TagKey<Item> SHULKERS_NORMAL = shulkersTag("shulkers/normal");
        public static final TagKey<Item> SHULKERS_LEVEL_0 = shulkersTag("shulkers/level_0");
        public static final TagKey<Item> SHULKERS_LEVEL_1 = shulkersTag("shulkers/level_1");
        public static final TagKey<Item> SHULKERS_LEVEL_2 = shulkersTag("shulkers/level_2");
        public static final TagKey<Item> SHULKERS_LEVEL_3 = shulkersTag("shulkers/level_3");
        public static final TagKey<Item> SHULKERS_LEVEL_4 = shulkersTag("shulkers/level_4");
        public static final TagKey<Item> SHULKERS_LEVEL_5 = shulkersTag("shulkers/level_5");

        private static TagKey<Item> shulkersTag(String name) {
            return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        }
    }

}
