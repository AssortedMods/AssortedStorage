package com.grim3212.assorted.hoppers.client.data;

import com.grim3212.assorted.hoppers.Constants;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.data.LibLanguageProvider;
import net.minecraft.data.PackOutput;

/**
 * Generates the en_us.json of this mod. A block whose name is its id in title case needs no line
 * here (see {@link LibLanguageProvider}); the manual's keys are the Assorted Storage section's, which every part shares.
 */
public class HoppersLanguageProvider extends LibLanguageProvider {

    /** A blank line between paragraphs; the manual splits its text the way the font does. */
    private static final String BREAK = "\n\n";

    public HoppersLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addNames() {
        this.add("itemGroup.assortedstorage", "Assorted Storage");

        this.add("assortedhoppers.container.locked_hopper", "Locked Hopper");

        // The material comes first in each name, where the id puts it last.
        for (StorageMaterial material : StorageMaterial.values()) {
            String id = material.toString();
            String name = titleCase(id);

            this.add("assortedhoppers.container.hopper_" + id, name + " Hopper");
            this.add("block.assortedhoppers.hopper_" + id, name + " Hopper");
        }

        // Item tag names, which recipe viewers show in place of the raw tag id.
        this.add("tag.item.c.hoppers", "Hoppers");
        for (int level = 0; level <= 5; level++) {
            this.add("tag.item.assortedhoppers.hoppers.level_" + level, "Level " + level + " Hoppers");
            this.add("tag.item.assortedhoppers.can_upgrade.level_" + level, "Level " + level + " Upgradable Hoppers");
        }

        this.addManual();
    }

    /** This part's chapter of the Assorted Storage section, and the section's own title, which every part writes the same. */
    private void addManual() {
        this.add("manual.assortedstorage.title", "Assorted Storage");
        this.add("manual.assortedstorage.description",
                "Bigger containers, crates, portable storage, and locks for all of it.");

        this.add("manual.assortedstorage.chapter.hoppers", "Hoppers");

        this.add("manual.assortedstorage.chapter.hoppers.materials.title", "Materials and Levels");
        this.add("manual.assortedstorage.chapter.hoppers.materials",
                "Hoppers come in a whole set of materials, and the material decides how much one holds. "
                        + "Stone is the smallest and netherite the largest." + BREAK
                        + "Materials are also grouped into storage levels, from 0 to 5. A level is what level upgrades step through." + BREAK
                        + "Everything Assorted Core adds has a place in that order too, which gives tin, "
                        + "silver, ruby and the rest somewhere useful to go.");

        this.add("manual.assortedstorage.chapter.hoppers.hoppers.title", "Hoppers");
        this.add("manual.assortedstorage.chapter.hoppers.hoppers",
                "A material hopper moves items the way a vanilla one does but has far more room, and can vary in speed depending on the material.");

        this.add("manual.assortedstorage.chapter.hoppers.locked.title", "Locked Hopper");
        this.add("manual.assortedstorage.chapter.hoppers.locked",
                "A padlock turns a vanilla hopper into a locked one and keeps whatever was inside." + BREAK
                        + "After that only a key with the matching combination opens it. Padlocks and keys come from Assorted Locks.");
    }
}
