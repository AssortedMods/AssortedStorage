package com.grim3212.assorted.chests.common.handlers;

import com.grim3212.assorted.chests.Constants;
import com.grim3212.assorted.lib.core.creative.CreativeTabItems;
import com.grim3212.assorted.lib.core.creative.SharedCreativeTabs;
import com.grim3212.assorted.chests.common.block.ChestsBlocks;
import com.grim3212.assorted.lib.family.Families;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** This part's share of the Assorted Storage tab, which every part asks for and the first to load registers. */
public class ChestsCreativeItems {

    public static final ResourceKey<CreativeModeTab> TAB = Families.tab(Constants.FAMILY_ID);

    public static void init() {
        SharedCreativeTabs.add(TAB, 700, ChestsCreativeItems::materialChests);
    }

    private static List<ItemStack> materialChests() {
        CreativeTabItems items = new CreativeTabItems();
        ChestsBlocks.CHESTS.forEach((mat, chest) -> items.addIfObtainable(chest.get(), mat.getMaterial()));
        return items.getItems();
    }
}
