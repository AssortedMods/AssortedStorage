package com.grim3212.assorted.barrels.api;

import com.grim3212.assorted.barrels.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class BarrelsTags {

    public static class Blocks {
        public static final TagKey<Block> BARRELS_LEVEL_0 = storageTag("barrels/level_0");
        public static final TagKey<Block> BARRELS_LEVEL_1 = storageTag("barrels/level_1");
        public static final TagKey<Block> BARRELS_LEVEL_2 = storageTag("barrels/level_2");
        public static final TagKey<Block> BARRELS_LEVEL_3 = storageTag("barrels/level_3");
        public static final TagKey<Block> BARRELS_LEVEL_4 = storageTag("barrels/level_4");
        public static final TagKey<Block> BARRELS_LEVEL_5 = storageTag("barrels/level_5");

        private static TagKey<Block> storageTag(String name) {
            return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> CAN_UPGRADE_LEVEL_0 = storageTag("can_upgrade/level_0");
        public static final TagKey<Item> CAN_UPGRADE_LEVEL_1 = storageTag("can_upgrade/level_1");
        public static final TagKey<Item> CAN_UPGRADE_LEVEL_2 = storageTag("can_upgrade/level_2");
        public static final TagKey<Item> CAN_UPGRADE_LEVEL_3 = storageTag("can_upgrade/level_3");
        public static final TagKey<Item> CAN_UPGRADE_LEVEL_4 = storageTag("can_upgrade/level_4");
        public static final TagKey<Item> CAN_UPGRADE_LEVEL_5 = storageTag("can_upgrade/level_5");

        public static final TagKey<Item> BARRELS_LEVEL_0 = storageTag("barrels/level_0");
        public static final TagKey<Item> BARRELS_LEVEL_1 = storageTag("barrels/level_1");
        public static final TagKey<Item> BARRELS_LEVEL_2 = storageTag("barrels/level_2");
        public static final TagKey<Item> BARRELS_LEVEL_3 = storageTag("barrels/level_3");
        public static final TagKey<Item> BARRELS_LEVEL_4 = storageTag("barrels/level_4");
        public static final TagKey<Item> BARRELS_LEVEL_5 = storageTag("barrels/level_5");

        private static TagKey<Item> storageTag(String name) {
            return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        }
    }

}
