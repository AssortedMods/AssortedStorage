package com.grim3212.assorted.bags.common.item;

import com.grim3212.assorted.bags.Family;
import com.grim3212.assorted.lib.core.storage.StorageInfo;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public class BagsDataComponents {

    // Shared through Assorted Lib under the family's id, so every storage mod puts the same component on its items
    public static final Supplier<DataComponentType<StorageInfo>> STORAGE_INFO = StorageInfo.type(Identifier.fromNamespaceAndPath(Family.ID, "storage_info"));

    // Runs before BagsItems, whose items carry this as a default component.
    public static void init() {
    }
}
