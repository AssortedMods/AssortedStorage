package com.grim3212.assorted.hoppers.common.handlers;

import com.grim3212.assorted.hoppers.Constants;
import com.grim3212.assorted.hoppers.common.block.HoppersBlocks;
import com.grim3212.assorted.lib.core.creative.CreativeTabItems;
import com.grim3212.assorted.lib.core.creative.SharedCreativeTabs;
import com.grim3212.assorted.lib.family.Families;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** This part's share of the Assorted Storage tab, which every part asks for and the first to load registers. */
public class HoppersCreativeItems {

    public static final ResourceKey<CreativeModeTab> TAB = Families.tab(Constants.FAMILY_ID);

    public static void init() {
        SharedCreativeTabs.add(TAB, 730, HoppersCreativeItems::materialItems);
    }

    private static List<ItemStack> materialItems() {
        CreativeTabItems items = new CreativeTabItems();
        HoppersBlocks.HOPPERS.forEach((mat, hopper) -> items.addIfObtainable(hopper.get(), mat.getMaterial()));
        return items.getItems();
    }
}
