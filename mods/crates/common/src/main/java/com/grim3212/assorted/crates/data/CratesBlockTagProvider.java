package com.grim3212.assorted.crates.data;

import com.grim3212.assorted.lib.data.LibBlockTagProvider;
import com.grim3212.assorted.crates.api.CratesTags;
import com.grim3212.assorted.crates.common.block.CratesBlocks;
import com.grim3212.assorted.crates.common.block.CratesBlocks.CrateGroup;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class CratesBlockTagProvider extends LibBlockTagProvider {

    public CratesBlockTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup) {
        super(packOutput, lookup);
    }

    @Override
    public void addCommonTags(Function<TagKey<Block>, TagAppender<Block>> rawTagger) {
        // A TagAppender takes ResourceKeys rather than the objects themselves now, so the appenders
        // are wrapped to keep the call sites reading in terms of Blocks.
        Function<TagKey<Block>, BlockTagAppender> tagger = (tag) -> new BlockTagAppender(rawTagger.apply(tag));

        BlockTagAppender piglinBuilder = tagger.apply(BlockTags.GUARDED_BY_PIGLINS);

        tagger.apply(CratesTags.Blocks.CRATES).add(CratesBlocks.CRATE_COMPACTING.get());
        tagger.apply(BlockTags.MINEABLE_WITH_PICKAXE).add(CratesBlocks.CRATE_COMPACTING.get(), CratesBlocks.CRATE_CONTROLLER.get(), CratesBlocks.CRATE_BRIDGE.get());
        for (CrateGroup crateGroup : CratesBlocks.CRATES) {
            piglinBuilder.add(crateGroup.SINGLE.get());
            tagger.apply(BlockTags.MINEABLE_WITH_AXE).add(crateGroup.SINGLE.get());
            tagger.apply(CratesTags.Blocks.CRATES).add(crateGroup.SINGLE.get());
            tagger.apply(CratesTags.Blocks.CRATES_SINGLE).add(crateGroup.SINGLE.get());

            piglinBuilder.add(crateGroup.DOUBLE.get());
            tagger.apply(BlockTags.MINEABLE_WITH_AXE).add(crateGroup.DOUBLE.get());
            tagger.apply(CratesTags.Blocks.CRATES).add(crateGroup.DOUBLE.get());
            tagger.apply(CratesTags.Blocks.CRATES_DOUBLE).add(crateGroup.DOUBLE.get());

            piglinBuilder.add(crateGroup.TRIPLE.get());
            tagger.apply(BlockTags.MINEABLE_WITH_AXE).add(crateGroup.TRIPLE.get());
            tagger.apply(CratesTags.Blocks.CRATES).add(crateGroup.TRIPLE.get());
            tagger.apply(CratesTags.Blocks.CRATES_TRIPLE).add(crateGroup.TRIPLE.get());

            piglinBuilder.add(crateGroup.QUADRUPLE.get());
            tagger.apply(BlockTags.MINEABLE_WITH_AXE).add(crateGroup.QUADRUPLE.get());
            tagger.apply(CratesTags.Blocks.CRATES).add(crateGroup.QUADRUPLE.get());
            tagger.apply(CratesTags.Blocks.CRATES_QUADRUPLE).add(crateGroup.QUADRUPLE.get());
        }

        tagger.apply(CratesTags.Blocks.DEEPSLATE).add(Blocks.DEEPSLATE, Blocks.COBBLED_DEEPSLATE, Blocks.POLISHED_DEEPSLATE, Blocks.DEEPSLATE_BRICKS, Blocks.CRACKED_DEEPSLATE_BRICKS, Blocks.DEEPSLATE_TILES, Blocks.CRACKED_DEEPSLATE_TILES, Blocks.CHISELED_DEEPSLATE, Blocks.REINFORCED_DEEPSLATE);
        tagger.apply(CratesTags.Blocks.PISTONS).add(Blocks.PISTON, Blocks.STICKY_PISTON);
    }

    /**
     * Adapts the vanilla {@link TagAppender}, which is keyed by {@link ResourceKey}, back to the
     * Block based calls this provider is written in terms of.
     */
    private record BlockTagAppender(TagAppender<Block> delegate) {

        BlockTagAppender add(Block block) {
            this.delegate.add(BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow());
            return this;
        }

        BlockTagAppender add(Block... blocks) {
            for (Block block : blocks) {
                this.add(block);
            }
            return this;
        }
    }
}
