package com.grim3212.assorted.barrels;

import com.grim3212.assorted.barrels.client.data.BarrelsBlockstateProvider;
import com.grim3212.assorted.barrels.client.data.BarrelsLanguageProvider;
import com.grim3212.assorted.barrels.client.data.BarrelsManualProvider;
import com.grim3212.assorted.barrels.common.block.blockentity.BarrelsBlockEntityTypes;
import com.grim3212.assorted.barrels.data.BarrelsBlockLoot;
import com.grim3212.assorted.barrels.data.BarrelsBlockTagProvider;
import com.grim3212.assorted.barrels.data.BarrelsItemTagProvider;
import com.grim3212.assorted.barrels.data.BarrelsRecipes;
import com.grim3212.assorted.lib.core.inventory.IInventoryBlockEntity;
import com.grim3212.assorted.lib.data.ForgeBlockTagProvider;
import com.grim3212.assorted.lib.data.ForgeItemTagProvider;
import com.grim3212.assorted.lib.data.ForgeRegistryEntries;
import com.grim3212.assorted.lib.inventory.ForgePlatformInventoryStorageHandlerUnsided;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@Mod(Constants.MOD_ID)
public class AssortedBarrelsNeoForge {

    /**
     * {@code FMLJavaModLoadingContext} is gone; the mod event bus and the mod container are injected
     * into the {@code @Mod} constructor instead.
     */
    public AssortedBarrelsNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherData);
        modBus.addListener(this::registerCapabilities);

        BarrelsCommonMod.init();
    }

    /** One event for both halves; each provider is added for the half its output belongs to. */
    private void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        ForgeRegistryEntries.of(Constants.MOD_ID).recipes(BarrelsRecipes::new).blockLoot(BarrelsBlockLoot::new).addTo(event);
        ForgeBlockTagProvider blockTagProvider = generator.addProvider(event.includeServer(), new ForgeBlockTagProvider(packOutput, lookupProvider, Constants.MOD_ID, fileHelper, new BarrelsBlockTagProvider(packOutput, lookupProvider)));
        generator.addProvider(event.includeServer(), new ForgeItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), Constants.MOD_ID, fileHelper, new BarrelsItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter())));

        generator.addProvider(event.includeClient(), new BarrelsBlockstateProvider(packOutput, fileHelper));
        generator.addProvider(event.includeClient(), new BarrelsLanguageProvider(packOutput));
        generator.addProvider(event.includeClient(), new BarrelsManualProvider(packOutput));
    }

    /**
     * Exposes the barrels as item handlers ({@code Capabilities.ItemHandler.BLOCK}), the NeoForge side of
     * Fabric's {@code ItemStorage.SIDED}.
     */
    private void registerCapabilities(final RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BarrelsBlockEntityTypes.LOCKED_BARREL.get(), (blockEntity, side) -> {
            if (blockEntity.isRemoved() || !(blockEntity instanceof IInventoryBlockEntity inv)) {
                return null;
            }
            return ((ForgePlatformInventoryStorageHandlerUnsided) inv.getStorageHandler()).getCapability();
        });
    }
}
