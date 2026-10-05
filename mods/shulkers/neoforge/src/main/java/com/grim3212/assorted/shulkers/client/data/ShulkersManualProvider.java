package com.grim3212.assorted.shulkers.client.data;

import com.grim3212.assorted.lib.data.LibManualProvider;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.shulkers.Constants;
import com.grim3212.assorted.shulkers.common.block.ShulkersBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;

/**
 * This part's chapter of the Assorted Storage section, which every part shares; the explicit chapter
 * order keeps the section's order whichever parts are installed.
 */
public class ShulkersManualProvider extends LibManualProvider {

    public ShulkersManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID, Constants.FAMILY_ID);
    }

    @Override
    protected void addChapters() {
        Block[] shulkers = ShulkersBlocks.SHULKERS.values().stream().map(IRegistryObject::get).toArray(Block[]::new);

        ChapterBuilder chapter = this.chapter("shulker_boxes", 2);
        chapter.text("materials");
        chapter.recipes("shulker_boxes", shulkers).opens(shulkers);
    }
}
