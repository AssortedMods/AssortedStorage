package com.grim3212.assorted.chests.common.handlers;

import com.grim3212.assorted.lib.core.inventory.locking.StorageAccessUtil;
import com.grim3212.assorted.lib.core.storage.BaseStorageBlock;
import com.grim3212.assorted.lib.core.storage.BaseStorageBlockEntity;
import com.grim3212.assorted.lib.core.storage.LevelUpgrades;
import com.grim3212.assorted.chests.common.block.LockedChestBlock;
import com.grim3212.assorted.chests.common.block.ChestsBlocks;
import com.grim3212.assorted.chests.common.block.blockentity.LockedChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/** How a material chest, or a vanilla chest, takes a level upgrade. */
public class ChestsLevelUpgrades {

    public static void init() {
        registerChests();
        registerVanillaChest();
    }

    private static void registerChests() {
        LevelUpgrades.register((level, pos, state, player, material) -> {
            if (!(state.getBlock() instanceof LockedChestBlock chestBlock) || !LevelUpgrades.canUpgrade(chestBlock.getStorageMaterial(), material)) {
                return null;
            }

            if (!(level.getBlockEntity(pos) instanceof LockedChestBlockEntity storageBE) || !canUpgradeStorage(level, pos, player, storageBE)) {
                return null;
            }

            BlockState newState = ChestsBlocks.CHESTS.get(material).get().defaultBlockState().setValue(BaseStorageBlock.FACING, state.getValue(BaseStorageBlock.FACING));
            return LevelUpgrades.from(storageBE, newState, new LockedChestBlockEntity(pos, newState));
        });
    }

    private static void registerVanillaChest() {
        LevelUpgrades.register((level, pos, state, player, material) -> {
            if (!(state.getBlock() instanceof ChestBlock) || !LevelUpgrades.canUpgrade(null, material)) {
                return null;
            }

            if (!(level.getBlockEntity(pos) instanceof ChestBlockEntity chestBE) || ChestBlockEntity.getOpenCount(level, pos) > 0 || !chestBE.canOpen(player)) {
                return null;
            }

            BlockState newState = ChestsBlocks.CHESTS.get(material).get().defaultBlockState().setValue(BaseStorageBlock.FACING, state.getValue(ChestBlock.FACING));
            return LevelUpgrades.from(chestBE, newState, new LockedChestBlockEntity(pos, newState));
        });
    }

    /** Nobody may have it open, and a locked one needs its key. */
    private static boolean canUpgradeStorage(Level level, BlockPos pos, Player player, BaseStorageBlockEntity storage) {
        if (storage.getNumberOfPlayersUsing(level, storage) > 0) {
            return false;
        }

        return !storage.isLocked() || StorageAccessUtil.canAccess(level, pos, player);
    }
}
