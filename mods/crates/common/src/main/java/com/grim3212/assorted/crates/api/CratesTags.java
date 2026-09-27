package com.grim3212.assorted.crates.api;

import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.crates.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CratesTags {

    public static class Blocks {
        public static final TagKey<Block> CRATES = storageTag("crates");
        public static final TagKey<Block> CRATES_SINGLE = storageTag("crates/single");
        public static final TagKey<Block> CRATES_DOUBLE = storageTag("crates/double");
        public static final TagKey<Block> CRATES_TRIPLE = storageTag("crates/triple");
        public static final TagKey<Block> CRATES_QUADRUPLE = storageTag("crates/quadruple");

        public static final TagKey<Block> DEEPSLATE = commonTag("deepslate");
        public static final TagKey<Block> PISTONS = commonTag("pistons");

        private static TagKey<Block> storageTag(String name) {
            return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        }

        private static TagKey<Block> commonTag(String name) {
            return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(LibCommonTags.COMMON_NAMESPACE, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> INGOTS_ALUMINUM = LibCommonTags.Items.INGOTS_ALUMINUM;
        public static final TagKey<Item> INGOTS_BRONZE = LibCommonTags.Items.INGOTS_BRONZE;
        public static final TagKey<Item> INGOTS_STEEL = LibCommonTags.Items.INGOTS_STEEL;

        public static final TagKey<Item> PAPER = commonTag("paper");

        // Filled by whichever mod adds level upgrades, so crates take them without depending on it.
        public static final TagKey<Item> STORAGE_LEVEL_UPGRADES = commonTag("storage/level_upgrades");

        public static final TagKey<Item> CRATES = storageTag("crates");
        public static final TagKey<Item> CRATES_SINGLE = storageTag("crates/single");
        public static final TagKey<Item> CRATES_DOUBLE = storageTag("crates/double");
        public static final TagKey<Item> CRATES_TRIPLE = storageTag("crates/triple");
        public static final TagKey<Item> CRATES_QUADRUPLE = storageTag("crates/quadruple");
        public static final TagKey<Item> UPGRADES = storageTag("upgrades");
        public static final TagKey<Item> CRAFTING_OVERRIDE = storageTag("crafting_override");
        public static final TagKey<Item> ONE_TO_ONE_CRAFTING_OVERRIDE = storageTag("one_to_one_crafting_override");

        public static final TagKey<Item> DEEPSLATE = commonTag("deepslate");
        public static final TagKey<Item> PISTONS = commonTag("pistons");

        private static TagKey<Item> storageTag(String name) {
            return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        }

        private static TagKey<Item> commonTag(String name) {
            return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(LibCommonTags.COMMON_NAMESPACE, name));
        }
    }

}
