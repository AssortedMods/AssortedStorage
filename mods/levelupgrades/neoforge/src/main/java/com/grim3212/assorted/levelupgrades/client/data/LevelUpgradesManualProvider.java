package com.grim3212.assorted.levelupgrades.client.data;

import com.grim3212.assorted.levelupgrades.Constants;
import com.grim3212.assorted.levelupgrades.common.item.LevelUpgradesItems;
import com.grim3212.assorted.lib.data.LibManualProvider;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;

/**
 * This mod's chapter of the Assorted Storage section, which every part shares; the explicit chapter order keeps the
 * section's order whichever parts are installed.
 */
public class LevelUpgradesManualProvider extends LibManualProvider {

    public LevelUpgradesManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID, Constants.FAMILY_ID);
    }

    @Override
    protected void addChapters() {
        Item[] upgrades = LevelUpgradesItems.LEVEL_UPGRADES.values().stream().map(IRegistryObject::get).toArray(Item[]::new);
        this.chapter("level_upgrades", 4).recipes("level_upgrades", upgrades).opens(upgrades);
    }
}
