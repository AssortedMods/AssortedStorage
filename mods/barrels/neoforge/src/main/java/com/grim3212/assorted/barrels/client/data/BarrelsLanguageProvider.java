package com.grim3212.assorted.barrels.client.data;

import com.grim3212.assorted.lib.data.LibLanguageProvider;
import com.grim3212.assorted.barrels.Constants;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import net.minecraft.data.PackOutput;

/**
 * Generates the en_us.json of Assorted Barrels. A block whose name is its id in title case needs no
 * line here (see {@link LibLanguageProvider}); these are the names that read differently, and every key that is not a name.
 */
public class BarrelsLanguageProvider extends LibLanguageProvider {

    /** A blank line between paragraphs; the manual splits its text the way the font does. */
    private static final String BREAK = "\n\n";

    public BarrelsLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addNames() {
        this.add("itemGroup.assortedstorage", "Assorted Storage");

        // The material comes first in each name, where the id puts it last.
        for (StorageMaterial material : StorageMaterial.values()) {
            String id = material.toString();
            String name = titleCase(id);

            this.add("assortedbarrels.container.barrel_" + id, name + " Barrel");
            this.add("block.assortedbarrels.barrel_" + id, name + " Barrel");
        }

        // Item tag names, which recipe viewers show in place of the raw tag id.
        for (int level = 0; level <= 5; level++) {
            this.add("tag.item.assortedbarrels.barrels.level_" + level, "Level " + level + " Barrels");
            this.add("tag.item.assortedbarrels.can_upgrade.level_" + level, "Level " + level + " Upgradable Storage");
        }

        this.addManual();
    }

    /** The section's title and description are the family's, written the same by every part. */
    private void addManual() {
        this.add("manual.assortedstorage.title", "Assorted Storage");
        this.add("manual.assortedstorage.description",
                "Bigger containers, crates, portable storage, and locks for all of it.");

        this.add("manual.assortedstorage.chapter.barrels", "Barrels");

        this.add("manual.assortedstorage.chapter.barrels.materials.title", "Materials and Levels");
        this.add("manual.assortedstorage.chapter.barrels.materials",
                "Barrels come in a set of materials, and the material decides "
                        + "how much one holds. Stone is the smallest and netherite the largest." + BREAK
                        + "Materials are also grouped into storage levels, from 0 to 5. A level is what level upgrades step through." + BREAK
                        + "Everything Assorted Core adds has a place in that order too, which gives tin, "
                        + "silver, ruby and the rest somewhere useful to go.");

        this.add("manual.assortedstorage.chapter.barrels.barrels.title", "Barrels");
        this.add("manual.assortedstorage.chapter.barrels.barrels",
                "Barrels hold the same as their chest and open without needing space above them.");
    }
}
