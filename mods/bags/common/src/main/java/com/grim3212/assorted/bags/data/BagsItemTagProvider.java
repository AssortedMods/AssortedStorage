package com.grim3212.assorted.bags.data;

import com.grim3212.assorted.bags.api.BagsTags;
import com.grim3212.assorted.bags.common.item.BagItem;
import com.grim3212.assorted.bags.common.item.BagsItems;
import com.grim3212.assorted.lib.data.LibItemTagProvider;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class BagsItemTagProvider extends LibItemTagProvider {

    public BagsItemTagProvider(PackOutput output, CompletableFuture<Provider> lookup, CompletableFuture<TagLookup<Block>> blockTagsProvider) {
        super(output, lookup, blockTagsProvider);
    }

    @Override
    public void addCommonTags(Function<TagKey<Item>, TagAppender<Item>> tagger, BiConsumer<TagKey<Block>, TagKey<Item>> copier) {
        for (Entry<StorageMaterial, IRegistryObject<BagItem>> bag : BagsItems.BAGS.entrySet()) {
            ResourceKey<Item> item = key(bag.getValue().get());

            switch (bag.getKey().getStorageLevel()) {
                case 1:
                    tagger.apply(BagsTags.Items.BAGS_LEVEL_1).add(item);
                    break;
                case 2:
                    tagger.apply(BagsTags.Items.BAGS_LEVEL_2).add(item);
                    break;
                case 3:
                    tagger.apply(BagsTags.Items.BAGS_LEVEL_3).add(item);
                    break;
                case 4:
                    tagger.apply(BagsTags.Items.BAGS_LEVEL_4).add(item);
                    break;
                case 5:
                    tagger.apply(BagsTags.Items.BAGS_LEVEL_5).add(item);
                    break;
                default:
                    tagger.apply(BagsTags.Items.BAGS_LEVEL_0).add(item);
                    break;
            }
        }

        tagger.apply(BagsTags.Items.BAGS_LEVEL_0).add(key(BagsItems.BAG.get()));
        tagger.apply(BagsTags.Items.BAGS).add(key(BagsItems.ENDER_BAG.get()));
        tagger.apply(BagsTags.Items.BAGS).addTag(BagsTags.Items.BAGS_LEVEL_0).addTag(BagsTags.Items.BAGS_LEVEL_1).addTag(BagsTags.Items.BAGS_LEVEL_2).addTag(BagsTags.Items.BAGS_LEVEL_3).addTag(BagsTags.Items.BAGS_LEVEL_4).addTag(BagsTags.Items.BAGS_LEVEL_5);
    }

    // A TagAppender takes ResourceKeys rather than the items themselves now.
    private static ResourceKey<Item> key(Item item) {
        return BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow();
    }
}
