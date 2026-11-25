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
public class AssortedHoppersNeoForge {

    public AssortedHoppersNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherData);
        modBus.addListener(this::registerCapabilities);

        HoppersCommonMod.init();
    }

    /** One event for both halves; each provider is added for the half its output belongs to. */
    private void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        ForgeRegistryEntries.of(Constants.MOD_ID).recipes(HoppersRecipes::new).blockLoot(HoppersBlockLoot::new).addTo(event);
        ForgeBlockTagProvider blockTagProvider = generator.addProvider(event.includeServer(), new ForgeBlockTagProvider(packOutput, lookupProvider, Constants.MOD_ID, fileHelper, new HoppersBlockTagProvider(packOutput, lookupProvider)));
        generator.addProvider(event.includeServer(), new ForgeItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), Constants.MOD_ID, fileHelper, new HoppersItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter())));

        generator.addProvider(event.includeClient(), new HoppersBlockstateProvider(packOutput, fileHelper));
        generator.addProvider(event.includeClient(), new HoppersLanguageProvider(packOutput));
        generator.addProvider(event.includeClient(), new HoppersManualProvider(packOutput));
    }

    /**
     * Exposes the hoppers as item handlers ({@code Capabilities.ItemHandler.BLOCK}), the NeoForge side of
     * Fabric's {@code ItemStorage.SIDED}.
     */
    private void registerCapabilities(final RegisterCapabilitiesEvent event) {
        registerBlockEntity(event, HoppersBlockEntityTypes.LOCKED_HOPPER);
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
