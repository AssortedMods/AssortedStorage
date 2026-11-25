package com.grim3212.assorted.levelupgrades.client.data;

import com.grim3212.assorted.levelupgrades.Constants;
import com.grim3212.assorted.levelupgrades.common.item.LevelUpgradesItems;
import com.grim3212.assorted.levelupgrades.common.item.upgrades.LevelUpgradeItem;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/** Item models for the level upgrades, which are this mod's only items. */
public class LevelUpgradesItemModelProvider extends ItemModelProvider {

    public LevelUpgradesItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Assorted Level Upgrades item models";
    }

    @Override
    protected void registerModels() {
        for (IRegistryObject<LevelUpgradeItem> levelUpgrade : LevelUpgradesItems.LEVEL_UPGRADES.values()) {
            this.basicItem(levelUpgrade.get());
        }
    }
}
