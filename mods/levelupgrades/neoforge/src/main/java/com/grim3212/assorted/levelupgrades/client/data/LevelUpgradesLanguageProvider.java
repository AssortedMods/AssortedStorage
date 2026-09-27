package com.grim3212.assorted.levelupgrades.client.data;

import com.grim3212.assorted.levelupgrades.Constants;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.data.LibLanguageProvider;
import net.minecraft.data.PackOutput;

/**
 * Generates the en_us.json of this mod. A block, item or entity whose name is its id in title case
 * needs no line here (see {@link LibLanguageProvider}); these are the names that read differently,
 * and every key that is not a name.
 */
public class LevelUpgradesLanguageProvider extends LibLanguageProvider {

    /** A blank line between paragraphs; the manual splits its text the way the font does. */
    private static final String BREAK = "\n\n";

    public LevelUpgradesLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addNames() {
        // The family's tab and manual section, written the same by every Assorted Storage mod.
        this.add("itemGroup.assortedstorage", "Assorted Storage");

        // The material comes first in each name, where the id puts it last.
        for (StorageMaterial material : StorageMaterial.values()) {
            String id = material.toString();
            this.add("item.assortedlevelupgrades.level_upgrade_" + id, titleCase(id) + " Level Upgrade");
        }

        // Item tag names, which recipe viewers show in place of the raw tag id.
        this.add("tag.item.c.paper", "Paper");
        this.add("tag.item.c.storage.level_upgrades", "Storage Level Upgrades");
        for (int level = 0; level <= 5; level++) {
            this.add("tag.item.c.storage.level_" + level + "_upgrades", "Level " + level + " Storage Upgrades");
        }

        this.addManual();
    }

    /** The chapter in {@code assets/assortedstorage/manual} names these keys. */
    private void addManual() {
        this.add("manual.assortedstorage.title", "Assorted Storage");
        this.add("manual.assortedstorage.description",
                "Bigger containers, crates, portable storage, and locks for all of it.");

        this.add("manual.assortedstorage.chapter.level_upgrades", "Level Upgrades");

        this.add("manual.assortedstorage.chapter.level_upgrades.level_upgrades.title", "Level Upgrades");
        this.add("manual.assortedstorage.chapter.level_upgrades.level_upgrades",
                "A level upgrade raises a container one material at a time, in place, without unpacking it." + BREAK
                        + "Each upgrade names the material it upgrades to, and its tooltip lists what level it will become. An upgrade will not skip a level, so working "
                        + "up from stone means working through the order.");
    }
}
