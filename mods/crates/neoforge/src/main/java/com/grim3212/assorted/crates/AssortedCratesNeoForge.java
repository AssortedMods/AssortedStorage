package com.grim3212.assorted.crates;

import com.grim3212.assorted.crates.client.data.CratesBlockstateProvider;
import com.grim3212.assorted.crates.client.data.CratesItemModelProvider;
import com.grim3212.assorted.crates.client.data.CratesLanguageProvider;
import com.grim3212.assorted.crates.client.data.CratesManualProvider;
import com.grim3212.assorted.crates.common.block.blockentity.CratesBlockEntityTypes;
import com.grim3212.assorted.crates.data.CratesBlockLoot;
import com.grim3212.assorted.crates.data.CratesBlockTagProvider;
import com.grim3212.assorted.crates.data.CratesItemTagProvider;
import com.grim3212.assorted.crates.data.CratesRecipes;
import com.grim3212.assorted.lib.core.inventory.IInventoryBlockEntity;
import com.grim3212.assorted.lib.data.ForgeBlockTagProvider;
import com.grim3212.assorted.lib.data.ForgeItemTagProvider;
import com.grim3212.assorted.lib.data.ForgeRegistryEntries;
import com.grim3212.assorted.lib.inventory.ForgePlatformInventoryStorageHandlerUnsided;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@Mod(Constants.MOD_ID)
public class AssortedCratesNeoForge {

    public AssortedCratesNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherData);
        modBus.addListener(this::registerCapabilities);

        CratesCommonMod.init();
    }

    /**
     * One event for both halves; each provider is added for the half its output belongs to. The two
     * model providers split the mod between them so they never write the same file.
     */
    private void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        ForgeRegistryEntries.of(Constants.MOD_ID).recipes(CratesRecipes::new).blockLoot(CratesBlockLoot::new).addTo(event);
        ForgeBlockTagProvider blockTagProvider = generator.addProvider(event.includeServer(), new ForgeBlockTagProvider(packOutput, lookupProvider, Constants.MOD_ID, fileHelper, new CratesBlockTagProvider(packOutput, lookupProvider)));
        generator.addProvider(event.includeServer(), new ForgeItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), Constants.MOD_ID, fileHelper, new CratesItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter())));

        generator.addProvider(event.includeClient(), new CratesBlockstateProvider(packOutput, fileHelper));
        generator.addProvider(event.includeClient(), new CratesItemModelProvider(packOutput, fileHelper));
        generator.addProvider(event.includeClient(), new CratesLanguageProvider(packOutput));
        generator.addProvider(event.includeClient(), new CratesManualProvider(packOutput));
    }

    /**
     * Exposes the crates and the controller as item handlers ({@code Capabilities.ItemHandler.BLOCK}),
     * the NeoForge side of Fabric's {@code ItemStorage.SIDED}.
     */
    private void registerCapabilities(final RegisterCapabilitiesEvent event) {
        registerBlockEntity(event, CratesBlockEntityTypes.CRATE);
        registerBlockEntity(event, CratesBlockEntityTypes.CRATE_CONTROLLER);
        registerBlockEntity(event, CratesBlockEntityTypes.CRATE_COMPACTING);
    }

    private static <BE extends BlockEntity> void registerBlockEntity(RegisterCapabilitiesEvent event, IRegistryObject<BlockEntityType<BE>> type) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, type.get(), (blockEntity, side) -> {
            if (blockEntity.isRemoved() || !(blockEntity instanceof IInventoryBlockEntity inv)) {
                return null;
            }
            return ((ForgePlatformInventoryStorageHandlerUnsided) inv.getStorageHandler()).getCapability();
        });
    }
}
