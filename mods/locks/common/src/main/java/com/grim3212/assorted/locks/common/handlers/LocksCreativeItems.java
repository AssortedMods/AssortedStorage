package com.grim3212.assorted.locks.common.handlers;

import com.grim3212.assorted.lib.core.creative.SharedCreativeTabs;
import com.grim3212.assorted.lib.core.inventory.locking.StorageUtil;
import com.grim3212.assorted.lib.family.Families;
import com.grim3212.assorted.locks.Constants;
import com.grim3212.assorted.locks.common.block.LocksBlocks;
import com.grim3212.assorted.locks.common.item.LocksItems;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;
import java.util.List;

/** This mod's share of the Assorted Storage tab, which every part asks for and the first to load registers. */
public class LocksCreativeItems {

    public static final ResourceKey<CreativeModeTab> TAB = Families.tab(Constants.FAMILY_ID);

    public static void init() {
        // First in the tab, as it was when this was all one mod, with the locked containers right behind.
        SharedCreativeTabs.add(TAB, 100, LocksCreativeItems::items);
        SharedCreativeTabs.add(TAB, 110, LocksCreativeItems::lockedContainers);
    }

    private static List<ItemStack> items() {
        return List.of(new ItemStack(LocksBlocks.LOCKSMITH_WORKBENCH.get()), new ItemStack(LocksItems.LOCKSMITH_KEY.get()),
                new ItemStack(LocksItems.LOCKSMITH_LOCK.get()), new ItemStack(LocksItems.KEY_RING.get()));
    }

    /** Coded with {@code default}, so each shows its padlock. */
    private static List<ItemStack> lockedContainers() {
        return Arrays.stream(LocksBlocks.lockedContainers()).map(LocksCreativeItems::locked).toList();
    }

    private static ItemStack locked(Block block) {
        return StorageUtil.setCodeOnStack("default", new ItemStack(block));
    }
}
