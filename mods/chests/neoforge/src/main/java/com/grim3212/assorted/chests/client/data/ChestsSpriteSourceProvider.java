package com.grim3212.assorted.chests.client.data;

import com.grim3212.assorted.chests.Constants;
import com.grim3212.assorted.chests.client.model.ChestsModels;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SpriteSourceProvider;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/** Adds the chest textures to the vanilla chest atlas they are drawn from. */
public class ChestsSpriteSourceProvider extends SpriteSourceProvider {

    public ChestsSpriteSourceProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void gather() {
        for (ResourceLocation tex : ChestsModels.CHEST_LOCATIONS.values()) {
            atlas(CHESTS_ATLAS).addSource(new SingleFile(tex, Optional.empty()));
        }
    }

}
