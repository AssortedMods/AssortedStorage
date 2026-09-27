package com.grim3212.assorted.hoppers;

import com.grim3212.assorted.hoppers.client.data.HoppersBlockstateProvider;
import com.grim3212.assorted.hoppers.client.data.HoppersLanguageProvider;
import com.grim3212.assorted.hoppers.client.data.HoppersManualProvider;
import com.grim3212.assorted.hoppers.common.block.blockentity.HoppersBlockEntityTypes;
import com.grim3212.assorted.hoppers.data.HoppersBlockLoot;
import com.grim3212.assorted.hoppers.data.HoppersBlockTagProvider;
import com.grim3212.assorted.hoppers.data.HoppersItemTagProvider;
import com.grim3212.assorted.hoppers.data.HoppersRecipes;
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
public class AssortedHoppersNeoForge {

    /**
     * {@code FMLJavaModLoadingContext} is gone; the mod event bus and the mod container are injected
     * into the {@code @Mod} constructor instead.
     */
    public AssortedHoppersNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherServerData);
        modBus.addListener(this::gatherClientData);
        modBus.addListener(this::registerCapabilities);

        HoppersCommonMod.init();
    }

    /**
     * Server datagen. The server and client halves are separate events; if the wrong one runs, the
     * build still succeeds, with "All providers took: 0 ms".
     */
    private void gatherServerData(final GatherDataEvent.Server event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // Recipe providers are not data providers any more - the Runner owns the output.
        event.addProvider(new HoppersRecipes.Runner(packOutput, lookupProvider));
        ForgeBlockTagProvider blockTagProvider = event.addProvider(new ForgeBlockTagProvider(packOutput, lookupProvider, Constants.MOD_ID, new HoppersBlockTagProvider(packOutput, lookupProvider)));
        event.addProvider(new ForgeItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), Constants.MOD_ID, new HoppersItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter())));
        event.addProvider(new LootTableProvider(packOutput, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(HoppersBlockLoot::new, LootContextParamSets.BLOCK)), lookupProvider));
    }

    /**
     * Client datagen: block states, models and item models, the lang file and the manual.
     */
    private void gatherClientData(final GatherDataEvent.Client event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();

        event.addProvider(new HoppersBlockstateProvider(packOutput));
        event.addProvider(new HoppersLanguageProvider(packOutput));
        event.addProvider(new HoppersManualProvider(packOutput));
    }

    /**
     * Exposes the hoppers as item handlers ({@code Capabilities.Item.BLOCK}), the NeoForge side of
     * Fabric's {@code ItemStorage.SIDED}.
     */
    private void registerCapabilities(final RegisterCapabilitiesEvent event) {
        registerBlockEntity(event, HoppersBlockEntityTypes.LOCKED_HOPPER);
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
