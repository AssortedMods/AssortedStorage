package com.grim3212.assorted.levelupgrades;

import com.grim3212.assorted.levelupgrades.client.data.LevelUpgradesItemModelProvider;
import com.grim3212.assorted.levelupgrades.client.data.LevelUpgradesLanguageProvider;
import com.grim3212.assorted.levelupgrades.client.data.LevelUpgradesManualProvider;
import com.grim3212.assorted.levelupgrades.data.LevelUpgradesItemTagProvider;
import com.grim3212.assorted.levelupgrades.data.LevelUpgradesRecipes;
import com.grim3212.assorted.lib.data.ForgeItemTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@Mod(Constants.MOD_ID)
public class AssortedLevelUpgradesNeoForge {

    /**
     * {@code FMLJavaModLoadingContext} is gone; the mod event bus and the mod container are injected
     * into the {@code @Mod} constructor instead.
     */
    public AssortedLevelUpgradesNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherServerData);
        modBus.addListener(this::gatherClientData);

        LevelUpgradesCommonMod.init();
    }

    /**
     * Server datagen. The server and client halves are separate events; if the wrong one runs, the
     * build still succeeds, with "All providers took: 0 ms".
     */
    private void gatherServerData(final GatherDataEvent.Server event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // Recipe providers are not data providers any more - the Runner owns the output.
        event.addProvider(new LevelUpgradesRecipes.Runner(packOutput, lookupProvider));
        // This mod has no blocks, so no block tags for the item tags to copy from.
        CompletableFuture<TagsProvider.TagLookup<Block>> noBlockTags = CompletableFuture.completedFuture(TagsProvider.TagLookup.empty());
        event.addProvider(new ForgeItemTagProvider(packOutput, lookupProvider, noBlockTags, Constants.MOD_ID, new LevelUpgradesItemTagProvider(packOutput, lookupProvider, noBlockTags)));
    }

    /** Client datagen: item models, the lang file and the manual. */
    private void gatherClientData(final GatherDataEvent.Client event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();

        event.addProvider(new LevelUpgradesItemModelProvider(packOutput));
        event.addProvider(new LevelUpgradesLanguageProvider(packOutput));
        event.addProvider(new LevelUpgradesManualProvider(packOutput));
    }
}
