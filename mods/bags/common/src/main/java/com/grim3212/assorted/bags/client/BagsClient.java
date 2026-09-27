package com.grim3212.assorted.bags.client;

import com.grim3212.assorted.bags.client.color.BagTintSource;
import com.grim3212.assorted.bags.client.properties.HasStorageTagProperty;
import com.grim3212.assorted.bags.client.screen.BagScreen;
import com.grim3212.assorted.bags.client.screen.EnderBagScreen;
import com.grim3212.assorted.bags.common.inventory.BagsContainerTypes;
import com.grim3212.assorted.lib.platform.ClientServices;

public class BagsClient {

    public static void init() {
        ClientServices.CLIENT.registerScreen(BagsContainerTypes.BAG::get, BagScreen::new);
        ClientServices.CLIENT.registerScreen(BagsContainerTypes.ENDER_BAG::get, EnderBagScreen::new);

        // An item's tints live in its model json now; all that is registered from code is the source
        // type. Bag models need a "tints" entry naming this id once per dyed layer, with "tag" set to
        // BagItem.TAG_PRIMARY_COLOR / TAG_SECONDARY_COLOR.
        ClientServices.CLIENT.registerItemTintSource(BagTintSource.ID, BagTintSource.MAP_CODEC);

        for (HasStorageTagProperty property : HasStorageTagProperty.values()) {
            ClientServices.CLIENT.registerConditionalItemModelProperty(property.id(), property.type());
        }
    }
}
