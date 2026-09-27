package com.grim3212.assorted.shulkers.client.data;

import com.grim3212.assorted.shulkers.Constants;
import com.grim3212.assorted.shulkers.client.model.ShulkersModels;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.AtlasIds;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.data.SpriteSourceProvider;

import java.util.concurrent.CompletableFuture;

/** Adds this mod's textures to the vanilla atlases they are drawn from. */
public class ShulkersSpriteSourceProvider extends SpriteSourceProvider {

    public ShulkersSpriteSourceProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Constants.MOD_ID);
    }

    @Override
    protected void gather() {
        for (Identifier tex : ShulkersModels.SHULKER_LOCATIONS.values()) {
            atlas(AtlasIds.SHULKER_BOXES).addSource(new SingleFile(tex));
        }
    }

}
