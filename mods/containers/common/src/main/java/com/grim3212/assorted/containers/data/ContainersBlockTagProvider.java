package com.grim3212.assorted.containers.data;

import com.grim3212.assorted.containers.common.block.ContainersBlocks;
import com.grim3212.assorted.lib.data.LibBlockTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ContainersBlockTagProvider extends LibBlockTagProvider {

    public ContainersBlockTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup) {
        super(packOutput, lookup);
    }

    @Override
    public void addCommonTags(Function<TagKey<Block>, TagAppender<Block>> tagger) {
        TagAppender<Block> piglinBuilder = tagger.apply(BlockTags.GUARDED_BY_PIGLINS);
        ContainersBlocks.WAREHOUSE_CRATES.values().forEach(crate -> piglinBuilder.add(key(crate.get())));
        for (Block block : new Block[]{ContainersBlocks.GLASS_CABINET.get(), ContainersBlocks.WOOD_CABINET.get(), ContainersBlocks.GOLD_SAFE.get(),
                ContainersBlocks.OBSIDIAN_SAFE.get(), ContainersBlocks.LOCKER.get(), ContainersBlocks.ITEM_TOWER.get()}) {
            piglinBuilder.add(key(block));
        }

        TagAppender<Block> pickaxeBuilder = tagger.apply(BlockTags.MINEABLE_WITH_PICKAXE);
        for (Block block : new Block[]{ContainersBlocks.ITEM_TOWER.get(), ContainersBlocks.LOCKER.get(), ContainersBlocks.GOLD_SAFE.get(), ContainersBlocks.OBSIDIAN_SAFE.get()}) {
            pickaxeBuilder.add(key(block));
        }
    }

    // A TagAppender takes ResourceKeys rather than the blocks themselves now.
    private static ResourceKey<Block> key(Block block) {
        return BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow();
    }
}
