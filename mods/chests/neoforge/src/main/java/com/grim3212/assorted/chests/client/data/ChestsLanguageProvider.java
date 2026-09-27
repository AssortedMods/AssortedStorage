package com.grim3212.assorted.chests.client.data;

import com.grim3212.assorted.lib.data.LibLanguageProvider;
import com.grim3212.assorted.chests.Constants;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import net.minecraft.data.PackOutput;

/**
 * Generates the en_us.json of Assorted Chests. A block whose name is its id in title case needs no
 * line here (see {@link LibLanguageProvider}); these are the names that read differently, and every key that is not a name.
 */
public class ChestsLanguageProvider extends LibLanguageProvider {

    /** A blank line between paragraphs; the manual splits its text the way the font does. */
    private static final String BREAK = "\n\n";

    public ChestsLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addNames() {
        this.add("itemGroup.assortedstorage", "Assorted Storage");

        this.add("assortedchests.container.locked_chest", "Locked Chest");
        this.add("assortedchests.container.locked_ender_chest", "Ender Chest");

        // The material comes first in each name, where the id puts it last.
        for (StorageMaterial material : StorageMaterial.values()) {
            String id = material.toString();
            String name = titleCase(id);

            this.add("assortedchests.container.chest_" + id, name + " Chest");
            this.add("block.assortedchests.chest_" + id, name + " Chest");
        }

        // Item tag names, which recipe viewers show in place of the raw tag id.
        for (int level = 0; level <= 5; level++) {
            this.add("tag.item.assortedchests.chests.level_" + level, "Level " + level + " Chests");
            this.add("tag.item.assortedchests.can_upgrade.level_" + level, "Level " + level + " Upgradable Storage");
        }

        this.addManual();
    }

    /** The section's title and description are the family's, written the same by every part. */
    private void addManual() {
        this.add("manual.assortedstorage.title", "Assorted Storage");
        this.add("manual.assortedstorage.description",
                "Bigger containers, crates, portable storage, and locks for all of it.");

        this.add("manual.assortedstorage.chapter.chests", "Chests");

        this.add("manual.assortedstorage.chapter.chests.materials.title", "Materials and Levels");
        this.add("manual.assortedstorage.chapter.chests.materials",
                "Chests come in a set of materials, and the material decides "
                        + "how much one holds. Stone is the smallest and netherite the largest." + BREAK
                        + "Materials are also grouped into storage levels, from 0 to 5. A level is what level upgrades step through." + BREAK
                        + "Everything Assorted Core adds has a place in that order too, which gives tin, "
                        + "silver, ruby and the rest somewhere useful to go.");

        this.add("manual.assortedstorage.chapter.chests.chests.title", "Chests");
        this.add("manual.assortedstorage.chapter.chests.chests",
                "A material chest works exactly like a vanilla one and holds a great deal more. They do not "
                        + "pair into doubles.");

        this.add("manual.assortedstorage.chapter.chests.locked.title", "Locked Chests");
        this.add("manual.assortedstorage.chapter.chests.locked",
                "Use a padlock on a chest or an ender chest and it becomes the locked version of itself, keeping "
                        + "whatever was inside. After that only a key with the matching combination opens it." + BREAK
                        + "A locked ender chest opens the ender storage tied to its combination.");
    }
}
