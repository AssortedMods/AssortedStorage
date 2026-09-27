package com.grim3212.assorted.containers.common.handlers;

import com.grim3212.assorted.containers.Family;
import com.grim3212.assorted.containers.common.block.ContainersBlocks;
import com.grim3212.assorted.lib.core.creative.CreativeTabItems;
import com.grim3212.assorted.lib.core.creative.SharedCreativeTabs;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** This part's share of the Assorted Storage tab, which every part asks for and the first to load registers. */
public class ContainersCreativeItems {

    public static final ResourceKey<CreativeModeTab> TAB = SharedCreativeTabs.tab(Identifier.fromNamespaceAndPath(Family.ID, "tab"), Family.ICONS);

    private static List<ItemStack> getCreativeItems() {
        CreativeTabItems items = new CreativeTabItems();

        items.add(ContainersBlocks.WOOD_CABINET.get());
        items.add(ContainersBlocks.GLASS_CABINET.get());
        items.add(ContainersBlocks.GOLD_SAFE.get());
        items.add(ContainersBlocks.OBSIDIAN_SAFE.get());
        items.add(ContainersBlocks.LOCKER.get());
        items.add(ContainersBlocks.ITEM_TOWER.get());
        ContainersBlocks.WAREHOUSE_CRATES.values().forEach(crate -> items.add(crate.get()));

        return items.getItems();
    }

    public static void init() {
        // After the level upgrades and before the locked chests, as the tab was when this was all one mod.
        SharedCreativeTabs.add(TAB, 500, ContainersCreativeItems::getCreativeItems);
    }
}
