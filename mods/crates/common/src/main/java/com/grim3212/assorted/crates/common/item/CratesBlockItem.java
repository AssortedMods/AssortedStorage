package com.grim3212.assorted.crates.common.item;

import com.grim3212.assorted.lib.core.storage.StorageInfo;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

/**
 * Shared block item for the crates. Its lock line comes from the {@link StorageInfo} component it sets; crates have no
 * storage level, so that line is left out.
 */
public class CratesBlockItem extends BlockItem {

    public CratesBlockItem(Block b, Properties props) {
        super(b, props.component(CratesDataComponents.STORAGE_INFO.get(), new StorageInfo(StorageInfo.LockLine.CODE, -1)));
    }
}
