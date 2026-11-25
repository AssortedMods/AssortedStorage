package com.grim3212.assorted.bags.client;

import com.grim3212.assorted.bags.client.color.BagColor;
import com.grim3212.assorted.bags.client.properties.HasStorageTagProperty;
import com.grim3212.assorted.bags.client.screen.BagScreen;
import com.grim3212.assorted.bags.client.screen.EnderBagScreen;
import com.grim3212.assorted.bags.common.inventory.BagsContainerTypes;
import com.grim3212.assorted.bags.common.item.BagItem;
import com.grim3212.assorted.bags.common.item.BagsItems;
import com.grim3212.assorted.lib.platform.ClientServices;
import com.grim3212.assorted.lib.registry.IRegistryObject;

import java.util.ArrayList;
import java.util.List;

public class BagsClient {

    public static void init() {
        ClientServices.CLIENT.registerScreen(BagsContainerTypes.BAG::get, BagScreen::new);
        ClientServices.CLIENT.registerScreen(BagsContainerTypes.ENDER_BAG::get, EnderBagScreen::new);

        // Every bag but the ender bag is dyed and locked; their item models select on these properties
        // in their overrides, and the dye colours tint layers 0 and 1.
        List<IRegistryObject<BagItem>> bags = new ArrayList<>(BagsItems.BAGS.values());
        bags.add(BagsItems.BAG);
        ClientServices.CLIENT.registerItemColor(BagColor.INSTANCE, () -> bags.stream().map(IRegistryObject::get).toList());
        for (IRegistryObject<BagItem> bag : bags) {
            for (HasStorageTagProperty property : HasStorageTagProperty.values()) {
                ClientServices.CLIENT.registerItemProperty(bag, property.id(), property);
            }
        }
        // The ender bag only shows its padlock.
        ClientServices.CLIENT.registerItemProperty(BagsItems.ENDER_BAG, HasStorageTagProperty.LOCKED.id(), HasStorageTagProperty.LOCKED);
    }
}
