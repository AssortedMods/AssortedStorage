package com.grim3212.assorted.locks.common.item;

import com.grim3212.assorted.lib.core.storage.StorageInfo;
import com.grim3212.assorted.locks.Constants;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public class LocksDataComponents {

    // Shared through Assorted Lib under the family's id, so whichever storage mod loads first registers it
    public static final Supplier<DataComponentType<StorageInfo>> STORAGE_INFO = StorageInfo.type(Identifier.fromNamespaceAndPath(Constants.FAMILY_ID, "storage_info"));

    // Runs before LocksItems, whose keys and locks carry this as a default component.
    public static void init() {
    }
}
