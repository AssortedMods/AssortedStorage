package com.grim3212.assorted.barrels.data;

import com.grim3212.assorted.lib.data.LibBlockTagProvider;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.barrels.api.BarrelsTags;
import com.grim3212.assorted.barrels.common.block.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class BarrelsBlockTagProvider extends LibBlockTagProvider {

    public BarrelsBlockTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup) {
        super(packOutput, lookup);
    }

    @Override
    public void addCommonTags(Function<TagKey<Block>, TagAppender<Block>> rawTagger) {
        // A TagAppender takes ResourceKeys rather than the objects themselves now, so the appenders
        // are wrapped to keep the call sites reading in terms of Blocks.
        Function<TagKey<Block>, BlockTagAppender> tagger = (tag) -> new BlockTagAppender(rawTagger.apply(tag));

        BlockTagAppender piglinBuilder = tagger.apply(BlockTags.GUARDED_BY_PIGLINS);

        for (Entry<StorageMaterial, IRegistryObject<LockedBarrelBlock>> barrel : BarrelsBlocks.BARRELS.entrySet()) {
            Block block = barrel.getValue().get();
            piglinBuilder.add(block);
            tagger.apply(LibCommonTags.Blocks.BARRELS).add(block);
            tagger.apply(BlockTags.MINEABLE_WITH_PICKAXE).add(block);

            switch (barrel.getKey().getStorageLevel()) {
                case 1:
                    tagger.apply(BarrelsTags.Blocks.BARRELS_LEVEL_1).add(block);
                    break;
                case 2:
                    tagger.apply(BarrelsTags.Blocks.BARRELS_LEVEL_2).add(block);
                    break;
                case 3:
                    tagger.apply(BarrelsTags.Blocks.BARRELS_LEVEL_3).add(block);
                    break;
                case 4:
                    tagger.apply(BarrelsTags.Blocks.BARRELS_LEVEL_4).add(block);
                    break;
                case 5:
                    tagger.apply(BarrelsTags.Blocks.BARRELS_LEVEL_5).add(block);
                    break;
                default:
                    tagger.apply(BarrelsTags.Blocks.BARRELS_LEVEL_0).add(block);
                    break;
            }
        }

        tagger.apply(LibCommonTags.Blocks.BARRELS_WOODEN).add(BarrelsBlocks.LOCKED_BARREL.get());
        tagger.apply(BarrelsTags.Blocks.BARRELS_LEVEL_0).addTag(LibCommonTags.Blocks.BARRELS_WOODEN);

        tagger.apply(BlockTags.MINEABLE_WITH_AXE).add(BarrelsBlocks.LOCKED_BARREL.get());
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

        BlockTagAppender addTag(TagKey<Block> tag) {
            this.delegate.addTag(tag);
            return this;
        }
    }
}
