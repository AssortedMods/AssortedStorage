package com.grim3212.assorted.locks.compat.curios;

import com.grim3212.assorted.lib.core.inventory.locking.StorageAccessUtil;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;

/**
 * Whether a key worn in a Curios slot opens a lock. Only reached when Curios is loaded, which is
 * why the api jar is compileOnly. The search goes through the API's own {@code findCurios(Predicate)}.
 */
public class CuriosHelper {

    public static boolean hasCodeMatch(@Nullable LivingEntity entity, String lockCode) {
        if (entity != null) {
            Optional<ICuriosItemHandler> handler = CuriosApi.getCuriosInventory(entity);
            if (handler.isPresent() && !handler.get().findCurios(stack -> StorageAccessUtil.canStackAccess(stack, lockCode)).isEmpty()) {
                return true;
            }
        }

        return lockCode == null || lockCode.isEmpty();
    }
}
