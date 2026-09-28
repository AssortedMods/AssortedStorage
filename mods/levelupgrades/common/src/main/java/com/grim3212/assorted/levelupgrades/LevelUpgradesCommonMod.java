package com.grim3212.assorted.levelupgrades;

import com.grim3212.assorted.levelupgrades.common.handlers.LevelUpgradesCreativeItems;
import com.grim3212.assorted.levelupgrades.common.item.LevelUpgradesDataComponents;
import com.grim3212.assorted.levelupgrades.common.item.LevelUpgradesItems;
import com.grim3212.assorted.lib.family.Families;
import com.grim3212.assorted.lib.migration.MovedIds;
import net.minecraft.resources.Identifier;

public class LevelUpgradesCommonMod {

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");
        Families.join(Constants.MOD_ID, Constants.FAMILY_ID)
                .icon(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "level_upgrade_gold"), 10)
                .manualOrder(80);

        LevelUpgradesDataComponents.init();
        LevelUpgradesItems.init();
        LevelUpgradesCreativeItems.init();

        // Recipes unlocked when this was all one mod carry over to their new ids.
        MovedIds.inherit(Constants.FAMILY_ID, Constants.MOD_ID);
    }
}
