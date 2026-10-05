package com.grim3212.assorted.containers.client.data;

import com.grim3212.assorted.containers.Constants;
import com.grim3212.assorted.lib.data.LibLanguageProvider;
import net.minecraft.data.PackOutput;

/**
 * Generates the en_us.json of this mod. A block whose name is its id in title case needs no line here (see
 * {@link LibLanguageProvider}); the manual's keys are the Assorted Storage section's, which every part shares.
 */
public class ContainersLanguageProvider extends LibLanguageProvider {

    /** A blank line between paragraphs; the manual splits its text the way the font does. */
    private static final String BREAK = "\n\n";

    public ContainersLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addNames() {
        this.add("itemGroup.assortedstorage", "Assorted Storage");

        this.add("assortedcontainers.container.wood_cabinet", "Wood Cabinet");
        this.add("assortedcontainers.container.glass_cabinet", "Glass Cabinet");
        this.add("assortedcontainers.container.warehouse_crate", "Warehouse Crate");
        this.add("assortedcontainers.container.gold_safe", "Gold Safe");
        this.add("assortedcontainers.container.obsidian_safe", "Obsidian Safe");
        this.add("assortedcontainers.container.locker", "Locker");
        this.add("assortedcontainers.container.item_tower", "Item Tower");
        this.add("assortedcontainers.container.item_tower.row", " - Row %s of");

        this.add("block.assortedcontainers.gold_safe", "§6Gold Safe");
        this.add("block.assortedcontainers.obsidian_safe", "§5Obsidian Safe");

        this.addManual();
    }

    /** This part's chapter of the Assorted Storage section, and the section's own title, which every part writes the same. */
    private void addManual() {
        this.add("manual.assortedstorage.title", "Assorted Storage");
        this.add("manual.assortedstorage.description",
                "Bigger containers, crates, portable storage, and locks for all of it.");

        this.add("manual.assortedstorage.chapter.furniture", "Other Storage");

        this.add("manual.assortedstorage.chapter.furniture.locker.title", "Locker");
        this.add("manual.assortedstorage.chapter.furniture.locker",
                "A locker is two blocks tall and opens as one container.");

        this.add("manual.assortedstorage.chapter.furniture.cabinets.title", "Cabinets");
        this.add("manual.assortedstorage.chapter.furniture.cabinets",
                "Cabinets are a visually pleasing addition to any room.");

        this.add("manual.assortedstorage.chapter.furniture.safes.title", "Safes");
        this.add("manual.assortedstorage.chapter.furniture.safes",
                "The gold safe works just like Shulker boxes where when you break it, it will keeps its inventory." + BREAK + "The obsidian safe is resitant to explosions");

        this.add("manual.assortedstorage.chapter.furniture.item_tower.title", "Item Tower");
        this.add("manual.assortedstorage.chapter.furniture.item_tower",
                "Item towers stack on top of each other and share one inventory, two rows per block. Build it as "
                        + "tall as the room allows and it is still opened from any part of it.");

        this.add("manual.assortedstorage.chapter.furniture.warehouse.title", "Warehouse Crates");
        this.add("manual.assortedstorage.chapter.furniture.warehouse",
                "A warehouse crate is not a crate from the other chapter but more of a box you would see in a Warehouse.");
    }
}
