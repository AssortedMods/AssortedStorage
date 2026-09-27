package com.grim3212.assorted.locks.data;

import com.grim3212.assorted.lib.data.LibItemTagProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.locks.common.block.LocksBlocks;
import com.grim3212.assorted.locks.common.item.LocksItems;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.Identifier;
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

        // Recipes from the other parts take a locked container wherever they take the vanilla one.
        tagger.apply(LibCommonTags.Items.CHESTS_WOODEN).add(key(LocksBlocks.LOCKED_CHEST.get().asItem()));
        tagger.apply(LibCommonTags.Items.CHESTS_ENDER).add(key(LocksBlocks.LOCKED_ENDER_CHEST.get().asItem()));
        tagger.apply(LibCommonTags.Items.BARRELS_WOODEN).add(key(LocksBlocks.LOCKED_BARREL.get().asItem()));
        tagger.apply(HOPPERS).add(key(LocksBlocks.LOCKED_HOPPER.get().asItem()));
    }

    private static final TagKey<Item> HOPPERS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "hoppers"));

    private static ResourceKey<Item> key(Item item) {
        return BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow();
    }
}
