package com.grim3212.assorted.shulkers.common.handlers;

import com.grim3212.assorted.lib.core.creative.CreativeTabItems;
import com.grim3212.assorted.lib.core.creative.SharedCreativeTabs;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.family.Families;
import com.grim3212.assorted.shulkers.Constants;
import com.grim3212.assorted.shulkers.ShulkersCommonMod;
import com.grim3212.assorted.shulkers.common.block.ShulkersBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** This part's share of the Assorted Storage tab, which every part asks for and the first to load registers. */
public class ShulkersCreativeItems {

    public static final ResourceKey<CreativeModeTab> TAB = Families.tab(Constants.FAMILY_ID);

    public static void init() {
        SharedCreativeTabs.add(TAB, 710, ShulkersCreativeItems::materialItems);
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
