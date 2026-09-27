package com.grim3212.assorted.storage.common.item.upgrades;

import com.grim3212.assorted.lib.core.storage.ICrateUpgrade;
import com.grim3212.assorted.lib.core.storage.LevelUpgrades;
import com.grim3212.assorted.lib.core.storage.PreparedLevelUpgrade;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.storage.StorageCommonMod;
import com.grim3212.assorted.storage.common.item.StorageDataComponents;
import com.grim3212.assorted.lib.core.storage.StorageInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

/** Upgrades a container in place to its material, through whatever each mod registered with {@link LevelUpgrades}. */
public class LevelUpgradeItem extends Item implements ICrateUpgrade {

    private final StorageMaterial storageMaterial;

    public LevelUpgradeItem(Properties properties, StorageMaterial storageMaterial) {
        super(properties.stacksTo(16).component(StorageDataComponents.STORAGE_INFO.get(), new StorageInfo(StorageInfo.LockLine.NONE, storageMaterial.getStorageLevel())));
        this.storageMaterial = storageMaterial;
    }

    public StorageMaterial getStorageMaterial() {
        return storageMaterial;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!StorageCommonMod.COMMON_CONFIG.upgradesEnabled.get()) {
            return super.useOn(context);
        }

        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack itemstack = player.getItemInHand(context.getHand());

        PreparedLevelUpgrade upgrade = LevelUpgrades.prepare(world, pos, player, this.storageMaterial);
        if (upgrade == null) {
            return InteractionResult.PASS;
        }

        if (LevelUpgrades.apply(world, pos, upgrade)) {
            if (!player.isCreative())
                itemstack.shrink(1);

            world.playSound(player, pos, SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 0.5F, world.getRandom().nextFloat() * 0.1F + 0.9F);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public int getStorageModifier() {
        return (this.storageMaterial.getStorageLevel() + 1) * 5;
    }
}
