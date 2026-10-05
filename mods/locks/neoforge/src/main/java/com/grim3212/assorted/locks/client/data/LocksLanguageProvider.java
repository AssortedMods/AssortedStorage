package com.grim3212.assorted.locks.client.data;

import com.grim3212.assorted.lib.data.LibLanguageProvider;
import com.grim3212.assorted.locks.Constants;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.DyeColor;

/**
 * Generates the en_us.json of this mod. A block, item or entity whose name is its id in title case
 * needs no line here (see {@link LibLanguageProvider}); these are the names that read differently,
 * and every key that is not a name.
 */
public class LocksLanguageProvider extends LibLanguageProvider {

    /** A blank line between paragraphs; the manual splits its text the way the font does. */
    private static final String BREAK = "\n\n";

    public LocksLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addNames() {
        // The family's tab and manual section, written the same by every Assorted Storage mod.
        this.add("itemGroup.assortedstorage", "Assorted Storage");

        this.add("assortedlocks.container.locksmith_workbench", "Locksmith Workbench");
        this.add("assortedlocks.container.key_ring", "Key Ring");
        this.add("assortedlocks.container.locked_chest", "Locked Chest");
        this.add("assortedlocks.container.locked_ender_chest", "Ender Chest");
        this.add("assortedlocks.container.locked_barrel", "Locked Barrel");
        this.add("assortedlocks.container.locked_hopper", "Locked Hopper");
        this.add("assortedlocks.container.locked_shulker_box", "Locked Shulker Box");

        // Item tag names, which recipe viewers show in place of the raw tag id.
        this.add("tag.item.c.hoppers", "Hoppers");

        // A dyed locked shulker box is named for its color.
        for (DyeColor color : DyeColor.values()) {
            this.add("block.assortedlocks.locked_shulker_box_" + color.getName(), titleCase(color.getName()) + " Locked Shulker Box");
        }

        this.addManual();
    }

    /** The chapter in {@code assets/assortedstorage/manual} names these keys. */
    private void addManual() {
        this.add("manual.assortedstorage.title", "Assorted Storage");
        this.add("manual.assortedstorage.description",
                "Bigger containers, crates, portable storage, and locks for all of it.");

        this.add("manual.assortedstorage.chapter.locking", "Locks and Keys");

        this.add("manual.assortedstorage.chapter.locking.workbench.title", "Locksmith Workbench");
        this.add("manual.assortedstorage.chapter.locking.workbench",
                "The locksmith workbench is where a lock or a key is given its combination. Two locks with the "
                        + "same combination take the same key.");

        this.add("manual.assortedstorage.chapter.locking.locks.title", "Padlocks and Keys");
        this.add("manual.assortedstorage.chapter.locking.locks",
                "Use a padlock on a door, chest, ender chest, barrel, hopper or shulker box and it becomes the locked version of itself. "
                        + "The containers from the other Assorted Storage mods take one the same way." + BREAK
                        + "After that only a key with the matching combination opens it.");

        this.add("manual.assortedstorage.chapter.locking.containers.title", "Locked Containers");
        this.add("manual.assortedstorage.chapter.locking.containers",
                "A locked chest, barrel, hopper or shulker box keeps whatever was inside when the padlock went on. "
                        + "A locked ender chest opens the ender storage tied to its combination instead of your own." + BREAK
                        + "Sneak and use it with an empty hand while you carry the matching key to take the padlock off and get the plain one back.");

        this.add("manual.assortedstorage.chapter.locking.key_ring.title", "Key Ring");
        this.add("manual.assortedstorage.chapter.locking.key_ring",
                "A key ring carries several keys in one slot and tries all of them.");

        this.add("manual.assortedstorage.chapter.locking.doors.title", "Locked Doors");
        this.add("manual.assortedstorage.chapter.locking.doors",
                "Every vanilla door has a locked form, and so do the doors Assorted Building Blocks adds. There is no "
                        + "item to craft. Simply put a padlock on a door that is already placed and it becomes one." + BREAK
                        + "A locked door will not open to a hand, a key of the wrong combination, or redstone.");
    }
}
