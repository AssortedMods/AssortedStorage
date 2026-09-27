package com.grim3212.assorted.containers.common.block.blockentity;

import com.grim3212.assorted.lib.core.storage.BaseStorageBlockEntity;
import com.grim3212.assorted.containers.Constants;
import com.grim3212.assorted.containers.common.inventory.ContainersMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class ObsidianSafeBlockEntity extends BaseStorageBlockEntity {

    public ObsidianSafeBlockEntity(BlockPos pos, BlockState state) {
        super(ContainersBlockEntityTypes.OBSIDIAN_SAFE.get(), pos, state);
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory player, Player playerEntity) {
        return ContainersMenus.createObsidianSafeContainer(windowId, player, this.getItemStackStorageHandler());
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable(Constants.MOD_ID + ".container.obsidian_safe");
    }
}
