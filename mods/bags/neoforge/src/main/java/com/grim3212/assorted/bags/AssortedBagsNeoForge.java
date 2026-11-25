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
import com.grim3212.assorted.lib.data.ForgeRegistryEntries;
import com.grim3212.assorted.lib.inventory.ForgePlatformInventoryStorageHandlerUnsided;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@Mod(Constants.MOD_ID)
public class AssortedBagsNeoForge {

    public AssortedBagsNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherData);
        modBus.addListener(this::registerCapabilities);

        BagsCommonMod.init();
    }

    /** One event for both halves; each provider is added for the half its output belongs to. */
    private void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        ForgeRegistryEntries.of(Constants.MOD_ID).recipes(BagsRecipes::new).addTo(event);
        ForgeBlockTagProvider blockTagProvider = generator.addProvider(event.includeServer(), new ForgeBlockTagProvider(packOutput, lookupProvider, Constants.MOD_ID, fileHelper, new BagsBlockTagProvider(packOutput, lookupProvider)));
        generator.addProvider(event.includeServer(), new ForgeItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), Constants.MOD_ID, fileHelper, new BagsItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter())));

        generator.addProvider(event.includeClient(), new BagsItemModelProvider(packOutput, fileHelper));
        generator.addProvider(event.includeClient(), new BagsLanguageProvider(packOutput));
        generator.addProvider(event.includeClient(), new BagsManualProvider(packOutput));
    }

    /** Exposes the bags as item handlers ({@code Capabilities.ItemHandler.ITEM}). */
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
        event.registerItem(Capabilities.ItemHandler.ITEM, (stack, context) -> {
            if (stack.isEmpty() || stack.getCount() > 1 || !(stack.getItem() instanceof IInventoryItem inv)) {
                return null;
            }
            return ((ForgePlatformInventoryStorageHandlerUnsided) inv.getStorageHandler(stack)).getCapability();
        }, item);
    }
}
