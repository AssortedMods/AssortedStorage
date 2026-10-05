package com.grim3212.assorted.crates.config;

import com.grim3212.assorted.lib.config.ConfigurationType;
import com.grim3212.assorted.lib.config.IConfigurationBuilder;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.crates.Constants;

import java.util.function.Supplier;

public class CratesClientConfig {

    public final Supplier<Integer> crateMaxRenderDistance;

    public CratesClientConfig() {
        final IConfigurationBuilder builder = Services.CONFIG.createBuilder(ConfigurationType.CLIENT_ONLY, Constants.MOD_ID + "-client");

        crateMaxRenderDistance = builder.defineInteger("crates.crateMaxRenderDistance", 16, 1, 256, "Set this to the maximum distance you would like to still render Storage Crate items.");

        builder.setup();
    }
}
