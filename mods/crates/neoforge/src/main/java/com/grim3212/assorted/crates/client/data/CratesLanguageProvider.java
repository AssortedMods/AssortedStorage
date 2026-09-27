package com.grim3212.assorted.crates.client.data;

import com.grim3212.assorted.lib.data.LibLanguageProvider;
import com.grim3212.assorted.crates.Constants;
import com.grim3212.assorted.crates.Family;
import com.grim3212.assorted.crates.common.block.CratesBlocks;
import com.grim3212.assorted.crates.common.block.CratesBlocks.CrateGroup;
import net.minecraft.data.PackOutput;

/**
 * Generates the en_us.json of this mod. A block or item whose name is its id in title case needs no line here (see
 * {@link LibLanguageProvider}); the manual's keys are the Assorted Storage section's, which every part shares.
 */
public class CratesLanguageProvider extends LibLanguageProvider {

    /** A blank line between paragraphs; the manual splits its text the way the font does. */
    private static final String BREAK = "\n\n";

    public CratesLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addNames() {
        this.add("itemGroup." + Family.ID, "Assorted Storage");

        this.add(Constants.MOD_ID + ".info.amount", "Amount %s");
        this.add(Constants.MOD_ID + ".info.upgrade.mode", "Mode: %s");
        this.add(Constants.MOD_ID + ".info.upgrade_amount.mode.simple", "Simple");
        this.add(Constants.MOD_ID + ".info.upgrade_amount.mode.full", "Full");
        this.add(Constants.MOD_ID + ".info.upgrade_redstone.mode.all", "All Slots");
        this.add(Constants.MOD_ID + ".info.upgrade_redstone.mode.most", "Most Full Slot");
        this.add(Constants.MOD_ID + ".info.upgrade_redstone.mode.least", "Least Full Slot");
        this.add(Constants.MOD_ID + ".info.upgrade_redstone.mode.slot", "Slot %s");
        this.add(Constants.MOD_ID + ".info.storage_multiplier", "Storage Multiplier %s");
        this.add(Constants.MOD_ID + ".info.item_lock", "Toggle item lock for slot %s");
        this.add(Constants.MOD_ID + ".info.compact_item_lock", "Toggle item lock for Compacting Crate");

        this.add(Constants.MOD_ID + ".container.storage_crate.upgrades", "Upgrades");
        this.add(Constants.MOD_ID + ".container.storage_crate", "Storage Crate");
        this.add(Constants.MOD_ID + ".container.compacting_storage_crate", "Compacting Storage Crate");

        for (CrateGroup group : CratesBlocks.CRATES) {
            String id = group.getType().toString();
            String name = titleCase(id);

            this.add("block." + Constants.MOD_ID + "." + id + "_crate", name + " Storage Crate");
            this.add("block." + Constants.MOD_ID + "." + id + "_crate_double", name + " Double Storage Crate");
            this.add("block." + Constants.MOD_ID + "." + id + "_crate_triple", name + " Triple Storage Crate");
            this.add("block." + Constants.MOD_ID + "." + id + "_crate_quadruple", name + " Quadruple Storage Crate");
        }
        this.add("block." + Constants.MOD_ID + ".crate_compacting", "Compacting Storage Crate");
        this.add("block." + Constants.MOD_ID + ".crate_controller", "Storage Crate Controller");
        this.add("block." + Constants.MOD_ID + ".crate_bridge", "Storage Crate Bridge");

        // Item tag names, which recipe viewers show in place of the raw tag id.
        this.add("tag.item." + Constants.MOD_ID + ".crafting_override", "Crafting Overrides");
        this.add("tag.item." + Constants.MOD_ID + ".one_to_one_crafting_override", "One-to-One Crafting Overrides");
        this.add("tag.item." + Constants.MOD_ID + ".crates", "Storage Crates");
        for (String size : new String[]{"single", "double", "triple", "quadruple"}) {
            this.add("tag.item." + Constants.MOD_ID + ".crates." + size, titleCase(size) + " Storage Crates");
        }
        this.add("tag.item." + Constants.MOD_ID + ".upgrades", "Storage Upgrades");
        this.add("tag.item.c.deepslate", "Deepslate");
        this.add("tag.item.c.paper", "Paper");
        this.add("tag.item.c.pistons", "Pistons");

        this.addManual();
    }

    /** This part's chapter of the Assorted Storage section, and the section's own title, which every part writes the same. */
    private void addManual() {
        this.add("manual." + Family.ID + ".title", "Assorted Storage");
        this.add("manual." + Family.ID + ".description",
                "Bigger containers, crates, portable storage, and locks for all of it.");

        String chapter = "manual." + Family.ID + ".chapter.crates";
        this.add(chapter, "Crates");

        this.add(chapter + ".crates.title", "Crates");
        this.add(chapter + ".crates",
                "A crate holds one kind of item and a lot of it. By default 32 stacks." + BREAK
                        + "There is a crate for every wood, and they are all the same size.");

        this.add(chapter + ".sizes.title", "Compartments");
        this.add(chapter + ".sizes",
                "Double, triple and quadruple crates split the same block into more compartments, each holding "
                        + "its own item." + BREAK
                        + "A double gives two compartments of sixteen stacks. A triple gives sixteen, eight and "
                        + "eight. A quadruple gives four of eight. More kinds of item, less of each.");

        this.add(chapter + ".controller.title", "Crate Controller");
        this.add(chapter + ".controller",
                "A controller gathers every crate it is connected to into one inventory, so a wall of crates "
                        + "can befilled from a single block." + BREAK
                        + "Crates connect through each other, so the controller only needs to touch the "
                        + "network once.");

        this.add(chapter + ".bridge.title", "Crate Bridge");
        this.add(chapter + ".bridge",
                "A bridge carries a crate network past a gap without storing anything itself. Use it to reach "
                        + "around a doorway, or to join two walls of crates to one controller.");

        this.add(chapter + ".compacting.title", "Compacting Crate");
        this.add(chapter + ".compacting",
                "A compacting crate can display the different levels of an item group. For exmaple if you place nine iron "
                        + "ingots, you will see the option of them as a iron block as well as iron nuggets.");

        this.add(chapter + ".upgrades.title", "Crate Upgrades");
        this.add(chapter + ".upgrades",
                "Upgrades slot into a crate and change how it behaves. The blank upgrade is the base the "
                        + "others are made from." + BREAK
                        + "Glow lights the crate to make it easier to see at night. Void throws away anything that will not fit. Amount shows how full it is, simply or exactly. "
                        + "Redstone gives out a signal, from all slots or from whichever one you pick.");

        this.add(chapter + ".rotator_majig.title", "Rotator Majig");
        this.add(chapter + ".rotator_majig",
                "The rotator majig turns whatever you right click into its next facing. Handy for pointing a "
                        + "crate, a hopper or anything else the right way round without breaking it first.");
    }
}
