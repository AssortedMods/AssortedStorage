package com.grim3212.assorted.containers;

import com.grim3212.assorted.containers.client.data.ContainersBlockstateProvider;
import com.grim3212.assorted.containers.client.data.ContainersLanguageProvider;
import com.grim3212.assorted.containers.client.data.ContainersManualProvider;
import com.grim3212.assorted.containers.common.block.blockentity.ContainersBlockEntityTypes;
import com.grim3212.assorted.containers.data.ContainersBlockLoot;
import com.grim3212.assorted.containers.data.ContainersBlockTagProvider;
import com.grim3212.assorted.containers.data.ContainersItemTagProvider;
import com.grim3212.assorted.containers.data.ContainersRecipes;
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
public class AssortedContainersNeoForge {

    /**
     * {@code FMLJavaModLoadingContext} is gone; the mod event bus and the mod container are injected
     * into the {@code @Mod} constructor instead.
     */
    public AssortedContainersNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherServerData);
        modBus.addListener(this::gatherClientData);
        modBus.addListener(this::registerCapabilities);

        ContainersCommonMod.init();
    }

    /**
     * Server datagen. The server and client halves are separate events; if the wrong one runs, the
     * build still succeeds, with "All providers took: 0 ms".
     */
    private void gatherServerData(final GatherDataEvent.Server event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // Recipe providers are not data providers any more - the Runner owns the output.
        event.addProvider(new ContainersRecipes.Runner(packOutput, lookupProvider));
        ForgeBlockTagProvider blockTagProvider = event.addProvider(new ForgeBlockTagProvider(packOutput, lookupProvider, Constants.MOD_ID, new ContainersBlockTagProvider(packOutput, lookupProvider)));
        event.addProvider(new ForgeItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), Constants.MOD_ID, new ContainersItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter())));
        event.addProvider(new LootTableProvider(packOutput, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(ContainersBlockLoot::new, LootContextParamSets.BLOCK)), lookupProvider));
    }

    /** Client datagen: block states, block and item models, the lang file and the manual. */
    private void gatherClientData(final GatherDataEvent.Client event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();

        event.addProvider(new ContainersBlockstateProvider(packOutput));
        event.addProvider(new ContainersLanguageProvider(packOutput));
        event.addProvider(new ContainersManualProvider(packOutput));
    }

    /**
     * Exposes the storage blocks as item handlers ({@code Capabilities.Item.BLOCK}), the NeoForge
     * side of Fabric's {@code ItemStorage.SIDED}.
     */
    private void registerCapabilities(final RegisterCapabilitiesEvent event) {
        registerBlockEntity(event, ContainersBlockEntityTypes.WOOD_CABINET);
        registerBlockEntity(event, ContainersBlockEntityTypes.GLASS_CABINET);
        registerBlockEntity(event, ContainersBlockEntityTypes.GOLD_SAFE);
        registerBlockEntity(event, ContainersBlockEntityTypes.OBSIDIAN_SAFE);
        registerBlockEntity(event, ContainersBlockEntityTypes.LOCKER);
        registerBlockEntity(event, ContainersBlockEntityTypes.ITEM_TOWER);
        registerBlockEntity(event, ContainersBlockEntityTypes.WAREHOUSE_CRATE);
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
