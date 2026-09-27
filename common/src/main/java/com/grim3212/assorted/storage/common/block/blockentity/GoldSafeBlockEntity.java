package com.grim3212.assorted.storage.common.block.blockentity;

import com.grim3212.assorted.lib.core.storage.BaseStorageBlockEntity;
import com.grim3212.assorted.storage.Constants;
import com.grim3212.assorted.storage.common.inventory.StorageMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class GoldSafeBlockEntity extends BaseStorageBlockEntity {

    public GoldSafeBlockEntity(BlockPos pos, BlockState state) {
        super(StorageBlockEntityTypes.GOLD_SAFE.get(), pos, state, 36);
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory player, Player playerEntity) {
        return StorageMenus.createGoldSafeContainer(windowId, player, this.getItemStackStorageHandler());
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable(Constants.MOD_ID + ".container.gold_safe");
    }

    /**
     * The safe's contents come out of its loot table through the dynamic CONTENTS drop, so the
     * block entity must not drop them a second time on removal.
     */
    @Override
    protected boolean shouldDropContents() {
        return false;
    }
}
