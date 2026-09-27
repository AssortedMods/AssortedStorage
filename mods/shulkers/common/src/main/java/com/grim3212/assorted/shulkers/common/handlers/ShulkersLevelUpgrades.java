package com.grim3212.assorted.shulkers.common.handlers;

import com.grim3212.assorted.lib.core.inventory.locking.StorageAccessUtil;
import com.grim3212.assorted.lib.core.storage.LevelUpgrades;
import com.grim3212.assorted.shulkers.common.block.LockedShulkerBoxBlock;
import com.grim3212.assorted.shulkers.common.block.ShulkersBlocks;
import com.grim3212.assorted.shulkers.common.block.blockentity.LockedShulkerBoxBlockEntity;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/** How a shulker box takes a level upgrade, vanilla or already made of a material. */
public class ShulkersLevelUpgrades {

    public static void init() {
        registerShulkers();
        registerVanillaShulker();
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

            BlockState newState = ShulkersBlocks.SHULKERS.get(material).get().defaultBlockState().setValue(ShulkerBoxBlock.FACING, state.getValue(ShulkerBoxBlock.FACING));
            LockedShulkerBoxBlockEntity newShulkerEntity = new LockedShulkerBoxBlockEntity(pos, newState);
            newShulkerEntity.setColor(storageBE.getColor());
            return LevelUpgrades.from(storageBE, newState, newShulkerEntity);
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

            BlockState newState = ShulkersBlocks.SHULKERS.get(material).get().defaultBlockState().setValue(ShulkerBoxBlock.FACING, state.getValue(ShulkerBoxBlock.FACING));
            LockedShulkerBoxBlockEntity newShulkerEntity = new LockedShulkerBoxBlockEntity(pos, newState);
            newShulkerEntity.setColor(shulkerToUpgrade.getColor());
            return LevelUpgrades.from(shulkerBE, newState, newShulkerEntity);
        });
    }
}
