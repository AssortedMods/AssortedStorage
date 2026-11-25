package com.grim3212.assorted.locks.client.data;

import com.grim3212.assorted.locks.Constants;
import com.grim3212.assorted.locks.client.model.LocksStorageModels;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SpriteSourceProvider;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/** Adds the locked chest, ender chest and shulker box textures to the vanilla atlases they are drawn from. */
public class LocksSpriteSourceProvider extends SpriteSourceProvider {

    public LocksSpriteSourceProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void gather() {
        atlas(CHESTS_ATLAS).addSource(new SingleFile(LocksStorageModels.CHEST_SPRITE, Optional.empty()));
        atlas(CHESTS_ATLAS).addSource(new SingleFile(LocksStorageModels.ENDER_CHEST_SPRITE, Optional.empty()));
        atlas(SHULKER_BOXES_ATLAS).addSource(new SingleFile(LocksStorageModels.SHULKER_BOX_SPRITE, Optional.empty()));
    }
}
