package com.grim3212.assorted.barrels.data;

import com.grim3212.assorted.lib.data.LibItemTagProvider;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.barrels.api.BarrelsTags;
import com.grim3212.assorted.barrels.common.block.*;
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

public class BarrelsItemTagProvider extends LibItemTagProvider {


    public BarrelsItemTagProvider(PackOutput output, CompletableFuture<Provider> lookup, CompletableFuture<TagLookup<Block>> blockTagsProvider) {
        super(output, lookup, blockTagsProvider);
    }

    @Override
    public void addCommonTags(Function<TagKey<Item>, TagAppender<Item>> rawTagger, BiConsumer<TagKey<Block>, TagKey<Item>> copier) {
        // A TagAppender takes ResourceKeys rather than the objects themselves now, so the appenders
        // are wrapped to keep the call sites reading in terms of Items.
        Function<TagKey<Item>, ItemTagAppender> tagger = (tag) -> new ItemTagAppender(rawTagger.apply(tag));

        tagger.apply(ItemTags.PIGLIN_LOVED).add(BarrelsBlocks.BARRELS.get(StorageMaterial.GOLD).get().asItem());

        tagger.apply(LibCommonTags.Items.BARRELS_WOODEN).add(BarrelsBlocks.LOCKED_BARREL.get().asItem());

        tagger.apply(BarrelsTags.Items.CAN_UPGRADE_LEVEL_0).addTag(LibCommonTags.Items.BARRELS_WOODEN);
        for (Entry<StorageMaterial, IRegistryObject<LockedBarrelBlock>> barrel : BarrelsBlocks.BARRELS.entrySet()) {
            Item item = barrel.getValue().get().asItem();

            switch (barrel.getKey().getStorageLevel()) {
                case 1:
                    tagger.apply(BarrelsTags.Items.BARRELS_LEVEL_1).add(item);
                    tagger.apply(BarrelsTags.Items.CAN_UPGRADE_LEVEL_2).add(item);
                    break;
                case 2:
                    tagger.apply(BarrelsTags.Items.BARRELS_LEVEL_2).add(item);
                    tagger.apply(BarrelsTags.Items.CAN_UPGRADE_LEVEL_3).add(item);
                    break;
                case 3:
                    tagger.apply(BarrelsTags.Items.BARRELS_LEVEL_3).add(item);
                    tagger.apply(BarrelsTags.Items.CAN_UPGRADE_LEVEL_4).add(item);
                    break;
                case 4:
                    tagger.apply(BarrelsTags.Items.BARRELS_LEVEL_4).add(item);
                    tagger.apply(BarrelsTags.Items.CAN_UPGRADE_LEVEL_5).add(item);
                    break;
                case 5:
                    tagger.apply(BarrelsTags.Items.BARRELS_LEVEL_5).add(item);
                    break;
                default:
                    tagger.apply(BarrelsTags.Items.BARRELS_LEVEL_0).add(item);
                    tagger.apply(BarrelsTags.Items.CAN_UPGRADE_LEVEL_1).add(item);
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
