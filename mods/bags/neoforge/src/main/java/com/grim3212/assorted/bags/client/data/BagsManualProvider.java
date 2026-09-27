package com.grim3212.assorted.bags.client.data;

import com.grim3212.assorted.bags.Constants;
import com.grim3212.assorted.bags.Family;
import com.grim3212.assorted.bags.common.item.BagsItems;
import com.grim3212.assorted.lib.data.LibManualProvider;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * This part's chapter of the Assorted Storage section, which every part shares; the explicit chapter order keeps the
 * section's order whichever parts are installed. The material bags are read from the map they are registered from.
 */
public class BagsManualProvider extends LibManualProvider {

    public BagsManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID, Family.ID);
    }

    @Override
    protected void addChapters() {
        this.section(Family.MANUAL_ORDER, Family.ICONS.toArray(Identifier[]::new));

        List<Item> bags = new ArrayList<>();
        bags.add(BagsItems.BAG.get());
        BagsItems.BAGS.values().stream().map(IRegistryObject::get).forEach(bags::add);

        ChapterBuilder chapter = this.chapter("bags", 6);
        chapter.recipes("bags", bags.toArray(Item[]::new)).opens(bags.toArray(Item[]::new));
        chapter.recipes("ender_bag", BagsItems.ENDER_BAG.get()).opens(BagsItems.ENDER_BAG.get());
    }
}
