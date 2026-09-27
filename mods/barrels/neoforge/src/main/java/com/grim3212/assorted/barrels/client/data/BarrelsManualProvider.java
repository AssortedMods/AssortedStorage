package com.grim3212.assorted.barrels.client.data;

import com.grim3212.assorted.lib.data.LibManualProvider;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.barrels.Constants;
import com.grim3212.assorted.barrels.Family;
import com.grim3212.assorted.barrels.common.block.BarrelsBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

/**
 * This part's chapter of the Assorted Storage section, which every part shares; the explicit chapter orders keep the
 * section's order whichever parts are installed. The materials are read from the map the barrels are registered from.
 */
public class BarrelsManualProvider extends LibManualProvider {

    public BarrelsManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID, Family.ID);
    }

    @Override
    protected void addChapters() {
        this.section(Family.MANUAL_ORDER, Family.ICONS.toArray(Identifier[]::new));

        Block[] barrels = BarrelsBlocks.BARRELS.values().stream().map(IRegistryObject::get).toArray(Block[]::new);

        ChapterBuilder chapter = this.chapter("barrels", 1);
        chapter.text("materials");
        chapter.recipes("barrels", barrels).opens(barrels);
        chapter.items("locked", BarrelsBlocks.LOCKED_BARREL.get()).opens(BarrelsBlocks.LOCKED_BARREL.get());
    }
}
