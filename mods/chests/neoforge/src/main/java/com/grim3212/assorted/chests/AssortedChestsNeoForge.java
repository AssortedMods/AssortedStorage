package com.grim3212.assorted.chests;

import com.grim3212.assorted.chests.client.data.ChestsBlockstateProvider;
import com.grim3212.assorted.chests.client.data.ChestsLanguageProvider;
import com.grim3212.assorted.chests.client.data.ChestsManualProvider;
import com.grim3212.assorted.chests.client.data.ChestsSpriteSourceProvider;
import com.grim3212.assorted.chests.common.block.blockentity.ChestsBlockEntityTypes;
import com.grim3212.assorted.chests.data.ChestsBlockLoot;
import com.grim3212.assorted.chests.data.ChestsBlockTagProvider;
import com.grim3212.assorted.chests.data.ChestsItemTagProvider;
import com.grim3212.assorted.chests.data.ChestsRecipes;
import com.grim3212.assorted.lib.core.inventory.IInventoryBlockEntity;
import com.grim3212.assorted.lib.data.ForgeBlockTagProvider;
import com.grim3212.assorted.lib.data.ForgeItemTagProvider;
import com.grim3212.assorted.lib.inventory.ForgePlatformInventoryStorageHandlerUnsided;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mod(Constants.MOD_ID)
public class AssortedChestsNeoForge {

    /**
     * {@code FMLJavaModLoadingContext} is gone; the mod event bus and the mod container are injected
     * into the {@code @Mod} constructor instead.
     */
    public AssortedChestsNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherServerData);
        modBus.addListener(this::gatherClientData);
        modBus.addListener(this::registerCapabilities);

        ChestsCommonMod.init();
    }

    /**
     * Server datagen. The server and client halves are separate events; if the wrong one runs, the
     * build still succeeds, with "All providers took: 0 ms".
     */
    private void gatherServerData(final GatherDataEvent.Server event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // Recipe providers are not data providers any more - the Runner owns the output.
        event.addProvider(new ChestsRecipes.Runner(packOutput, lookupProvider));
        ForgeBlockTagProvider blockTagProvider = event.addProvider(new ForgeBlockTagProvider(packOutput, lookupProvider, Constants.MOD_ID, new ChestsBlockTagProvider(packOutput, lookupProvider)));
        event.addProvider(new ForgeItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), Constants.MOD_ID, new ChestsItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter())));
        event.addProvider(new LootTableProvider(packOutput, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(ChestsBlockLoot::new, LootContextParamSets.BLOCK)), lookupProvider));
    }

    /** Client datagen: block states and models, sprite sources, the lang file and the manual. */
    private void gatherClientData(final GatherDataEvent.Client event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        event.addProvider(new ChestsBlockstateProvider(packOutput));
        event.addProvider(new ChestsSpriteSourceProvider(packOutput, lookupProvider));
        event.addProvider(new ChestsLanguageProvider(packOutput));
        event.addProvider(new ChestsManualProvider(packOutput));
    }

    /**
     * Exposes the chests as item handlers ({@code Capabilities.Item.BLOCK}), the NeoForge side of
     * Fabric's {@code ItemStorage.SIDED}.
     */
    private void registerCapabilities(final RegisterCapabilitiesEvent event) {
        registerBlockEntity(event, ChestsBlockEntityTypes.LOCKED_CHEST);
        registerBlockEntity(event, ChestsBlockEntityTypes.LOCKED_ENDER_CHEST);
    }

    private static <BE extends BlockEntity> void registerBlockEntity(RegisterCapabilitiesEvent event, IRegistryObject<BlockEntityType<BE>> type) {
        event.registerBlockEntity(Capabilities.Item.BLOCK, type.get(), (blockEntity, side) -> {
            if (blockEntity.isRemoved() || !(blockEntity instanceof IInventoryBlockEntity inv)) {
                return null;
            }
            return ((ForgePlatformInventoryStorageHandlerUnsided) inv.getStorageHandler()).getCapability();
        });
    }
}
