package com.grim3212.assorted.crates.client.data;

import com.grim3212.assorted.crates.Constants;
import com.grim3212.assorted.crates.common.item.CratesItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/** Item models for everything but block items, which {@link CratesBlockstateProvider} models. */
public class CratesItemModelProvider extends ItemModelProvider {

    public CratesItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Assorted Crates item models";
    }

    @Override
    protected void registerModels() {
        handheldItem(CratesItems.ROTATOR_MAJIG.get());

        generatedItem(CratesItems.BLANK_UPGRADE.get());
        generatedItem(CratesItems.VOID_UPGRADE.get());
        generatedItem(CratesItems.REDSTONE_UPGRADE.get());
        generatedItem(CratesItems.AMOUNT_UPGRADE.get());
        generatedItem(CratesItems.GLOW_UPGRADE.get());
    }

    private void generatedItem(Item item) {
        this.basicItem(item);
    }
}
