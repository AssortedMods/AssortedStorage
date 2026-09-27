package com.grim3212.assorted.hoppers.common.handlers;

import com.grim3212.assorted.hoppers.common.block.HoppersBlocks;
import com.grim3212.assorted.hoppers.common.block.LockedHopperBlock;
import com.grim3212.assorted.hoppers.common.block.blockentity.LockedHopperBlockEntity;
import com.grim3212.assorted.lib.core.inventory.locking.StorageAccessUtil;
import com.grim3212.assorted.lib.core.storage.BaseStorageBlockEntity;
import com.grim3212.assorted.lib.core.storage.LevelUpgrades;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * How a hopper takes a level upgrade. The locked hopper extends the vanilla one, so the material
 * hoppers register first.
 */
public class HoppersLevelUpgrades {

    public static void init() {
        registerHoppers();
        registerVanillaHopper();
    }

    private static void registerHoppers() {
        LevelUpgrades.register((level, pos, state, player, material) -> {
            if (!(state.getBlock() instanceof LockedHopperBlock hopperBlock) || !LevelUpgrades.canUpgrade(hopperBlock.getStorageMaterial(), material)) {
                return null;
            }

            if (!(level.getBlockEntity(pos) instanceof LockedHopperBlockEntity hopperBE) || !canUpgradeStorage(level, pos, player, hopperBE)) {
                return null;
            }

            BlockState newState = HoppersBlocks.HOPPERS.get(material).get().defaultBlockState().setValue(LockedHopperBlock.FACING, state.getValue(LockedHopperBlock.FACING)).setValue(LockedHopperBlock.ENABLED, state.getValue(LockedHopperBlock.ENABLED));
            return LevelUpgrades.from(hopperBE, newState, new LockedHopperBlockEntity(pos, newState));
        });
    }

    private static void registerVanillaHopper() {
        LevelUpgrades.register((level, pos, state, player, material) -> {
            if (!(state.getBlock() instanceof HopperBlock) || !LevelUpgrades.canUpgrade(null, material)) {
                return null;
            }

            if (!(level.getBlockEntity(pos) instanceof HopperBlockEntity hopperBE) || !hopperBE.canOpen(player)) {
                return null;
            }

            BlockState newState = HoppersBlocks.HOPPERS.get(material).get().defaultBlockState().setValue(LockedHopperBlock.FACING, state.getValue(HopperBlock.FACING)).setValue(LockedHopperBlock.ENABLED, state.getValue(HopperBlock.ENABLED));
            return LevelUpgrades.from(hopperBE, newState, new LockedHopperBlockEntity(pos, newState));
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
