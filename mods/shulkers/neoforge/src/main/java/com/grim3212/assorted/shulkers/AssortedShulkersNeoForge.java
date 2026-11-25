package com.grim3212.assorted.shulkers;

import com.grim3212.assorted.lib.core.inventory.IInventoryBlockEntity;
import com.grim3212.assorted.lib.data.ForgeBlockTagProvider;
import com.grim3212.assorted.lib.data.ForgeItemTagProvider;
import com.grim3212.assorted.lib.data.ForgeRegistryEntries;
import com.grim3212.assorted.lib.inventory.ForgePlatformInventoryStorageHandlerUnsided;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.shulkers.client.data.ShulkersBlockstateProvider;
import com.grim3212.assorted.shulkers.client.data.ShulkersLanguageProvider;
import com.grim3212.assorted.shulkers.client.data.ShulkersManualProvider;
import com.grim3212.assorted.shulkers.client.data.ShulkersSpriteSourceProvider;
import com.grim3212.assorted.shulkers.common.block.ShulkersBlocks;
import com.grim3212.assorted.shulkers.common.block.blockentity.ShulkersBlockEntityTypes;
import com.grim3212.assorted.shulkers.data.ShulkersBlockLoot;
import com.grim3212.assorted.shulkers.data.ShulkersBlockTagProvider;
import com.grim3212.assorted.shulkers.data.ShulkersItemTagProvider;
import com.grim3212.assorted.shulkers.data.ShulkersRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@Mod(Constants.MOD_ID)
public class AssortedShulkersNeoForge {

    /**
     * {@code FMLJavaModLoadingContext} is gone; the mod event bus and the mod container are injected
     * into the {@code @Mod} constructor instead.
     */
    public AssortedShulkersNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::setup);
        modBus.addListener(this::gatherData);
        modBus.addListener(this::registerCapabilities);

        ShulkersCommonMod.init();
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ShulkersBlocks::initDispenserHandlers);
    }

    /** One event for both halves; each provider is added for the half its output belongs to. */
    private void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        ForgeRegistryEntries.of(Constants.MOD_ID).recipes(ShulkersRecipes::new).blockLoot(ShulkersBlockLoot::new).addTo(event);
        ForgeBlockTagProvider blockTagProvider = generator.addProvider(event.includeServer(), new ForgeBlockTagProvider(packOutput, lookupProvider, Constants.MOD_ID, fileHelper, new ShulkersBlockTagProvider(packOutput, lookupProvider)));
        generator.addProvider(event.includeServer(), new ForgeItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), Constants.MOD_ID, fileHelper, new ShulkersItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter())));

        generator.addProvider(event.includeClient(), new ShulkersBlockstateProvider(packOutput, fileHelper));
        generator.addProvider(event.includeClient(), new ShulkersSpriteSourceProvider(packOutput, lookupProvider, fileHelper));
        generator.addProvider(event.includeClient(), new ShulkersLanguageProvider(packOutput));
        generator.addProvider(event.includeClient(), new ShulkersManualProvider(packOutput));
    }

    /**
     * Exposes the shulker boxes as item handlers ({@code Capabilities.ItemHandler.BLOCK}), the NeoForge
     * side of Fabric's {@code ItemStorage.SIDED}.
     */
    private void registerCapabilities(final RegisterCapabilitiesEvent event) {
        registerBlockEntity(event, ShulkersBlockEntityTypes.LOCKED_SHULKER_BOX);
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
