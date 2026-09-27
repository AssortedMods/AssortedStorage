package com.grim3212.assorted.locks.common.item;

import com.grim3212.assorted.lib.core.storage.StorageInfo;
import net.minecraft.world.item.Item;


public class CombinationItem extends Item {

    public CombinationItem(Properties properties) {
        super(properties.stacksTo(16).component(LocksDataComponents.STORAGE_INFO.get(), new StorageInfo(StorageInfo.LockLine.CODE, -1)));
    }
}
