package com.grim3212.assorted.containers.data;

import com.grim3212.assorted.containers.common.block.ContainersBlocks;
import com.grim3212.assorted.lib.data.LibItemTagProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ContainersItemTagProvider extends LibItemTagProvider {

    public ContainersItemTagProvider(PackOutput output, CompletableFuture<Provider> lookup, CompletableFuture<TagLookup<Block>> blockTagsProvider) {
        super(output, lookup, blockTagsProvider);
    }

    @Override
    public void addCommonTags(Function<TagKey<Item>, TagAppender<Item>> tagger, BiConsumer<TagKey<Block>, TagKey<Item>> copier) {
        // A TagAppender takes ResourceKeys rather than the items themselves now.
        tagger.apply(ItemTags.PIGLIN_LOVED).add(BuiltInRegistries.ITEM.getResourceKey(ContainersBlocks.GOLD_SAFE.get().asItem()).orElseThrow());
    }
}
