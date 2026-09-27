package com.grim3212.assorted.hoppers.api;

import com.grim3212.assorted.hoppers.Constants;
import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class HoppersTags {

    public static class Blocks {
        public static final TagKey<Block> HOPPERS_LEVEL_0 = hoppersTag("hoppers/level_0");
        public static final TagKey<Block> HOPPERS_LEVEL_1 = hoppersTag("hoppers/level_1");
        public static final TagKey<Block> HOPPERS_LEVEL_2 = hoppersTag("hoppers/level_2");
        public static final TagKey<Block> HOPPERS_LEVEL_3 = hoppersTag("hoppers/level_3");
        public static final TagKey<Block> HOPPERS_LEVEL_4 = hoppersTag("hoppers/level_4");
        public static final TagKey<Block> HOPPERS_LEVEL_5 = hoppersTag("hoppers/level_5");

        public static final TagKey<Block> HOPPERS = commonTag("hoppers");

        private static TagKey<Block> hoppersTag(String name) {
            return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        }

        private static TagKey<Block> commonTag(String name) {
            return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(LibCommonTags.COMMON_NAMESPACE, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> CAN_UPGRADE_LEVEL_0 = hoppersTag("can_upgrade/level_0");
        public static final TagKey<Item> CAN_UPGRADE_LEVEL_1 = hoppersTag("can_upgrade/level_1");
        public static final TagKey<Item> CAN_UPGRADE_LEVEL_2 = hoppersTag("can_upgrade/level_2");
        public static final TagKey<Item> CAN_UPGRADE_LEVEL_3 = hoppersTag("can_upgrade/level_3");
        public static final TagKey<Item> CAN_UPGRADE_LEVEL_4 = hoppersTag("can_upgrade/level_4");
        public static final TagKey<Item> CAN_UPGRADE_LEVEL_5 = hoppersTag("can_upgrade/level_5");

        public static final TagKey<Item> HOPPERS = commonTag("hoppers");
        public static final TagKey<Item> HOPPERS_LEVEL_0 = hoppersTag("hoppers/level_0");
        public static final TagKey<Item> HOPPERS_LEVEL_1 = hoppersTag("hoppers/level_1");
        public static final TagKey<Item> HOPPERS_LEVEL_2 = hoppersTag("hoppers/level_2");
        public static final TagKey<Item> HOPPERS_LEVEL_3 = hoppersTag("hoppers/level_3");
        public static final TagKey<Item> HOPPERS_LEVEL_4 = hoppersTag("hoppers/level_4");
        public static final TagKey<Item> HOPPERS_LEVEL_5 = hoppersTag("hoppers/level_5");

        private static TagKey<Item> hoppersTag(String name) {
            return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        }

        private static TagKey<Item> commonTag(String name) {
            return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(LibCommonTags.COMMON_NAMESPACE, name));
        }
    }

}
