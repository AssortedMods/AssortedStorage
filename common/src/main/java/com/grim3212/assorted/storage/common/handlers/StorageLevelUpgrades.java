package com.grim3212.assorted.storage.common.handlers;

import com.grim3212.assorted.lib.core.inventory.locking.StorageAccessUtil;
import com.grim3212.assorted.lib.core.storage.BaseStorageBlock;
import com.grim3212.assorted.lib.core.storage.BaseStorageBlockEntity;
import com.grim3212.assorted.lib.core.storage.LevelUpgrades;
import com.grim3212.assorted.storage.common.block.LockedBarrelBlock;
import com.grim3212.assorted.storage.common.block.LockedChestBlock;
import com.grim3212.assorted.storage.common.block.LockedHopperBlock;
import com.grim3212.assorted.storage.common.block.LockedShulkerBoxBlock;
import com.grim3212.assorted.storage.common.block.StorageBlocks;
import com.grim3212.assorted.storage.common.block.blockentity.LockedBarrelBlockEntity;
import com.grim3212.assorted.storage.common.block.blockentity.LockedChestBlockEntity;
import com.grim3212.assorted.storage.common.block.blockentity.LockedHopperBlockEntity;
import com.grim3212.assorted.storage.common.block.blockentity.LockedShulkerBoxBlockEntity;
import com.grim3212.assorted.storage.mixin.block.BarrelBlockEntityAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * How each container takes a level upgrade. Each group is one future mod's share; the locked
 * hopper extends the vanilla one, so the material containers register first.
 */
public class StorageLevelUpgrades {

    public static void init() {
        registerChests();
        registerBarrels();
        registerHoppers();
        registerShulkers();

        registerVanillaChest();
        registerVanillaBarrel();
        registerVanillaHopper();
        registerVanillaShulker();
    }

    private static void registerChests() {
        LevelUpgrades.register((level, pos, state, player, material) -> {
            if (!(state.getBlock() instanceof LockedChestBlock chestBlock) || !LevelUpgrades.canUpgrade(chestBlock.getStorageMaterial(), material)) {
                return null;
            }

            if (!(level.getBlockEntity(pos) instanceof LockedChestBlockEntity storageBE) || !canUpgradeStorage(level, pos, player, storageBE)) {
                return null;
            }

            BlockState newState = StorageBlocks.CHESTS.get(material).get().defaultBlockState().setValue(BaseStorageBlock.FACING, state.getValue(BaseStorageBlock.FACING));
            return LevelUpgrades.from(storageBE, newState, new LockedChestBlockEntity(pos, newState));
        });
    }

    private static void registerBarrels() {
        LevelUpgrades.register((level, pos, state, player, material) -> {
            if (!(state.getBlock() instanceof LockedBarrelBlock barrelBlock) || !LevelUpgrades.canUpgrade(barrelBlock.getStorageMaterial(), material)) {
                return null;
            }

            if (!(level.getBlockEntity(pos) instanceof LockedBarrelBlockEntity barrelBE) || !canUpgradeStorage(level, pos, player, barrelBE)) {
                return null;
            }

            BlockState newState = StorageBlocks.BARRELS.get(material).get().defaultBlockState().setValue(LockedBarrelBlock.FACING, state.getValue(LockedBarrelBlock.FACING));
            return LevelUpgrades.from(barrelBE, newState, new LockedBarrelBlockEntity(pos, newState));
        });
    }

    private static void registerHoppers() {
        LevelUpgrades.register((level, pos, state, player, material) -> {
            if (!(state.getBlock() instanceof LockedHopperBlock hopperBlock) || !LevelUpgrades.canUpgrade(hopperBlock.getStorageMaterial(), material)) {
                return null;
            }

            if (!(level.getBlockEntity(pos) instanceof LockedHopperBlockEntity hopperBE) || !canUpgradeStorage(level, pos, player, hopperBE)) {
                return null;
            }

            BlockState newState = StorageBlocks.HOPPERS.get(material).get().defaultBlockState().setValue(LockedHopperBlock.FACING, state.getValue(LockedHopperBlock.FACING)).setValue(LockedHopperBlock.ENABLED, state.getValue(LockedHopperBlock.ENABLED));
            return LevelUpgrades.from(hopperBE, newState, new LockedHopperBlockEntity(pos, newState));
        });
    }

    private static void registerShulkers() {
        LevelUpgrades.register((level, pos, state, player, material) -> {
            if (!(state.getBlock() instanceof LockedShulkerBoxBlock shulkerBlock) || !LevelUpgrades.canUpgrade(shulkerBlock.getStorageMaterial(), material)) {
                return null;
            }

            if (!(level.getBlockEntity(pos) instanceof LockedShulkerBoxBlockEntity storageBE) || !storageBE.isClosed()) {
                return null;
            }

            if (storageBE.isLocked() && !StorageAccessUtil.canAccess(level, pos, player)) {
                return null;
            }

            BlockState newState = StorageBlocks.SHULKERS.get(material).get().defaultBlockState().setValue(ShulkerBoxBlock.FACING, state.getValue(ShulkerBoxBlock.FACING));
            LockedShulkerBoxBlockEntity newShulkerEntity = new LockedShulkerBoxBlockEntity(pos, newState);
            newShulkerEntity.setColor(storageBE.getColor());
            return LevelUpgrades.from(storageBE, newState, newShulkerEntity);
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

            BlockState newState = StorageBlocks.CHESTS.get(material).get().defaultBlockState().setValue(BaseStorageBlock.FACING, state.getValue(ChestBlock.FACING));
            return LevelUpgrades.from(chestBE, newState, new LockedChestBlockEntity(pos, newState));
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

            BlockState newState = StorageBlocks.BARRELS.get(material).get().defaultBlockState().setValue(LockedBarrelBlock.FACING, state.getValue(BarrelBlock.FACING));
            return LevelUpgrades.from(barrelBE, newState, new LockedBarrelBlockEntity(pos, newState));
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

            BlockState newState = StorageBlocks.HOPPERS.get(material).get().defaultBlockState().setValue(LockedHopperBlock.FACING, state.getValue(HopperBlock.FACING)).setValue(LockedHopperBlock.ENABLED, state.getValue(HopperBlock.ENABLED));
            return LevelUpgrades.from(hopperBE, newState, new LockedHopperBlockEntity(pos, newState));
        });
    }

    private static void registerVanillaShulker() {
        LevelUpgrades.register((level, pos, state, player, material) -> {
            if (!(state.getBlock() instanceof ShulkerBoxBlock shulkerToUpgrade) || !LevelUpgrades.canUpgrade(null, material)) {
                return null;
            }

            if (!(level.getBlockEntity(pos) instanceof ShulkerBoxBlockEntity shulkerBE) || !shulkerBE.isClosed() || !shulkerBE.canOpen(player)) {
                return null;
            }

            BlockState newState = StorageBlocks.SHULKERS.get(material).get().defaultBlockState().setValue(ShulkerBoxBlock.FACING, state.getValue(ShulkerBoxBlock.FACING));
            LockedShulkerBoxBlockEntity newShulkerEntity = new LockedShulkerBoxBlockEntity(pos, newState);
            newShulkerEntity.setColor(shulkerToUpgrade.getColor());
            return LevelUpgrades.from(shulkerBE, newState, newShulkerEntity);
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
