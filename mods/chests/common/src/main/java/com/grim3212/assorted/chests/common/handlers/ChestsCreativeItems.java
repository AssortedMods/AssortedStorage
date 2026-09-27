package com.grim3212.assorted.chests.common.handlers;

import com.grim3212.assorted.lib.core.creative.CreativeTabItems;
import com.grim3212.assorted.lib.core.creative.SharedCreativeTabs;
import com.grim3212.assorted.chests.ChestsCommonMod;
import com.grim3212.assorted.chests.Family;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.chests.common.block.ChestsBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** This part's share of the Assorted Storage tab, which every part asks for and the first to load registers. */
public class ChestsCreativeItems {

    public static final ResourceKey<CreativeModeTab> TAB = SharedCreativeTabs.tab(Identifier.fromNamespaceAndPath(Family.ID, "tab"), Family.ICONS);

    public static void init() {
        SharedCreativeTabs.add(TAB, 700, ChestsCreativeItems::materialChests);
    }

    private static List<ItemStack> materialChests() {
        CreativeTabItems items = new CreativeTabItems();
        ChestsBlocks.CHESTS.forEach((mat, chest) -> {
            if (canNotCraft(mat)) {
                return;
            }

            items.add(chest.get());
        });
        return items.getItems();
    }

    private static boolean canNotCraft(StorageMaterial type) {
        // getTag returned an Optional<HolderSet>; getTagOrEmpty yields the holders directly, so
        // "tag exists but is empty" collapses to a plain emptiness check.
        return ChestsCommonMod.COMMON_CONFIG.hideUncraftableItems.get()
                && !BuiltInRegistries.ITEM.getTagOrEmpty(type.getMaterial()).iterator().hasNext();
    }
}
