package com.grim3212.assorted.chests.client.data;

import com.grim3212.assorted.lib.data.LibManualProvider;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.chests.Constants;
import com.grim3212.assorted.chests.Family;
import com.grim3212.assorted.chests.common.block.ChestsBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

/**
 * This part's chapter of the Assorted Storage section, which every part shares; the explicit chapter orders keep the
 * section's order whichever parts are installed. The materials are read from the map the chests are registered from.
 */
public class ChestsManualProvider extends LibManualProvider {

    public ChestsManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID, Family.ID);
    }

    @Override
    protected void addChapters() {
        this.section(Family.MANUAL_ORDER, Family.ICONS.toArray(Identifier[]::new));

        Block[] chests = ChestsBlocks.CHESTS.values().stream().map(IRegistryObject::get).toArray(Block[]::new);
        Block[] locked = {ChestsBlocks.LOCKED_CHEST.get(), ChestsBlocks.LOCKED_ENDER_CHEST.get()};

        ChapterBuilder chapter = this.chapter("chests", 0);
        chapter.text("materials");
        chapter.recipes("chests", chests).opens(chests);
        chapter.items("locked", locked).opens(locked);
    }
}
