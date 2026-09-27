package com.grim3212.assorted.hoppers.data;

import com.grim3212.assorted.hoppers.api.HoppersTags;
import com.grim3212.assorted.hoppers.common.block.HoppersBlocks;
import com.grim3212.assorted.lib.core.storage.hopper.LockedHopperBlock;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.data.LibBlockTagProvider;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class HoppersBlockTagProvider extends LibBlockTagProvider {

    public HoppersBlockTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup) {
        super(packOutput, lookup);
    }

    @Override
    public void addCommonTags(Function<TagKey<Block>, TagAppender<Block>> rawTagger) {
        // A TagAppender takes ResourceKeys rather than the objects themselves now, so the appenders
        // are wrapped to keep the call sites reading in terms of Blocks.
        Function<TagKey<Block>, BlockTagAppender> tagger = (tag) -> new BlockTagAppender(rawTagger.apply(tag));

        BlockTagAppender piglinBuilder = tagger.apply(BlockTags.GUARDED_BY_PIGLINS);

        for (Entry<StorageMaterial, IRegistryObject<LockedHopperBlock>> hopper : HoppersBlocks.HOPPERS.entrySet()) {
            Block block = hopper.getValue().get();
            piglinBuilder.add(block);
            tagger.apply(HoppersTags.Blocks.HOPPERS).add(block);
            tagger.apply(BlockTags.MINEABLE_WITH_PICKAXE).add(block);

            switch (hopper.getKey().getStorageLevel()) {
                case 1:
                    tagger.apply(HoppersTags.Blocks.HOPPERS_LEVEL_1).add(block);
                    break;
                case 2:
                    tagger.apply(HoppersTags.Blocks.HOPPERS_LEVEL_2).add(block);
                    break;
                case 3:
                    tagger.apply(HoppersTags.Blocks.HOPPERS_LEVEL_3).add(block);
                    break;
                case 4:
                    tagger.apply(HoppersTags.Blocks.HOPPERS_LEVEL_4).add(block);
                    break;
                case 5:
                    tagger.apply(HoppersTags.Blocks.HOPPERS_LEVEL_5).add(block);
                    break;
                default:
                    tagger.apply(HoppersTags.Blocks.HOPPERS_LEVEL_0).add(block);
                    break;
            }
        }

        tagger.apply(HoppersTags.Blocks.HOPPERS).add(Blocks.HOPPER);
        tagger.apply(HoppersTags.Blocks.HOPPERS_LEVEL_0).add(Blocks.HOPPER);
        // Assorted Locks' locked hopper, when it is installed.
        rawTagger.apply(HoppersTags.Blocks.HOPPERS_LEVEL_0).addOptional(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("assortedlocks", "locked_hopper")));
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
