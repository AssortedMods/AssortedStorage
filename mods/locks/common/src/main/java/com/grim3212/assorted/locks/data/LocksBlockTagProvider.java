package com.grim3212.assorted.locks.data;

import com.grim3212.assorted.lib.data.LibBlockTagProvider;
import com.grim3212.assorted.locks.common.block.LocksBlocks;
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

public class LocksBlockTagProvider extends LibBlockTagProvider {

    public LocksBlockTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup) {
        super(packOutput, lookup);
    }

    @Override
    public void addCommonTags(Function<TagKey<Block>, TagAppender<Block>> tagger) {
        TagAppender<Block> doors = tagger.apply(BlockTags.DOORS);
        for (Block door : LocksBlocks.lockedDoors()) {
            doors.add(key(door));
        }

        TagAppender<Block> pickaxe = tagger.apply(BlockTags.MINEABLE_WITH_PICKAXE);
        pickaxe.add(key(LocksBlocks.LOCKED_IRON_DOOR.get()));
        pickaxe.add(key(LocksBlocks.LOCKED_QUARTZ_DOOR.get()));
        pickaxe.add(key(LocksBlocks.LOCKED_STEEL_DOOR.get()));
        // The locked copper doors need the right tool, as vanilla's do, so they have to be in a
        // mineable tag or they break normally and drop nothing.
        Blocks.COPPER_DOOR.forEach(door -> pickaxe.add(key(LocksBlocks.VANILLA_DOORS.get(door).get())));
    }

    private static ResourceKey<Block> key(Block block) {
        return BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow();
    }
}
