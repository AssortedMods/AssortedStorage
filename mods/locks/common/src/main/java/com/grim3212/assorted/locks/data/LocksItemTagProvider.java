package com.grim3212.assorted.locks.data;

import com.grim3212.assorted.lib.data.LibItemTagProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.locks.common.item.LocksItems;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class LocksItemTagProvider extends LibItemTagProvider {

    public LocksItemTagProvider(PackOutput output, CompletableFuture<Provider> lookup, CompletableFuture<TagLookup<Block>> blockTagsProvider) {
        super(output, lookup, blockTagsProvider);
    }

    @Override
    public void addCommonTags(Function<TagKey<Item>, TagAppender<Item>> tagger, BiConsumer<TagKey<Block>, TagKey<Item>> copier) {
        // Every storage mod tests these tags rather than naming this one's items.
        tagger.apply(LibCommonTags.Items.LOCKS).add(key(LocksItems.LOCKSMITH_LOCK.get()));
        TagAppender<Item> keys = tagger.apply(LibCommonTags.Items.KEYS);
        keys.add(key(LocksItems.LOCKSMITH_KEY.get()));
        keys.add(key(LocksItems.KEY_RING.get()));
    }

    private static ResourceKey<Item> key(Item item) {
        return BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow();
    }
}
