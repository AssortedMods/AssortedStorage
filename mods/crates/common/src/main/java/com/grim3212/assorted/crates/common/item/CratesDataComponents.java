package com.grim3212.assorted.crates.common.item;

import com.grim3212.assorted.lib.core.storage.StorageInfo;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.crates.Constants;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public class CratesDataComponents {

    public static final RegistryProvider<DataComponentType<?>> DATA_COMPONENTS = RegistryProvider.create(Registries.DATA_COMPONENT_TYPE, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);

    // In the family's namespace, so every Assorted Storage mod puts the same component on its items and the first one loaded registers it
    public static final Supplier<DataComponentType<StorageInfo>> STORAGE_INFO = StorageInfo.type(Identifier.fromNamespaceAndPath(Constants.FAMILY_ID, "storage_info"));
    public static final IRegistryObject<DataComponentType<UpgradeModeInfo>> UPGRADE_MODE_INFO = DATA_COMPONENTS.register("upgrade_mode_info",
            () -> new DataComponentType.Builder<UpgradeModeInfo>().persistent(UpgradeModeInfo.CODEC).networkSynchronized(UpgradeModeInfo.STREAM_CODEC).build());

    // Runs before CratesBlocks and CratesItems, whose items carry these as default components.
    public static void init() {
        Services.PLATFORM.showComponentTooltip(UPGRADE_MODE_INFO);
    }
}
