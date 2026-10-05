package com.grim3212.assorted.barrels.common.handlers;

import com.grim3212.assorted.barrels.Constants;
import com.grim3212.assorted.lib.core.creative.CreativeTabItems;
import com.grim3212.assorted.lib.core.creative.SharedCreativeTabs;
import com.grim3212.assorted.barrels.common.block.BarrelsBlocks;
import com.grim3212.assorted.lib.family.Families;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** This part's share of the Assorted Storage tab, which every part asks for and the first to load registers. */
public class BarrelsCreativeItems {

    public static final ResourceKey<CreativeModeTab> TAB = Families.tab(Constants.FAMILY_ID);

    public static void init() {
        SharedCreativeTabs.add(TAB, 720, BarrelsCreativeItems::materialBarrels);
    }

    private static List<ItemStack> materialBarrels() {
        CreativeTabItems items = new CreativeTabItems();
        BarrelsBlocks.BARRELS.forEach((mat, barrel) -> items.addIfObtainable(barrel.get(), mat.getMaterial()));
        return items.getItems();
    }
}
