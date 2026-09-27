package com.grim3212.assorted.shulkers.data;

import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.data.LibBlockTagProvider;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.shulkers.api.ShulkersTags;
import com.grim3212.assorted.shulkers.common.block.LockedShulkerBoxBlock;
import com.grim3212.assorted.shulkers.common.block.ShulkersBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ShulkersBlockTagProvider extends LibBlockTagProvider {

    public ShulkersBlockTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup) {
        super(packOutput, lookup);
    }

    @Override
    public void addCommonTags(Function<TagKey<Block>, TagAppender<Block>> rawTagger) {
        // A TagAppender takes ResourceKeys rather than the objects themselves now, so the appenders
        // are wrapped to keep the call sites reading in terms of Blocks.
        Function<TagKey<Block>, BlockTagAppender> tagger = (tag) -> new BlockTagAppender(rawTagger.apply(tag));

        BlockTagAppender piglinBuilder = tagger.apply(BlockTags.GUARDED_BY_PIGLINS);
        piglinBuilder.add(ShulkersBlocks.LOCKED_SHULKER_BOX.get());

        for (Entry<StorageMaterial, IRegistryObject<LockedShulkerBoxBlock>> shulker : ShulkersBlocks.SHULKERS.entrySet()) {
            Block block = shulker.getValue().get();
            piglinBuilder.add(block);
            tagger.apply(BlockTags.SHULKER_BOXES).add(block);
            tagger.apply(BlockTags.MINEABLE_WITH_PICKAXE).add(block);

            switch (shulker.getKey().getStorageLevel()) {
                case 1:
                    tagger.apply(ShulkersTags.Blocks.SHULKERS_LEVEL_1).add(block);
                    break;
                case 2:
                    tagger.apply(ShulkersTags.Blocks.SHULKERS_LEVEL_2).add(block);
                    break;
                case 3:
                    tagger.apply(ShulkersTags.Blocks.SHULKERS_LEVEL_3).add(block);
                    break;
                case 4:
                    tagger.apply(ShulkersTags.Blocks.SHULKERS_LEVEL_4).add(block);
                    break;
                case 5:
                    tagger.apply(ShulkersTags.Blocks.SHULKERS_LEVEL_5).add(block);
                    break;
                default:
                    tagger.apply(ShulkersTags.Blocks.SHULKERS_LEVEL_0).add(block);
                    break;
            }
        }

        tagger.apply(BlockTags.SHULKER_BOXES).add(ShulkersBlocks.LOCKED_SHULKER_BOX.get());
        // The per colour shulker boxes are a ColorCollection now rather than 16 separate fields.
        tagger.apply(ShulkersTags.Blocks.SHULKERS_LEVEL_0).add(Blocks.SHULKER_BOX).addAll(Blocks.DYED_SHULKER_BOX.asList());
        tagger.apply(ShulkersTags.Blocks.SHULKERS_NORMAL).add(Blocks.SHULKER_BOX).addAll(Blocks.DYED_SHULKER_BOX.asList());
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

        BlockTagAppender addAll(Iterable<Block> blocks) {
            for (Block block : blocks) {
                this.add(block);
            }
            return this;
        }
    }
}
