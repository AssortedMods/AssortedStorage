package com.grim3212.assorted.crates.config;

import com.grim3212.assorted.lib.config.ConfigurationType;
import com.grim3212.assorted.lib.config.IConfigurationBuilder;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.crates.Constants;

import java.util.function.Supplier;

public class CratesCommonConfig {

    public final Supplier<Integer> maxControllerSearchRange;

    public CratesCommonConfig() {
        final IConfigurationBuilder builder = Services.CONFIG.createBuilder(ConfigurationType.NOT_SYNCED, Constants.MOD_ID + "-common");

        maxControllerSearchRange = builder.defineInteger("crates.maxControllerSearchRange", 64, 1, 500, "What is the maximum distance that Storage Crate Controllers will search for Storage Crates.");

        builder.setup();
    }
}
