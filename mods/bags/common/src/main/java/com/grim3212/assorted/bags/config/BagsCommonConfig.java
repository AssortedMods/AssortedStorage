package com.grim3212.assorted.bags.config;

import com.grim3212.assorted.bags.Constants;
import com.grim3212.assorted.lib.config.ConfigurationType;
import com.grim3212.assorted.lib.config.IConfigurationBuilder;
import com.grim3212.assorted.lib.platform.Services;

import java.util.function.Supplier;

public class BagsCommonConfig {

    public final Supplier<Boolean> hideUncraftableItems;

    public BagsCommonConfig() {
        final IConfigurationBuilder builder = Services.CONFIG.createBuilder(ConfigurationType.NOT_SYNCED, Constants.MOD_ID + "-common");

        hideUncraftableItems = builder.defineBoolean("general.hideUncraftableItems", false, "For any item that is unobtainable (like missing materials from other mods) hide it from the creative menu / JEI.");

        builder.setup();
    }
}
