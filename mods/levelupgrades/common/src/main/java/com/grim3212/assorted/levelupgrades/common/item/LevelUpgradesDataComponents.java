package com.grim3212.assorted.levelupgrades.common.item;

import com.grim3212.assorted.levelupgrades.Family;
import com.grim3212.assorted.lib.core.storage.StorageInfo;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public class LevelUpgradesDataComponents {

    // Shared through Assorted Lib under the family's id, so whichever storage mod loads first registers it
    public static final Supplier<DataComponentType<StorageInfo>> STORAGE_INFO = StorageInfo.type(Identifier.fromNamespaceAndPath(Family.ID, "storage_info"));

    // Runs before LevelUpgradesItems, whose upgrades carry this as a default component.
    public static void init() {
    }
}
