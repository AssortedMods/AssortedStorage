package com.grim3212.assorted.locks.client.data;

import com.grim3212.assorted.lib.data.LibManualProvider;
import com.grim3212.assorted.locks.Constants;
import com.grim3212.assorted.locks.Family;
import com.grim3212.assorted.locks.common.block.LocksBlocks;
import com.grim3212.assorted.locks.common.item.LocksItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

/**
 * This mod's chapter of the Assorted Storage section, which every part shares; the explicit chapter order keeps the
 * section's order whichever parts are installed.
 */
public class LocksManualProvider extends LibManualProvider {

    public LocksManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID, Family.ID);
    }

    @Override
    protected void addChapters() {
        this.section(Family.MANUAL_ORDER, Family.ICONS.toArray(Identifier[]::new));

        ChapterBuilder locking = this.chapter("locking", 7);
        locking.recipes("workbench", LocksBlocks.LOCKSMITH_WORKBENCH.get()).opens(LocksBlocks.LOCKSMITH_WORKBENCH.get());
        locking.recipes("locks", LocksItems.LOCKSMITH_LOCK.get(), LocksItems.LOCKSMITH_KEY.get()).every(50)
                .opens(LocksItems.LOCKSMITH_LOCK.get(), LocksItems.LOCKSMITH_KEY.get());
        locking.recipes("key_ring", LocksItems.KEY_RING.get()).opens(LocksItems.KEY_RING.get());
        locking.items("containers", LocksBlocks.lockedContainers()).opens(LocksBlocks.lockedContainers());
        // A locked door is made by locking one that is already hung, so there is no item to draw.
        locking.text("doors").opens(LocksBlocks.lockedDoors());
    }
}
