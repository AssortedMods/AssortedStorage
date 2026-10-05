package com.grim3212.assorted.chests.common.item;

import com.grim3212.assorted.chests.Constants;
import com.grim3212.assorted.lib.core.storage.StorageInfo;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public class ChestsDataComponents {

    // Under the family's id so every Assorted Storage part shares the one component; the first loaded registers it.
    public static final Supplier<DataComponentType<StorageInfo>> STORAGE_INFO = StorageInfo.type(Identifier.fromNamespaceAndPath(Constants.FAMILY_ID, "storage_info"));

    // Runs before ChestsBlocks, whose items carry this as a default component.
    public static void init() {
    }
}
