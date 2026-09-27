package com.grim3212.assorted.levelupgrades;

import com.grim3212.assorted.levelupgrades.common.handlers.LevelUpgradesCreativeItems;
import com.grim3212.assorted.levelupgrades.common.item.LevelUpgradesDataComponents;
import com.grim3212.assorted.levelupgrades.common.item.LevelUpgradesItems;
import com.grim3212.assorted.levelupgrades.config.LevelUpgradesCommonConfig;
import com.grim3212.assorted.lib.migration.MovedIds;

public class LevelUpgradesCommonMod {

    public static final LevelUpgradesCommonConfig COMMON_CONFIG = new LevelUpgradesCommonConfig();

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");

        LevelUpgradesDataComponents.init();
        LevelUpgradesItems.init();
        LevelUpgradesCreativeItems.init();

        // Recipes unlocked when this was all one mod carry over to their new ids.
        MovedIds.inherit(Family.ID, Constants.MOD_ID);
    }
}
