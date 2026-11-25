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
public class AssortedContainersNeoForge {

    /**
     * {@code FMLJavaModLoadingContext} is gone; the mod event bus and the mod container are injected
     * into the {@code @Mod} constructor instead.
     */
    public AssortedContainersNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherData);
        modBus.addListener(this::registerCapabilities);

        ContainersCommonMod.init();
    }

    /**
     * Datagen. NeoForge 21.1 has one event for both halves; each provider is added for the half it
     * belongs to, so a run of only one half still writes its files.
     */
    private void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        ForgeRegistryEntries.of(Constants.MOD_ID).recipes(ContainersRecipes::new).blockLoot(ContainersBlockLoot::new).addTo(event);
        ForgeBlockTagProvider blockTagProvider = generator.addProvider(event.includeServer(), new ForgeBlockTagProvider(packOutput, lookupProvider, Constants.MOD_ID, fileHelper, new ContainersBlockTagProvider(packOutput, lookupProvider)));
        generator.addProvider(event.includeServer(), new ForgeItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), Constants.MOD_ID, fileHelper, new ContainersItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter())));

        generator.addProvider(event.includeClient(), new ContainersBlockstateProvider(packOutput, fileHelper));
        generator.addProvider(event.includeClient(), new ContainersLanguageProvider(packOutput));
        generator.addProvider(event.includeClient(), new ContainersManualProvider(packOutput));
    }

    /**
     * Exposes the storage blocks as item handlers ({@code Capabilities.ItemHandler.BLOCK}), the NeoForge
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
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, type.get(), (blockEntity, side) -> {
            if (blockEntity.isRemoved() || !(blockEntity instanceof IInventoryBlockEntity inv)) {
                return null;
            }
            return ((ForgePlatformInventoryStorageHandlerUnsided) inv.getStorageHandler()).getCapability();
        });
    }
}
