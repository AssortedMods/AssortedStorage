package com.grim3212.assorted.bags.common.handlers;

import com.grim3212.assorted.bags.BagsCommonMod;
import com.grim3212.assorted.bags.Family;
import com.grim3212.assorted.bags.common.item.BagItem;
import com.grim3212.assorted.bags.common.item.BagsItems;
import com.grim3212.assorted.lib.core.creative.CreativeTabItems;
import com.grim3212.assorted.lib.core.creative.SharedCreativeTabs;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.util.NBTHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** This part's share of the Assorted Storage tab, which every part asks for and the first to load registers. */
public class BagsCreativeItems {

    public static final ResourceKey<CreativeModeTab> TAB = SharedCreativeTabs.tab(Identifier.fromNamespaceAndPath(Family.ID, "tab"), Family.ICONS);

    private static List<ItemStack> getCreativeItems() {
        CreativeTabItems items = new CreativeTabItems();

        items.add(BagsItems.ENDER_BAG.get());
        items.add(BagsItems.BAG.get());

        for (DyeColor color : DyeColor.values()) {
            items.add(NBTHelper.putIntItemStack(new ItemStack(BagsItems.BAG.get()), BagItem.TAG_PRIMARY_COLOR, color.getId()));
        }

        BagsItems.BAGS.forEach((mat, bag) -> {
            if (canNotCraft(mat)) {
                return;
            }

            items.add(bag.get());
        });

        return items.getItems();
    }

    private static boolean canNotCraft(StorageMaterial type) {
        // getTag returned an Optional<HolderSet>; getTagOrEmpty yields the holders directly, so
        // "tag exists but is empty" collapses to a plain emptiness check.
        return BagsCommonMod.COMMON_CONFIG.hideUncraftableItems.get()
                && !BuiltInRegistries.ITEM.getTagOrEmpty(type.getMaterial()).iterator().hasNext();
    }

    public static void init() {
        // After the crates and before the blank upgrade, as the tab was when this was all one mod.
        SharedCreativeTabs.add(TAB, 300, BagsCreativeItems::getCreativeItems);
    }
}
