package com.grim3212.assorted.barrels.common.handlers;

import com.grim3212.assorted.lib.core.creative.CreativeTabItems;
import com.grim3212.assorted.lib.core.creative.SharedCreativeTabs;
import com.grim3212.assorted.barrels.BarrelsCommonMod;
import com.grim3212.assorted.barrels.Family;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.barrels.common.block.BarrelsBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** This part's share of the Assorted Storage tab, which every part asks for and the first to load registers. */
public class BarrelsCreativeItems {

    public static final ResourceKey<CreativeModeTab> TAB = SharedCreativeTabs.tab(Identifier.fromNamespaceAndPath(Family.ID, "tab"), Family.ICONS);

    public static void init() {
        SharedCreativeTabs.add(TAB, 720, BarrelsCreativeItems::materialBarrels);
    }

    private static List<ItemStack> materialBarrels() {
        CreativeTabItems items = new CreativeTabItems();
        BarrelsBlocks.BARRELS.forEach((mat, barrel) -> {
            if (canNotCraft(mat)) {
                return;
            }

            items.add(barrel.get());
        });
        return items.getItems();
    }

    private static boolean canNotCraft(StorageMaterial type) {
        // getTag returned an Optional<HolderSet>; getTagOrEmpty yields the holders directly, so
        // "tag exists but is empty" collapses to a plain emptiness check.
        return BarrelsCommonMod.COMMON_CONFIG.hideUncraftableItems.get()
                && !BuiltInRegistries.ITEM.getTagOrEmpty(type.getMaterial()).iterator().hasNext();
    }
}
