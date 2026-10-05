package com.grim3212.assorted.locks.common.item;

import com.grim3212.assorted.lib.core.inventory.locking.LockConversions;
import com.grim3212.assorted.lib.core.inventory.locking.StorageUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

/**
 * Locks a vanilla block by turning it into the locked version some mod registered with {@link
 * LockConversions}. Blocks that are lockable already take the padlock themselves.
 */
public class PadlockItem extends CombinationItem {

    public PadlockItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack itemstack = player.getItemInHand(context.getHand());

        String code = StorageUtil.getCode(itemstack);
        if (!code.isEmpty() && LockConversions.tryLock(world, pos, code)) {
            if (!player.isCreative())
                itemstack.shrink(1);

            world.playSound(player, pos, SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 0.5F, world.getRandom().nextFloat() * 0.1F + 0.9F);
            return InteractionResult.SUCCESS;
        }

        return super.useOn(context);
    }
}
