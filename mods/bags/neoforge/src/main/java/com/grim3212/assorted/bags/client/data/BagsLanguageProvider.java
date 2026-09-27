package com.grim3212.assorted.bags.client.data;

import com.grim3212.assorted.bags.Constants;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.data.LibLanguageProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.DyeColor;

/**
 * Generates the en_us.json of this mod. An item whose name is its id in title case needs no line here (see
 * {@link LibLanguageProvider}); the manual's keys are the Assorted Storage section's, which every part shares.
 */
public class BagsLanguageProvider extends LibLanguageProvider {

    /** A blank line between paragraphs; the manual splits its text the way the font does. */
    private static final String BREAK = "\n\n";

    public BagsLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addNames() {
        this.add("itemGroup.assortedstorage", "Assorted Storage");

        // The material comes first in each name, where the id puts it last.
        for (StorageMaterial material : StorageMaterial.values()) {
            this.add("item.assortedbags.bag_" + material, titleCase(material.toString()) + " Bag");
        }

        for (DyeColor color : DyeColor.values()) {
            String id = color.getName();
            String name = titleCase(id);

            this.add("item.assortedbags.bag_" + id, name + " Bag");
            for (StorageMaterial material : StorageMaterial.values()) {
                this.add("item.assortedbags.bag_" + material + "_" + id, name + " " + titleCase(material.toString()) + " Bag");
            }
        }

        // Item tag names, which recipe viewers show in place of the raw tag id.
        this.add("tag.item.assortedbags.bags", "Bags");
        for (int level = 0; level <= 5; level++) {
            this.add("tag.item.assortedbags.bags.level_" + level, "Level " + level + " Bags");
        }

        this.addManual();
    }

    /** This part's chapter of the Assorted Storage section, and the section's own title, which every part writes the same. */
    private void addManual() {
        this.add("manual.assortedstorage.title", "Assorted Storage");
        this.add("manual.assortedstorage.description",
                "Bigger containers, crates, portable storage, and locks for all of it.");

        this.add("manual.assortedstorage.chapter.bags", "Bags");

        this.add("manual.assortedstorage.chapter.bags.bags.title", "Bags");
        this.add("manual.assortedstorage.chapter.bags.bags",
                "A bag is a container you carry. Right click to open it anywhere, and it holds as much as its "
                        + "material allows." + BREAK
                        + "Bags can be dyed, you can adjust both Primary and Secondary Colors to really distinguish them.");

        this.add("manual.assortedstorage.chapter.bags.ender_bag.title", "Ender Bag");
        this.add("manual.assortedstorage.chapter.bags.ender_bag",
                "An ender bag opens your ender chest from wherever you are standing. If you lock it it will open the ender storage tied to that combination.");
    }
}
