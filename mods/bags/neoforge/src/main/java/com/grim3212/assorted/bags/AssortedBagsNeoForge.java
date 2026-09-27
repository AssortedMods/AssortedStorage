package com.grim3212.assorted.bags;

import com.grim3212.assorted.bags.client.data.BagsItemModelProvider;
import com.grim3212.assorted.bags.client.data.BagsLanguageProvider;
import com.grim3212.assorted.bags.client.data.BagsManualProvider;
import com.grim3212.assorted.bags.common.item.BagsItems;
import com.grim3212.assorted.bags.data.BagsBlockTagProvider;
import com.grim3212.assorted.bags.data.BagsItemTagProvider;
import com.grim3212.assorted.bags.data.BagsRecipes;
import com.grim3212.assorted.lib.core.inventory.IInventoryItem;
import com.grim3212.assorted.lib.data.ForgeBlockTagProvider;
import com.grim3212.assorted.lib.data.ForgeItemTagProvider;
import com.grim3212.assorted.lib.inventory.ForgePlatformInventoryStorageHandlerUnsided;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@Mod(Constants.MOD_ID)
public class AssortedBagsNeoForge {

    /**
     * {@code FMLJavaModLoadingContext} is gone; the mod event bus and the mod container are injected
     * into the {@code @Mod} constructor instead.
     */
    public AssortedBagsNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherServerData);
        modBus.addListener(this::gatherClientData);
        modBus.addListener(this::registerCapabilities);

        BagsCommonMod.init();
    }

    /**
     * Server datagen. The server and client halves are separate events; if the wrong one runs, the
     * build still succeeds, with "All providers took: 0 ms".
     */
    private void gatherServerData(final GatherDataEvent.Server event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // Recipe providers are not data providers any more - the Runner owns the output.
        event.addProvider(new BagsRecipes.Runner(packOutput, lookupProvider));
        ForgeBlockTagProvider blockTagProvider = event.addProvider(new ForgeBlockTagProvider(packOutput, lookupProvider, Constants.MOD_ID, new BagsBlockTagProvider(packOutput, lookupProvider)));
        event.addProvider(new ForgeItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), Constants.MOD_ID, new BagsItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter())));
    }

    /** Client datagen: item models, the lang file and the manual. */
    private void gatherClientData(final GatherDataEvent.Client event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();

        event.addProvider(new BagsItemModelProvider(packOutput));
        event.addProvider(new BagsLanguageProvider(packOutput));
        event.addProvider(new BagsManualProvider(packOutput));
    }

    /** Exposes the bags as item handlers ({@code Capabilities.Item.ITEM}). */
    private void registerCapabilities(final RegisterCapabilitiesEvent event) {
        registerItem(event, BagsItems.BAG.get());
        registerItem(event, BagsItems.ENDER_BAG.get());
        for (IRegistryObject<? extends Item> bag : BagsItems.BAGS.values()) {
            registerItem(event, bag.get());
        }
    }

    /**
     * The stack-count guard is the one the deleted mixin carried: a stacked bag has no single
     * inventory to expose, so it answers with nothing rather than letting several stacks share one.
     */
    private static void registerItem(RegisterCapabilitiesEvent event, Item item) {
        event.registerItem(Capabilities.Item.ITEM, (stack, context) -> {
            if (stack.isEmpty() || stack.getCount() > 1 || !(stack.getItem() instanceof IInventoryItem inv)) {
                return null;
            }
            return ((ForgePlatformInventoryStorageHandlerUnsided) inv.getStorageHandler(stack)).getCapability();
        }, item);
    }
}
