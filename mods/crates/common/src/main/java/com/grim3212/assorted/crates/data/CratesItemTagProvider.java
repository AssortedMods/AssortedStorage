package com.grim3212.assorted.crates.data;

import com.grim3212.assorted.lib.data.LibItemTagProvider;
import com.grim3212.assorted.crates.api.CratesTags;
import com.grim3212.assorted.crates.common.item.CratesItems;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class CratesItemTagProvider extends LibItemTagProvider {


    public CratesItemTagProvider(PackOutput output, CompletableFuture<Provider> lookup, CompletableFuture<TagLookup<Block>> blockTagsProvider) {
        super(output, lookup, blockTagsProvider);
    }

    @Override
    public void addCommonTags(Function<TagKey<Item>, TagAppender<Item>> rawTagger, BiConsumer<TagKey<Block>, TagKey<Item>> copier) {
        // A TagAppender takes ResourceKeys rather than the objects themselves now, so the appenders
        // are wrapped to keep the call sites reading in terms of Items.
        Function<TagKey<Item>, ItemTagAppender> tagger = (tag) -> new ItemTagAppender(rawTagger.apply(tag));

        copier.accept(CratesTags.Blocks.DEEPSLATE, CratesTags.Items.DEEPSLATE);
        copier.accept(CratesTags.Blocks.PISTONS, CratesTags.Items.PISTONS);

        tagger.apply(CratesTags.Items.PAPER).add(Items.PAPER);

        // Logic from FunctionalStorage
        //@formatter:off
        tagger.apply(CratesTags.Items.CRAFTING_OVERRIDE)
                .add(Items.MELON, Items.MELON_SLICE)
                .add(Items.CLAY, Items.CLAY_BALL)
                .add(Items.GLOWSTONE, Items.GLOWSTONE_DUST)
                .add(Items.QUARTZ, Items.QUARTZ_BLOCK)
                .add(Items.ICE, Items.BLUE_ICE, Items.PACKED_ICE)
                .add(Items.AMETHYST_BLOCK, Items.AMETHYST_SHARD)
                .add(Items.SNOWBALL, Items.SNOW_BLOCK)
                .add(Items.BRICKS, Items.BRICK)
                .add(Items.NETHER_BRICK, Items.NETHER_BRICKS)
                .add(Items.NETHER_WART_BLOCK, Items.NETHER_WART)
                .add(Items.SANDSTONE, Items.SAND)
                .add(Items.RED_SANDSTONE, Items.RED_SAND);

        tagger.apply(CratesTags.Items.ONE_TO_ONE_CRAFTING_OVERRIDE)
                .add(Items.CUT_SANDSTONE)
                .add(Items.CUT_RED_SANDSTONE);
        //@formatter:on

        copier.accept(CratesTags.Blocks.CRATES, CratesTags.Items.CRATES);
        copier.accept(CratesTags.Blocks.CRATES_SINGLE, CratesTags.Items.CRATES_SINGLE);
        copier.accept(CratesTags.Blocks.CRATES_DOUBLE, CratesTags.Items.CRATES_DOUBLE);
        copier.accept(CratesTags.Blocks.CRATES_TRIPLE, CratesTags.Items.CRATES_TRIPLE);
        copier.accept(CratesTags.Blocks.CRATES_QUADRUPLE, CratesTags.Items.CRATES_QUADRUPLE);
        tagger.apply(CratesTags.Items.UPGRADES).add(CratesItems.AMOUNT_UPGRADE.get(), CratesItems.GLOW_UPGRADE.get(), CratesItems.REDSTONE_UPGRADE.get(), CratesItems.VOID_UPGRADE.get());
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

        ItemTagAppender add(ItemLike... items) {
            for (ItemLike item : items) {
                this.add(item);
            }
            return this;
        }
    }
}
