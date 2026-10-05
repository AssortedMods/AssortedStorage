package com.grim3212.assorted.barrels.common.handlers;

import com.grim3212.assorted.lib.core.inventory.locking.StorageAccessUtil;
import com.grim3212.assorted.lib.core.storage.BaseStorageBlockEntity;
import com.grim3212.assorted.lib.core.storage.LevelUpgrades;
import com.grim3212.assorted.lib.core.storage.barrel.LockedBarrelBlock;
import com.grim3212.assorted.barrels.common.block.BarrelsBlocks;
import com.grim3212.assorted.lib.core.storage.barrel.LockedBarrelBlockEntity;
import com.grim3212.assorted.barrels.mixin.block.BarrelBlockEntityAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/** How a material barrel, a vanilla barrel or Assorted Locks' locked barrel takes a level upgrade. */
public class BarrelsLevelUpgrades {

    public static void init() {
        registerBarrels();
        registerVanillaBarrel();
    }

    private static void registerBarrels() {
        LevelUpgrades.register((level, pos, state, player, material) -> {
            if (!(state.getBlock() instanceof LockedBarrelBlock barrelBlock) || !LevelUpgrades.canUpgrade(barrelBlock.getStorageMaterial(), material)) {
                return null;
            }

            if (!(level.getBlockEntity(pos) instanceof LockedBarrelBlockEntity barrelBE) || !canUpgradeStorage(level, pos, player, barrelBE)) {
                return null;
            }

            BlockState newState = BarrelsBlocks.BARRELS.get(material).get().defaultBlockState().setValue(LockedBarrelBlock.FACING, state.getValue(LockedBarrelBlock.FACING));
            return LevelUpgrades.from(barrelBE, newState, new LockedBarrelBlockEntity(pos, newState));
        });
    }

    private static void registerVanillaBarrel() {
        LevelUpgrades.register((level, pos, state, player, material) -> {
            if (!(state.getBlock() instanceof BarrelBlock) || !LevelUpgrades.canUpgrade(null, material)) {
                return null;
            }

            if (!(level.getBlockEntity(pos) instanceof BarrelBlockEntity barrelBE) || ((BarrelBlockEntityAccessor) barrelBE).getOpenersCounter().getOpenerCount() > 0 || !barrelBE.canOpen(player)) {
                return null;
            }

            BlockState newState = BarrelsBlocks.BARRELS.get(material).get().defaultBlockState().setValue(LockedBarrelBlock.FACING, state.getValue(BarrelBlock.FACING));
            return LevelUpgrades.from(barrelBE, newState, new LockedBarrelBlockEntity(pos, newState));
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
