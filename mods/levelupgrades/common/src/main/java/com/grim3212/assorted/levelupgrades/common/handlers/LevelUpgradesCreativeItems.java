package com.grim3212.assorted.levelupgrades.common.handlers;

import com.grim3212.assorted.levelupgrades.Constants;
import com.grim3212.assorted.levelupgrades.LevelUpgradesCommonMod;
import com.grim3212.assorted.levelupgrades.common.item.LevelUpgradesItems;
import com.grim3212.assorted.lib.core.creative.SharedCreativeTabs;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.family.Families;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/** This mod's share of the Assorted Storage tab, which every part asks for and the first to load registers. */
public class LevelUpgradesCreativeItems {

    public static final ResourceKey<CreativeModeTab> TAB = Families.tab(Constants.FAMILY_ID);

    public static void init() {
        // After the crates' blank upgrade, as the tab was when this was all one mod.
        SharedCreativeTabs.add(TAB, 400, LevelUpgradesCreativeItems::items);
    }

    private static List<ItemStack> items() {
        List<ItemStack> items = new ArrayList<>();
        LevelUpgradesItems.LEVEL_UPGRADES.forEach((mat, upgrade) -> {
            if (!canNotCraft(mat)) {
                items.add(new ItemStack(upgrade.get()));
            }
        });
        return items;
    }

    private static boolean canNotCraft(StorageMaterial type) {
        return LevelUpgradesCommonMod.COMMON_CONFIG.hideUncraftableItems.get()
                && !BuiltInRegistries.ITEM.getTagOrEmpty(type.getMaterial()).iterator().hasNext();
    }
}
