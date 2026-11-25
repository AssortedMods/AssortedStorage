package com.grim3212.assorted.hoppers.common.item;

import com.grim3212.assorted.hoppers.Constants;
import com.grim3212.assorted.lib.core.storage.StorageInfo;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class HoppersDataComponents {

    // Shared through Assorted Lib under the family's id, so whichever storage mod loads first registers it
    public static final Supplier<DataComponentType<StorageInfo>> STORAGE_INFO = StorageInfo.type(ResourceLocation.fromNamespaceAndPath(Constants.FAMILY_ID, "storage_info"));

    // Runs before HoppersBlocks, whose items carry this as a default component.
    public static void init() {
    }
}
