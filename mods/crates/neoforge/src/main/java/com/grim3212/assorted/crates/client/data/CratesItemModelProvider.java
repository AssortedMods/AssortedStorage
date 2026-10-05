package com.grim3212.assorted.crates.client.data;

import com.grim3212.assorted.crates.Constants;
import com.grim3212.assorted.crates.common.item.CratesItems;
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

/** Item models for everything but block items, which {@link CratesBlockstateProvider} models. */
public class CratesItemModelProvider extends ModelProvider {

    public CratesItemModelProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    public String getName() {
        return "Assorted Crates item models";
    }

    /**
     * This provider owns no blocks - {@link CratesBlockstateProvider} does - so there is nothing
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
        itemModels.generateFlatItem(CratesItems.ROTATOR_MAJIG.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        generatedItem(itemModels, CratesItems.BLANK_UPGRADE.get());
        generatedItem(itemModels, CratesItems.VOID_UPGRADE.get());
        generatedItem(itemModels, CratesItems.REDSTONE_UPGRADE.get());
        generatedItem(itemModels, CratesItems.AMOUNT_UPGRADE.get());
        generatedItem(itemModels, CratesItems.GLOW_UPGRADE.get());
    }

    private void generatedItem(ItemModelGenerators itemModels, Item item) {
        itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
    }
}
