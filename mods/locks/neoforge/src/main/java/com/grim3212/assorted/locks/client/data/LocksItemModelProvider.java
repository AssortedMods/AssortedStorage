package com.grim3212.assorted.locks.client.data;

import com.grim3212.assorted.locks.Constants;
import com.grim3212.assorted.locks.common.item.LocksItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.stream.Stream;

/** Item models for everything but block items, which {@link LocksBlockstateProvider} models. */
public class LocksItemModelProvider extends ModelProvider {

    public LocksItemModelProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    public String getName() {
        return "Assorted Locks item models";
    }

    /**
     * This provider owns no blocks - {@link LocksBlockstateProvider} does - so there is nothing
     * for {@link BlockModelGenerators} to do and nothing for the blockstate validation to miss.
     */
    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.empty();
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return super.getKnownItems().filter(holder -> !(holder.value() instanceof BlockItem));
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        itemModels.generateFlatItem(LocksItems.LOCKSMITH_KEY.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(LocksItems.LOCKSMITH_LOCK.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(LocksItems.KEY_RING.get(), ModelTemplates.FLAT_ITEM);
    }
}
