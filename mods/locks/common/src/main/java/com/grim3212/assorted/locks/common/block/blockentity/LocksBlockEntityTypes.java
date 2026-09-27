package com.grim3212.assorted.locks.common.block.blockentity;

import com.grim3212.assorted.lib.core.inventory.locking.BaseLockedBlockEntity;
import com.grim3212.assorted.lib.core.storage.barrel.LockedBarrelBlockEntity;
import com.grim3212.assorted.lib.core.storage.chest.LockedChestBlockEntity;
import com.grim3212.assorted.lib.core.storage.hopper.LockedHopperBlockEntity;
import com.grim3212.assorted.lib.core.storage.shulker.LockedShulkerBoxBlockEntity;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.locks.Constants;
import com.grim3212.assorted.locks.Family;
import com.grim3212.assorted.locks.common.block.LocksBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Map;

public class LocksBlockEntityTypes {
    public static final RegistryProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistryProvider.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID).aliasFrom(Family.ID);

    /**
     * Types whose old {@code assortedstorage} ids also covered a part's material containers. That part keeps the
     * alias when it is installed, so these only take it when it is not; see {@link #aliasUnlessAPartHasIt()}.
     */
    public static final RegistryProvider<BlockEntityType<?>> SHARED_BLOCK_ENTITIES = RegistryProvider.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);

    public static final IRegistryObject<BlockEntityType<BaseLockedBlockEntity>> BASE_LOCKED = BLOCK_ENTITIES.register("base_locked", () -> Services.PLATFORM.createBlockEntityType((pos, state) -> new BaseLockedBlockEntity(LocksBlockEntityTypes.BASE_LOCKED.get(), pos, state), LocksBlocks.lockedDoors()));
    public static final IRegistryObject<BlockEntityType<LockedEnderChestBlockEntity>> LOCKED_ENDER_CHEST = BLOCK_ENTITIES.register("locked_ender_chest", () -> Services.PLATFORM.createBlockEntityType(LockedEnderChestBlockEntity::new, LocksBlocks.LOCKED_ENDER_CHEST.get()));

    public static final IRegistryObject<BlockEntityType<LockedChestBlockEntity>> LOCKED_CHEST = SHARED_BLOCK_ENTITIES.register("locked_chest", () -> Services.PLATFORM.createBlockEntityType(LockedChestBlockEntity::new, LocksBlocks.LOCKED_CHEST.get()));
    public static final IRegistryObject<BlockEntityType<LockedBarrelBlockEntity>> LOCKED_BARREL = SHARED_BLOCK_ENTITIES.register("locked_barrel", () -> Services.PLATFORM.createBlockEntityType(LockedBarrelBlockEntity::new, LocksBlocks.LOCKED_BARREL.get()));
    public static final IRegistryObject<BlockEntityType<LockedHopperBlockEntity>> LOCKED_HOPPER = SHARED_BLOCK_ENTITIES.register("locked_hopper", () -> Services.PLATFORM.createBlockEntityType(LockedHopperBlockEntity::new, LocksBlocks.LOCKED_HOPPER.get()));
    public static final IRegistryObject<BlockEntityType<LockedShulkerBoxBlockEntity>> LOCKED_SHULKER_BOX = SHARED_BLOCK_ENTITIES.register("locked_shulker_box", () -> Services.PLATFORM.createBlockEntityType(LockedShulkerBoxBlockEntity::new, LocksBlocks.LOCKED_SHULKER_BOX.get()));

    /** Each shared type and the part that aliases its old id when installed. */
    public static final Map<IRegistryObject<? extends BlockEntityType<?>>, String> SHARED_WITH = Map.of(
            LOCKED_CHEST, "assortedchests",
            LOCKED_BARREL, "assortedbarrels",
            LOCKED_HOPPER, "assortedhoppers",
            LOCKED_SHULKER_BOX, "assortedshulkers");

    /**
     * A registry id can only be aliased once. With the part installed its alias still loads these, as the block entity
     * takes its type from the block it is on.
     */
    private static void aliasUnlessAPartHasIt() {
        SHARED_WITH.forEach((type, part) -> {
            if (!Services.PLATFORM.isModLoaded(part)) {
                Services.REGISTRY_FACTORY.alias(Registries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(Family.ID, type.getId().getPath()), type.getId());
            }
        });
    }

    public static void init() {
        aliasUnlessAPartHasIt();
    }
}
