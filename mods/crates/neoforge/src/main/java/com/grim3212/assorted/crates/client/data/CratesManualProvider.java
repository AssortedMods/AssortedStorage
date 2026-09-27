package com.grim3212.assorted.crates.client.data;

import com.grim3212.assorted.lib.data.LibManualProvider;
import com.grim3212.assorted.crates.Constants;
import com.grim3212.assorted.crates.Family;
import com.grim3212.assorted.crates.common.block.CratesBlocks;
import com.grim3212.assorted.crates.common.item.CratesItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

/**
 * This part's chapter of the Assorted Storage section, which every part shares; the explicit chapter order keeps the
 * section's order whichever parts are installed. The woods are read from the same list the crates are registered from.
 */
public class CratesManualProvider extends LibManualProvider {

    public CratesManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID, Family.ID);
    }

    @Override
    protected void addChapters() {
        this.section(Family.MANUAL_ORDER, Family.ICONS.toArray(Identifier[]::new));

        Block[] singles = CratesBlocks.CRATES.stream().map(group -> group.SINGLE.get()).toArray(Block[]::new);
        List<Block> bigger = new ArrayList<>();
        for (CratesBlocks.CrateGroup group : CratesBlocks.CRATES) {
            bigger.add(group.DOUBLE.get());
            bigger.add(group.TRIPLE.get());
            bigger.add(group.QUADRUPLE.get());
        }
        CratesBlocks.CrateGroup oak = CratesBlocks.CRATES.getFirst();

        ChapterBuilder crates = this.chapter("crates", 5);
        crates.recipes("crates", singles).opens(singles);
        crates.recipes("sizes", oak.SINGLE.get(), oak.DOUBLE.get(), oak.TRIPLE.get(), oak.QUADRUPLE.get()).every(50)
                .opens(bigger.toArray(Block[]::new));
        crates.recipes("controller", CratesBlocks.CRATE_CONTROLLER.get()).opens(CratesBlocks.CRATE_CONTROLLER.get());
        crates.recipesById("bridge", recipeId("crate_bridge_copper"), recipeId("crate_bridge_bronze"), recipeId("crate_bridge_gold")).every(60)
                .opens(CratesBlocks.CRATE_BRIDGE.get());
        crates.recipesById("compacting", recipeId("crate_compacting_iron"), recipeId("crate_compacting_aluminum"), recipeId("crate_compacting_steel"))
                .every(60).opens(CratesBlocks.CRATE_COMPACTING.get());
        crates.recipes("upgrades", CratesItems.BLANK_UPGRADE.get(), CratesItems.GLOW_UPGRADE.get(),
                        CratesItems.VOID_UPGRADE.get(), CratesItems.AMOUNT_UPGRADE.get(),
                        CratesItems.REDSTONE_UPGRADE.get()).every(50)
                .opens(CratesItems.BLANK_UPGRADE.get(), CratesItems.GLOW_UPGRADE.get(),
                        CratesItems.VOID_UPGRADE.get(), CratesItems.AMOUNT_UPGRADE.get(),
                        CratesItems.REDSTONE_UPGRADE.get());
        // Was in the furniture chapter, which Assorted Containers writes now.
        crates.recipesById("rotator_majig", recipeId("rotator_majig_iron"), recipeId("rotator_majig_aluminum"), recipeId("rotator_majig_steel"))
                .every(60).opens(CratesItems.ROTATOR_MAJIG.get());
    }
}
