package com.grim3212.assorted.locks;

import com.grim3212.assorted.lib.core.inventory.IInventoryBlockEntity;
import com.grim3212.assorted.lib.inventory.FabricPlatformInventoryStorageHandlerUnsided;
import com.grim3212.assorted.locks.common.block.blockentity.LocksBlockEntityTypes;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import com.grim3212.assorted.locks.common.block.LocksBlocks;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class AssortedLocksFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        LocksCommonMod.init();

        registerLockedCopperDoorOxidation();

        ItemStorage.SIDED.registerForBlockEntities((be, direction) -> be instanceof IInventoryBlockEntity inv ? ((FabricPlatformInventoryStorageHandlerUnsided) inv.getStorageHandler()).getFabricInventory() : null,
                LocksBlockEntityTypes.LOCKED_CHEST.get(),
                LocksBlockEntityTypes.LOCKED_ENDER_CHEST.get(),
                LocksBlockEntityTypes.LOCKED_BARREL.get(),
                LocksBlockEntityTypes.LOCKED_HOPPER.get(),
                LocksBlockEntityTypes.LOCKED_SHULKER_BOX.get());
    }

    /**
     * Tells Fabric how the locked copper doors scrape back with an axe and wax with a honeycomb;
     * oxidising over time is {@code LockedCopperDoorBlock}'s own random tick and does not read this.
     * NeoForge's half is the {@code neoforge:oxidizables} and {@code neoforge:waxables} data maps in
     * {@code LocksDataMapProvider}, so a change here needs the same change there.
     */
    private static void registerLockedCopperDoorOxidation() {
        Blocks.COPPER_DOOR.weathering().progressMapping((from, to) -> OxidizableBlocksRegistry.registerNextStage(locked(from), locked(to)));
        Blocks.COPPER_DOOR.zipUnwaxedWaxed((unwaxed, waxed) -> OxidizableBlocksRegistry.registerWaxable(locked(unwaxed), locked(waxed)));
    }

    private static Block locked(Block vanillaDoor) {
        return LocksBlocks.VANILLA_DOORS.get(vanillaDoor).get();
    }
}
