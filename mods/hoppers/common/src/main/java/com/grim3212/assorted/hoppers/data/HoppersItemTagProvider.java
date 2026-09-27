package com.grim3212.assorted.hoppers.data;

import com.grim3212.assorted.hoppers.api.HoppersTags;
import com.grim3212.assorted.hoppers.common.block.HoppersBlocks;
import com.grim3212.assorted.hoppers.common.block.LockedHopperBlock;
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
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class HoppersItemTagProvider extends LibItemTagProvider {


    public HoppersItemTagProvider(PackOutput output, CompletableFuture<Provider> lookup, CompletableFuture<TagLookup<Block>> blockTagsProvider) {
        super(output, lookup, blockTagsProvider);
    }

    @Override
    public void addCommonTags(Function<TagKey<Item>, TagAppender<Item>> rawTagger, BiConsumer<TagKey<Block>, TagKey<Item>> copier) {
        // A TagAppender takes ResourceKeys rather than the objects themselves now, so the appenders
        // are wrapped to keep the call sites reading in terms of Items.
        Function<TagKey<Item>, ItemTagAppender> tagger = (tag) -> new ItemTagAppender(rawTagger.apply(tag));

        tagger.apply(ItemTags.PIGLIN_LOVED).add(HoppersBlocks.HOPPERS.get(StorageMaterial.GOLD).get().asItem());

        copier.accept(HoppersTags.Blocks.HOPPERS, HoppersTags.Items.HOPPERS);
        tagger.apply(HoppersTags.Items.CAN_UPGRADE_LEVEL_0).addTag(HoppersTags.Items.HOPPERS);
        for (Entry<StorageMaterial, IRegistryObject<LockedHopperBlock>> hopper : HoppersBlocks.HOPPERS.entrySet()) {
            Item item = hopper.getValue().get().asItem();

            switch (hopper.getKey().getStorageLevel()) {
                case 1:
                    tagger.apply(HoppersTags.Items.HOPPERS_LEVEL_1).add(item);
                    tagger.apply(HoppersTags.Items.CAN_UPGRADE_LEVEL_2).add(item);
                    break;
                case 2:
                    tagger.apply(HoppersTags.Items.HOPPERS_LEVEL_2).add(item);
                    tagger.apply(HoppersTags.Items.CAN_UPGRADE_LEVEL_3).add(item);
                    break;
                case 3:
                    tagger.apply(HoppersTags.Items.HOPPERS_LEVEL_3).add(item);
                    tagger.apply(HoppersTags.Items.CAN_UPGRADE_LEVEL_4).add(item);
                    break;
                case 4:
                    tagger.apply(HoppersTags.Items.HOPPERS_LEVEL_4).add(item);
                    tagger.apply(HoppersTags.Items.CAN_UPGRADE_LEVEL_5).add(item);
                    break;
                case 5:
                    tagger.apply(HoppersTags.Items.HOPPERS_LEVEL_5).add(item);
                    break;
                default:
                    tagger.apply(HoppersTags.Items.HOPPERS_LEVEL_0).add(item);
                    tagger.apply(HoppersTags.Items.CAN_UPGRADE_LEVEL_1).add(item);
                    break;
            }
        }
    }

    /**
     * Adapts the vanilla {@link TagAppender}, which is keyed by {@link ResourceKey}, back to the
     * ItemLike based calls this provider is written in terms of.
     */
    private record ItemTagAppender(TagAppender<Item> delegate) {

        ItemTagAppender add(ItemLike item) {
            this.delegate.add(BuiltInRegistries.ITEM.getResourceKey(item.asItem()).orElseThrow());
            return this;
        }

        ItemTagAppender addTag(TagKey<Item> tag) {
            this.delegate.addTag(tag);
            return this;
        }
    }
}
