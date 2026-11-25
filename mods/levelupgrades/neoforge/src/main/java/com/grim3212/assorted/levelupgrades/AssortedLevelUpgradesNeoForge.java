package com.grim3212.assorted.levelupgrades;

import com.grim3212.assorted.levelupgrades.client.data.LevelUpgradesItemModelProvider;
import com.grim3212.assorted.levelupgrades.client.data.LevelUpgradesLanguageProvider;
import com.grim3212.assorted.levelupgrades.client.data.LevelUpgradesManualProvider;
import com.grim3212.assorted.levelupgrades.data.LevelUpgradesItemTagProvider;
import com.grim3212.assorted.levelupgrades.data.LevelUpgradesRecipes;
import com.grim3212.assorted.lib.data.ForgeItemTagProvider;
import com.grim3212.assorted.lib.data.ForgeRegistryEntries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@Mod(Constants.MOD_ID)
public class AssortedLevelUpgradesNeoForge {

    public AssortedLevelUpgradesNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherData);

        LevelUpgradesCommonMod.init();
    }

    /** One event for both halves; each provider is added for the half its output belongs to. */
    private void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        ForgeRegistryEntries.of(Constants.MOD_ID).recipes(LevelUpgradesRecipes::new).addTo(event);
        // This mod has no blocks, so no block tags for the item tags to copy from.
        CompletableFuture<TagsProvider.TagLookup<Block>> noBlockTags = CompletableFuture.completedFuture(TagsProvider.TagLookup.empty());
        generator.addProvider(event.includeServer(), new ForgeItemTagProvider(packOutput, lookupProvider, noBlockTags, Constants.MOD_ID, fileHelper, new LevelUpgradesItemTagProvider(packOutput, lookupProvider, noBlockTags)));

        generator.addProvider(event.includeClient(), new LevelUpgradesItemModelProvider(packOutput, fileHelper));
        generator.addProvider(event.includeClient(), new LevelUpgradesLanguageProvider(packOutput));
        generator.addProvider(event.includeClient(), new LevelUpgradesManualProvider(packOutput));
    }
}
