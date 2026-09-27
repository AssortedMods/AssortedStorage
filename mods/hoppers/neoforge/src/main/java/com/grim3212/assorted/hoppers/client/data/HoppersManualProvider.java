package com.grim3212.assorted.hoppers.client.data;

import com.grim3212.assorted.hoppers.Constants;
import com.grim3212.assorted.hoppers.Family;
import com.grim3212.assorted.hoppers.common.block.HoppersBlocks;
import com.grim3212.assorted.lib.data.LibManualProvider;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

/**
 * This part's chapter of the Assorted Storage section, which every part shares; the explicit chapter
 * order keeps the section's order whichever parts are installed.
 */
public class HoppersManualProvider extends LibManualProvider {

    public HoppersManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID, Family.ID);
    }

    @Override
    protected void addChapters() {
        this.section(Family.MANUAL_ORDER, Family.ICONS.toArray(Identifier[]::new));

        Block[] hoppers = HoppersBlocks.HOPPERS.values().stream().map(IRegistryObject::get).toArray(Block[]::new);

        ChapterBuilder chapter = this.chapter("hoppers", 3);
        chapter.text("materials");
        chapter.recipes("hoppers", hoppers).opens(hoppers);
    }
}
