package com.grim3212.assorted.levelupgrades.client.data;

import com.grim3212.assorted.levelupgrades.Constants;
import com.grim3212.assorted.levelupgrades.common.item.LevelUpgradesItems;
import com.grim3212.assorted.levelupgrades.common.item.upgrades.LevelUpgradeItem;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;

import java.util.stream.Stream;

/** Item models for the level upgrades, which are this mod's only items. */
public class LevelUpgradesItemModelProvider extends ModelProvider {

    public LevelUpgradesItemModelProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    public String getName() {
        return "Assorted Level Upgrades item models";
    }

    /** This mod has no blocks, so there is nothing for {@link BlockModelGenerators} to do. */
    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.empty();
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        for (IRegistryObject<LevelUpgradeItem> levelUpgrade : LevelUpgradesItems.LEVEL_UPGRADES.values()) {
            itemModels.generateFlatItem(levelUpgrade.get(), ModelTemplates.FLAT_ITEM);
        }
    }
}
