package com.grim3212.assorted.containers.client.data;

import com.grim3212.assorted.containers.Constants;
import com.grim3212.assorted.containers.common.block.ContainersBlocks;
import com.grim3212.assorted.lib.data.LibManualProvider;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;

/**
 * This part's chapter of the Assorted Storage section, which every part shares; the explicit chapter order keeps the
 * section's order whichever parts are installed. The warehouse crates are read from the map they are registered from.
 */
public class ContainersManualProvider extends LibManualProvider {

    public ContainersManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID, Constants.FAMILY_ID);
    }

    @Override
    protected void addChapters() {
        Block[] warehouse = ContainersBlocks.WAREHOUSE_CRATES.values().stream().map(IRegistryObject::get).toArray(Block[]::new);
        ChapterBuilder furniture = this.chapter("furniture", 8);

        furniture.recipes("locker", ContainersBlocks.LOCKER.get()).opens(ContainersBlocks.LOCKER.get());
        furniture.recipes("cabinets", ContainersBlocks.WOOD_CABINET.get(), ContainersBlocks.GLASS_CABINET.get()).every(50)
                .opens(ContainersBlocks.WOOD_CABINET.get(), ContainersBlocks.GLASS_CABINET.get());
        furniture.recipes("safes", ContainersBlocks.GOLD_SAFE.get(), ContainersBlocks.OBSIDIAN_SAFE.get()).every(50)
                .opens(ContainersBlocks.GOLD_SAFE.get(), ContainersBlocks.OBSIDIAN_SAFE.get());
        furniture.recipes("item_tower", ContainersBlocks.ITEM_TOWER.get()).opens(ContainersBlocks.ITEM_TOWER.get());
        furniture.recipes("warehouse", warehouse).opens(warehouse);
    }
}
