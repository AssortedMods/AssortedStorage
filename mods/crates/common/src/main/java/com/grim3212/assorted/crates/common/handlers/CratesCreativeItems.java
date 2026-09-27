package com.grim3212.assorted.crates.common.handlers;

import com.grim3212.assorted.lib.core.creative.CreativeTabItems;
import com.grim3212.assorted.lib.core.creative.SharedCreativeTabs;
import com.grim3212.assorted.crates.Family;
import com.grim3212.assorted.crates.common.block.CratesBlocks;
import com.grim3212.assorted.crates.common.item.CratesItems;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** This part's share of the Assorted Storage tab, which every part asks for and the first to load registers. */
public class CratesCreativeItems {

    public static final ResourceKey<CreativeModeTab> TAB = SharedCreativeTabs.tab(Identifier.fromNamespaceAndPath(Family.ID, "tab"), Family.ICONS);

    private static List<ItemStack> rotatorMajig() {
        CreativeTabItems items = new CreativeTabItems();
        items.add(CratesItems.ROTATOR_MAJIG.get());
        return items.getItems();
    }

    private static List<ItemStack> crates() {
        CreativeTabItems items = new CreativeTabItems();

        items.add(CratesBlocks.CRATE_COMPACTING.get());
        items.add(CratesBlocks.CRATE_CONTROLLER.get());
        items.add(CratesBlocks.CRATE_BRIDGE.get());

        for (CratesBlocks.CrateGroup group : CratesBlocks.CRATES) {
            items.add(group.SINGLE.get());
            items.add(group.DOUBLE.get());
            items.add(group.TRIPLE.get());
            items.add(group.QUADRUPLE.get());
        }

        items.add(CratesItems.VOID_UPGRADE.get());
        items.add(CratesItems.REDSTONE_UPGRADE.get());
        items.add(CratesItems.AMOUNT_UPGRADE.get());
        items.add(CratesItems.GLOW_UPGRADE.get());

        return items.getItems();
    }

    private static List<ItemStack> blankUpgrade() {
        CreativeTabItems items = new CreativeTabItems();
        items.add(CratesItems.BLANK_UPGRADE.get());
        return items.getItems();
    }

    public static void init() {
        // Three places in the tab, as it was when this was all one mod: after the locks, before the bags, just before the level upgrades.
        SharedCreativeTabs.add(TAB, 150, CratesCreativeItems::rotatorMajig);
        SharedCreativeTabs.add(TAB, 200, CratesCreativeItems::crates);
        SharedCreativeTabs.add(TAB, 390, CratesCreativeItems::blankUpgrade);
    }
}
