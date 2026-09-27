package com.grim3212.assorted.locks;

import com.grim3212.assorted.lib.core.inventory.IInventoryBlockEntity;
import com.grim3212.assorted.lib.core.inventory.IInventoryItem;
import com.grim3212.assorted.lib.core.inventory.locking.StorageAccessUtil;
import com.grim3212.assorted.lib.data.ForgeBlockTagProvider;
import com.grim3212.assorted.lib.data.ForgeItemTagProvider;
import com.grim3212.assorted.lib.inventory.ForgePlatformInventoryStorageHandlerUnsided;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.locks.client.data.LocksBlockstateProvider;
import com.grim3212.assorted.locks.client.data.LocksItemModelProvider;
import com.grim3212.assorted.locks.client.data.LocksLanguageProvider;
import com.grim3212.assorted.locks.client.data.LocksManualProvider;
import com.grim3212.assorted.locks.client.data.LocksSpriteSourceProvider;
import com.grim3212.assorted.locks.common.block.blockentity.LocksBlockEntityTypes;
import com.grim3212.assorted.locks.common.item.LocksItems;
import com.grim3212.assorted.locks.compat.curios.CuriosHelper;
import com.grim3212.assorted.locks.data.LocksBlockLoot;
import com.grim3212.assorted.locks.data.LocksBlockTagProvider;
import com.grim3212.assorted.locks.data.LocksDataMapProvider;
import com.grim3212.assorted.locks.data.LocksItemTagProvider;
import com.grim3212.assorted.locks.data.LocksRecipes;
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
public class AssortedLocksNeoForge {

    /**
     * {@code FMLJavaModLoadingContext} is gone; the mod event bus and the mod container are injected
     * into the {@code @Mod} constructor instead.
     */
    public AssortedLocksNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherServerData);
        modBus.addListener(this::gatherClientData);
        modBus.addListener(this::registerCapabilities);

        LocksCommonMod.init();

        // A key worn in a Curios slot opens its locks too
        if (Services.PLATFORM.isModLoaded("curios")) {
            StorageAccessUtil.registerKeySource(CuriosHelper::hasCodeMatch);
        }
    }

    /**
     * Server datagen. The server and client halves are separate events; if the wrong one runs, the
     * build still succeeds, with "All providers took: 0 ms".
     */
    private void gatherServerData(final GatherDataEvent.Server event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // Recipe providers are not data providers any more - the Runner owns the output.
        event.addProvider(new LocksRecipes.Runner(packOutput, lookupProvider));
        ForgeBlockTagProvider blockTagProvider = event.addProvider(new ForgeBlockTagProvider(packOutput, lookupProvider, Constants.MOD_ID, new LocksBlockTagProvider(packOutput, lookupProvider)));
        event.addProvider(new ForgeItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), Constants.MOD_ID, new LocksItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter())));
        event.addProvider(new LootTableProvider(packOutput, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(LocksBlockLoot::new, LootContextParamSets.BLOCK)), lookupProvider));
        event.addProvider(new LocksDataMapProvider(packOutput, lookupProvider));
    }

    /**
     * Client datagen: block states and models, item models and the lang file. The two model
     * providers split the mod between them so they never write the same file.
     */
    private void gatherClientData(final GatherDataEvent.Client event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();

        event.addProvider(new LocksBlockstateProvider(packOutput));
        event.addProvider(new LocksItemModelProvider(packOutput));
        event.addProvider(new LocksSpriteSourceProvider(packOutput, event.getLookupProvider()));
        event.addProvider(new LocksLanguageProvider(packOutput));
        event.addProvider(new LocksManualProvider(packOutput));
    }

    /**
     * Exposes the key ring and the locked containers as item handlers. The stack-count guard keeps a stacked ring, which has no single
     * inventory, from answering at all.
     */
    private void registerCapabilities(final RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.Item.ITEM, (stack, context) -> {
            if (stack.isEmpty() || stack.getCount() > 1 || !(stack.getItem() instanceof IInventoryItem inv)) {
                return null;
            }
            return ((ForgePlatformInventoryStorageHandlerUnsided) inv.getStorageHandler(stack)).getCapability();
        }, LocksItems.KEY_RING.get());

        registerBlockEntity(event, LocksBlockEntityTypes.LOCKED_CHEST);
        registerBlockEntity(event, LocksBlockEntityTypes.LOCKED_ENDER_CHEST);
        registerBlockEntity(event, LocksBlockEntityTypes.LOCKED_BARREL);
        registerBlockEntity(event, LocksBlockEntityTypes.LOCKED_HOPPER);
        registerBlockEntity(event, LocksBlockEntityTypes.LOCKED_SHULKER_BOX);
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
