package com.grim3212.assorted.shulkers.client.data;

import com.grim3212.assorted.shulkers.Constants;
import com.grim3212.assorted.shulkers.client.model.ShulkersModels;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SpriteSourceProvider;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/** Adds this mod's textures to the vanilla atlases they are drawn from. */
public class ShulkersSpriteSourceProvider extends SpriteSourceProvider {

    public ShulkersSpriteSourceProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void gather() {
        for (ResourceLocation tex : ShulkersModels.SHULKER_LOCATIONS.values()) {
            atlas(SHULKER_BOXES_ATLAS).addSource(new SingleFile(tex, Optional.empty()));
        }
    }

}
