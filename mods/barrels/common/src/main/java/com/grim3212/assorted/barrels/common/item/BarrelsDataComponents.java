package com.grim3212.assorted.barrels.common.item;

import com.grim3212.assorted.lib.core.storage.StorageInfo;
import com.grim3212.assorted.barrels.Family;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public class BarrelsDataComponents {

    // Under the family's id so every Assorted Storage part shares the one component; the first loaded registers it.
    public static final Supplier<DataComponentType<StorageInfo>> STORAGE_INFO = StorageInfo.type(Identifier.fromNamespaceAndPath(Family.ID, "storage_info"));

    // Runs before BarrelsBlocks, whose items carry this as a default component.
    public static void init() {
    }
}
