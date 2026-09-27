package com.grim3212.assorted.shulkers.common.handlers;

import com.grim3212.assorted.lib.core.creative.CreativeTabItems;
import com.grim3212.assorted.lib.core.creative.SharedCreativeTabs;
import com.grim3212.assorted.lib.core.inventory.locking.StorageUtil;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.shulkers.Family;
import com.grim3212.assorted.shulkers.ShulkersCommonMod;
import com.grim3212.assorted.shulkers.common.block.ShulkersBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** This part's share of the Assorted Storage tab, which every part asks for and the first to load registers. */
public class ShulkersCreativeItems {

    public static final ResourceKey<CreativeModeTab> TAB = SharedCreativeTabs.tab(Identifier.fromNamespaceAndPath(Family.ID, "tab"), Family.ICONS);

    public static void init() {
        // The locked box sits with the other locked containers and the materials with the other materials, as when this was all one mod.
        SharedCreativeTabs.add(TAB, 620, ShulkersCreativeItems::lockedItems);
        SharedCreativeTabs.add(TAB, 710, ShulkersCreativeItems::materialItems);
    }

    private static List<ItemStack> lockedItems() {
        return List.of(StorageUtil.setCodeOnStack("default", new ItemStack(ShulkersBlocks.LOCKED_SHULKER_BOX.get())));
    }

    private static List<ItemStack> materialItems() {
        CreativeTabItems items = new CreativeTabItems();
        ShulkersBlocks.SHULKERS.forEach((mat, shulker) -> {
            if (!canNotCraft(mat)) {
                items.add(shulker.get());
            }
        });
        return items.getItems();
    }

    private static boolean canNotCraft(StorageMaterial type) {
        // getTag returned an Optional<HolderSet>; getTagOrEmpty yields the holders directly, so
        // "tag exists but is empty" collapses to a plain emptiness check.
        return ShulkersCommonMod.COMMON_CONFIG.hideUncraftableItems.get()
                && !BuiltInRegistries.ITEM.getTagOrEmpty(type.getMaterial()).iterator().hasNext();
    }
}
