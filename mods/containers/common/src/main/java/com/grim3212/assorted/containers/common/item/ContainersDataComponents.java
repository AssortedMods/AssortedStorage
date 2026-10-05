package com.grim3212.assorted.containers.common.item;

import com.grim3212.assorted.containers.Constants;
import com.grim3212.assorted.lib.core.storage.StorageInfo;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public class ContainersDataComponents {

    // Shared through Assorted Lib under the family's id, so every storage mod puts the same component on its items
    public static final Supplier<DataComponentType<StorageInfo>> STORAGE_INFO = StorageInfo.type(Identifier.fromNamespaceAndPath(Constants.FAMILY_ID, "storage_info"));

    // Runs before ContainersBlocks, whose items carry this as a default component.
    public static void init() {
    }
}
