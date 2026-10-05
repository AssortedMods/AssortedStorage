package com.grim3212.assorted.levelupgrades.data;

import com.grim3212.assorted.levelupgrades.api.LevelUpgradesTags;
import com.grim3212.assorted.levelupgrades.common.item.LevelUpgradesItems;
import com.grim3212.assorted.levelupgrades.common.item.upgrades.LevelUpgradeItem;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.data.LibItemTagProvider;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class LevelUpgradesItemTagProvider extends LibItemTagProvider {

    /** The {@code c:storage/level_<n>_upgrades} tag for each storage level. */
    private static final List<TagKey<Item>> LEVEL_TAGS = List.of(LevelUpgradesTags.Items.STORAGE_LEVEL_0_UPGRADES, LevelUpgradesTags.Items.STORAGE_LEVEL_1_UPGRADES,
            LevelUpgradesTags.Items.STORAGE_LEVEL_2_UPGRADES, LevelUpgradesTags.Items.STORAGE_LEVEL_3_UPGRADES, LevelUpgradesTags.Items.STORAGE_LEVEL_4_UPGRADES,
            LevelUpgradesTags.Items.STORAGE_LEVEL_5_UPGRADES);

    public LevelUpgradesItemTagProvider(PackOutput output, CompletableFuture<Provider> lookup, CompletableFuture<TagLookup<Block>> blockTagsProvider) {
        super(output, lookup, blockTagsProvider);
    }

    @Override
    public void addCommonTags(Function<TagKey<Item>, TagAppender<Item>> tagger, BiConsumer<TagKey<Block>, TagKey<Item>> copier) {
        tagger.apply(ItemTags.PIGLIN_LOVED).add(key(LevelUpgradesItems.LEVEL_UPGRADES.get(StorageMaterial.GOLD).get()));
        tagger.apply(LevelUpgradesTags.Items.PAPER).add(key(Items.PAPER));

        for (Entry<StorageMaterial, IRegistryObject<LevelUpgradeItem>> levelUpgrade : LevelUpgradesItems.LEVEL_UPGRADES.entrySet()) {
            // Anything outside 1 to 5 counts as level 0, as it always has.
            int level = levelUpgrade.getKey().getStorageLevel();
            tagger.apply(LEVEL_TAGS.get(level >= 1 && level <= 5 ? level : 0)).add(key(levelUpgrade.getValue().get()));
        }

        TagAppender<Item> all = tagger.apply(LevelUpgradesTags.Items.STORAGE_LEVEL_UPGRADES);
        for (TagKey<Item> levelTag : LEVEL_TAGS) {
            all.addTag(levelTag);
        }
    }

    private static ResourceKey<Item> key(Item item) {
        return BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow();
    }
}
