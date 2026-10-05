package com.grim3212.assorted.chests.client.data;

import com.grim3212.assorted.chests.Constants;
import com.grim3212.assorted.chests.client.model.ChestsModels;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.AtlasIds;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.data.SpriteSourceProvider;

import java.util.concurrent.CompletableFuture;

/** Adds the chest textures to the vanilla chest atlas they are drawn from. */
public class ChestsSpriteSourceProvider extends SpriteSourceProvider {

    public ChestsSpriteSourceProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Constants.MOD_ID);
    }

    @Override
    protected void gather() {
        for (Identifier tex : ChestsModels.CHEST_LOCATIONS.values()) {
            atlas(AtlasIds.CHESTS).addSource(new SingleFile(tex));
        }
    }

}
