package com.grim3212.assorted.shulkers.client.data;

import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.data.LibLanguageProvider;
import com.grim3212.assorted.shulkers.Constants;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.DyeColor;

/**
 * Generates the en_us.json of this mod. A block whose name is its id in title case needs no line
 * here (see {@link LibLanguageProvider}); the manual's keys are the Assorted Storage section's, which every part shares.
 */
public class ShulkersLanguageProvider extends LibLanguageProvider {

    /** A blank line between paragraphs; the manual splits its text the way the font does. */
    private static final String BREAK = "\n\n";

    public ShulkersLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addNames() {
        this.add("itemGroup.assortedstorage", "Assorted Storage");

        this.add("assortedshulkers.container.locked_shulker_box", "Locked Shulker Box");

        // The material comes first in each name, where the id puts it last.
        for (StorageMaterial material : StorageMaterial.values()) {
            String id = material.toString();
            String name = titleCase(id);

            this.add("assortedshulkers.container.shulker_" + id, name + " Shulker Box");
            this.add("block.assortedshulkers.shulker_box_" + id, name + " Shulker Box");
        }

        for (DyeColor color : DyeColor.values()) {
            String id = color.getName();
            String name = titleCase(id);

            this.add("block.assortedshulkers.locked_shulker_box_" + id, name + " Locked Shulker Box");

            for (StorageMaterial material : StorageMaterial.values()) {
                String materialId = material.toString();
                this.add("block.assortedshulkers.shulker_box_" + materialId + "_" + id, name + " " + titleCase(materialId) + " Shulker Box");
            }
        }

        // Item tag names, which recipe viewers show in place of the raw tag id.
        this.add("tag.item.assortedshulkers.shulkers.normal", "Vanilla Shulker Boxes");
        for (int level = 0; level <= 5; level++) {
            this.add("tag.item.assortedshulkers.shulkers.level_" + level, "Level " + level + " Shulker Boxes");
            this.add("tag.item.assortedshulkers.can_upgrade.level_" + level, "Level " + level + " Upgradable Shulker Boxes");
        }

        this.addManual();
    }

    /** This part's chapter of the Assorted Storage section, and the section's own title, which every part writes the same. */
    private void addManual() {
        this.add("manual.assortedstorage.title", "Assorted Storage");
        this.add("manual.assortedstorage.description",
                "Bigger containers, crates, portable storage, and locks for all of it.");

        this.add("manual.assortedstorage.chapter.shulker_boxes", "Shulker Boxes");

        this.add("manual.assortedstorage.chapter.shulker_boxes.materials.title", "Materials and Levels");
        this.add("manual.assortedstorage.chapter.shulker_boxes.materials",
                "Shulker boxes come in a whole set of materials, and the material decides how much one holds. "
                        + "Stone is the smallest and netherite the largest." + BREAK
                        + "Materials are also grouped into storage levels, from 0 to 5. A level is what level upgrades step through." + BREAK
                        + "Everything Assorted Core adds has a place in that order too, which gives tin, "
                        + "silver, ruby and the rest somewhere useful to go.");

        this.add("manual.assortedstorage.chapter.shulker_boxes.shulker_boxes.title", "Shulker Boxes");
        this.add("manual.assortedstorage.chapter.shulker_boxes.shulker_boxes",
                "Material shulker boxes keep their contents when broken, the same as the vanilla, and can end up being much larger.");

        this.add("manual.assortedstorage.chapter.shulker_boxes.locked.title", "Locked Shulker Box");
        this.add("manual.assortedstorage.chapter.shulker_boxes.locked",
                "A padlock turns a vanilla shulker box into a locked one and keeps whatever was inside." + BREAK
                        + "After that only a key with the matching combination opens it. Padlocks and keys come from Assorted Locks.");
    }
}
