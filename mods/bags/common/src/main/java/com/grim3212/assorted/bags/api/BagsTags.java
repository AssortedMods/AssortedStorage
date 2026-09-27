package com.grim3212.assorted.bags.api;

import com.grim3212.assorted.bags.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class BagsTags {

    public static class Items {
        public static final TagKey<Item> BAGS = bagsTag("bags");
        public static final TagKey<Item> BAGS_LEVEL_0 = bagsTag("bags/level_0");
        public static final TagKey<Item> BAGS_LEVEL_1 = bagsTag("bags/level_1");
        public static final TagKey<Item> BAGS_LEVEL_2 = bagsTag("bags/level_2");
        public static final TagKey<Item> BAGS_LEVEL_3 = bagsTag("bags/level_3");
        public static final TagKey<Item> BAGS_LEVEL_4 = bagsTag("bags/level_4");
        public static final TagKey<Item> BAGS_LEVEL_5 = bagsTag("bags/level_5");

        private static TagKey<Item> bagsTag(String name) {
            return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        }
    }

}
