package com.grim3212.assorted.locks.client.data;

import com.grim3212.assorted.locks.Constants;
import com.grim3212.assorted.locks.common.item.LocksItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/** Item models for everything but block items, which {@link LocksBlockstateProvider} models. */
public class LocksItemModelProvider extends ItemModelProvider {

    public LocksItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Assorted Locks item models";
    }

    @Override
    protected void registerModels() {
        this.basicItem(LocksItems.LOCKSMITH_KEY.get());
        this.basicItem(LocksItems.LOCKSMITH_LOCK.get());
        this.basicItem(LocksItems.KEY_RING.get());
    }
}
