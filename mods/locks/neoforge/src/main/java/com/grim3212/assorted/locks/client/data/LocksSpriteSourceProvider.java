package com.grim3212.assorted.locks.client.data;

import com.grim3212.assorted.locks.Constants;
import com.grim3212.assorted.locks.client.model.LocksStorageModels;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.AtlasIds;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.data.SpriteSourceProvider;

import java.util.concurrent.CompletableFuture;

/** Adds the locked chest, ender chest and shulker box textures to the vanilla atlases they are drawn from. */
public class LocksSpriteSourceProvider extends SpriteSourceProvider {

    public LocksSpriteSourceProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Constants.MOD_ID);
    }

    @Override
    protected void gather() {
        atlas(AtlasIds.CHESTS).addSource(new SingleFile(LocksStorageModels.CHEST_SPRITE));
        atlas(AtlasIds.CHESTS).addSource(new SingleFile(LocksStorageModels.ENDER_CHEST_SPRITE));
        atlas(AtlasIds.SHULKER_BOXES).addSource(new SingleFile(LocksStorageModels.SHULKER_BOX_SPRITE));
    }
}
