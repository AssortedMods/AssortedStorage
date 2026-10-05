package com.grim3212.assorted.locks.gametest;

import com.grim3212.assorted.lib.core.inventory.locking.ILockable;
import com.grim3212.assorted.lib.core.inventory.locking.LockConversions;
import com.grim3212.assorted.lib.core.inventory.locking.StorageUtil;
import com.grim3212.assorted.lib.core.storage.BaseStorageBlockEntity;
import com.grim3212.assorted.lib.core.storage.shulker.LockedShulkerBoxBlockEntity;
import com.grim3212.assorted.locks.Constants;
import com.grim3212.assorted.locks.common.block.LocksBlocks;
import com.grim3212.assorted.locks.common.block.blockentity.LockedEnderChestBlockEntity;
import com.grim3212.assorted.locks.common.item.LocksItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.lib.test.TestSupport.*;
import static com.grim3212.assorted.locks.gametest.LocksTestSupport.*;

/**
 * The locked chest, ender chest, barrel, hopper and shulker box: a padlock makes them, a key takes them back, and
 * worlds saved before this mod owned them still load them.
 */
final class LockedContainerTests {

    private LockedContainerTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("vanilla_containers_lock_into_their_locked_versions", LockedContainerTests::vanillaContainersLockIntoTheirLockedVersions);
        out.accept("locked_containers_unlock_back_into_vanilla", LockedContainerTests::lockedContainersUnlockBackIntoVanilla);
        out.accept("locked_ender_chests_share_by_code", LockedContainerTests::lockedEnderChestsShareByCode);
        out.accept("locked_containers_send_their_lock_to_clients", LockedContainerTests::lockedContainersSendTheirLockToClients);
        out.accept("locked_containers_survive_save_load", LockedContainerTests::lockedContainersSurviveSaveLoad);
        out.accept("assortedstorage_locked_containers_still_load", LockedContainerTests::assortedstorageLockedContainersStillLoad);
    }

    /** Each vanilla container and the locked one a padlock turns it into, the dyed shulker box standing for all 16. */
    private static Map<Block, Block> vanillaToLocked() {
        Map<Block, Block> map = new LinkedHashMap<>();
        map.put(Blocks.CHEST, LocksBlocks.LOCKED_CHEST.get());
        map.put(Blocks.ENDER_CHEST, LocksBlocks.LOCKED_ENDER_CHEST.get());
        map.put(Blocks.BARREL, LocksBlocks.LOCKED_BARREL.get());
        map.put(Blocks.HOPPER, LocksBlocks.LOCKED_HOPPER.get());
        map.put(Blocks.SHULKER_BOX, LocksBlocks.LOCKED_SHULKER_BOX.get());
        map.put(Blocks.DYED_SHULKER_BOX.pick(DyeColor.RED), LocksBlocks.LOCKED_SHULKER_BOX.get());
        return map;
    }

    /** The locked containers that hold items of their own, which the ender chest does not. */
    private static List<Block> lockedWithInventories() {
        return List.of(LocksBlocks.LOCKED_CHEST.get(), LocksBlocks.LOCKED_BARREL.get(), LocksBlocks.LOCKED_HOPPER.get(), LocksBlocks.LOCKED_SHULKER_BOX.get());
    }

    /** A padlock on each vanilla container makes this mod's locked one, with the code on it and the contents kept. */
    private static void vanillaContainersLockIntoTheirLockedVersions(GameTestHelper helper) {
        List<String> wrong = new ArrayList<>();

        int i = 0;
        for (Map.Entry<Block, Block> entry : vanillaToLocked().entrySet()) {
            BlockPos pos = spread(i++);
            helper.setBlock(pos, entry.getKey());
            String name = name(entry.getKey());
            boolean holdsItems = helper.getBlockEntity(pos, BlockEntity.class) instanceof Container;
            if (holdsItems) {
                ((Container) helper.getBlockEntity(pos, BlockEntity.class)).setItem(0, new ItemStack(Items.DIAMOND, 3));
            }

            if (!LockConversions.tryLock(helper.getLevel(), helper.absolutePos(pos), CODE)) {
                wrong.add("a padlock did nothing to a " + name);
                continue;
            }
            if (!helper.getBlockState(pos).is(entry.getValue())) {
                wrong.add("a padlocked " + name + " became " + name(helper.getBlockState(pos).getBlock()) + ", not " + name(entry.getValue()));
                continue;
            }

            BlockEntity locked = helper.getBlockEntity(pos, BlockEntity.class);
            if (!(locked instanceof ILockable lockable) || !CODE.equals(lockable.getLockCode())) {
                wrong.add("a padlocked " + name + " does not carry the padlock's code");
            }
            if (holdsItems && !(locked instanceof BaseStorageBlockEntity storage && storage.getItemStackStorageHandler().getStackInSlot(0).is(Items.DIAMOND))) {
                wrong.add("a padlocked " + name + " lost what was inside it");
            }
        }

        LockedShulkerBoxBlockEntity red = helper.getBlockEntity(spread(5), LockedShulkerBoxBlockEntity.class);
        helper.assertValueEqual(red.getColor(), DyeColor.RED, "the color of a padlocked red shulker box");
        helper.assertTrue(wrong.isEmpty(), wrong.size() + " problem(s): " + String.join("; ", wrong));
        helper.succeed();
    }

    /** Sneaking with an empty hand and the key in the inventory takes the padlock off and gives the vanilla container back. */
    private static void lockedContainersUnlockBackIntoVanilla(GameTestHelper helper) {
        ServerPlayer player = survivalPlayer(helper, ItemStack.EMPTY);
        player.getInventory().setItem(9, StorageUtil.setCodeOnStack(CODE, new ItemStack(LocksItems.LOCKSMITH_KEY.get())));
        player.setShiftKeyDown(true);
        List<String> wrong = new ArrayList<>();

        Map<Block, Block> lockedToVanilla = new LinkedHashMap<>();
        vanillaToLocked().forEach((vanilla, locked) -> lockedToVanilla.putIfAbsent(locked, vanilla));

        int i = 0;
        for (Map.Entry<Block, Block> entry : lockedToVanilla.entrySet()) {
            BlockPos pos = spread(i++);
            helper.setBlock(pos, entry.getKey());
            BaseStorageBlockEntity storage = helper.getBlockEntity(pos, BaseStorageBlockEntity.class);
            storage.setLockCode(CODE);
            boolean holdsItems = !(storage instanceof LockedEnderChestBlockEntity);
            if (holdsItems) {
                storage.getItemStackStorageHandler().setStackInSlot(0, new ItemStack(Items.DIAMOND, 3));
            }

            BlockPos abs = helper.absolutePos(pos);
            helper.getBlockState(pos).useItemOn(ItemStack.EMPTY, helper.getLevel(), player, InteractionHand.MAIN_HAND, new BlockHitResult(Vec3.atCenterOf(abs), Direction.UP, abs, false));

            String name = name(entry.getKey());
            if (!helper.getBlockState(pos).is(entry.getValue())) {
                wrong.add("an unlocked " + name + " is " + name(helper.getBlockState(pos).getBlock()) + ", not " + name(entry.getValue()));
                continue;
            }
            if (holdsItems && !(helper.getBlockEntity(pos, BlockEntity.class) instanceof Container vanilla && vanilla.getItem(0).is(Items.DIAMOND))) {
                wrong.add("an unlocked " + name + " lost what was inside it");
            }
        }

        helper.assertTrue(wrong.isEmpty(), wrong.size() + " problem(s): " + String.join("; ", wrong));
        helper.assertTrue(countInInventory(player, LocksItems.LOCKSMITH_LOCK.get()) + helper.getEntities(EntityTypes.ITEM).stream().filter(item -> item.getItem().is(LocksItems.LOCKSMITH_LOCK.get())).count() > 0, "no padlock came back from unlocking");
        helper.succeed();
    }

    /** One inventory per code, held in level saved data: two chests on a code share it, a chest on another code does not. */
    private static void lockedEnderChestsShareByCode(GameTestHelper helper) {
        LockedEnderChestBlockEntity first = enderChest(helper, new BlockPos(2, 1, 2), "shared-a");
        LockedEnderChestBlockEntity second = enderChest(helper, new BlockPos(6, 1, 2), "shared-a");
        LockedEnderChestBlockEntity other = enderChest(helper, new BlockPos(2, 1, 6), "shared-b");

        first.getItemStackStorageHandler().setStackInSlot(0, new ItemStack(Items.DIAMOND, 3));

        ItemStack shared = second.getItemStackStorageHandler().getStackInSlot(0);
        helper.assertTrue(shared.is(Items.DIAMOND), "a second chest on the same code did not see the contents");
        helper.assertValueEqual(shared.getCount(), 3, "the shared stack size");
        helper.assertTrue(other.getItemStackStorageHandler().getStackInSlot(0).isEmpty(), "a chest on a different code saw another code's contents");
        helper.succeed();
    }

    /** The lock goes to clients in the update tag, or other players keep seeing an unlocked container. */
    private static void lockedContainersSendTheirLockToClients(GameTestHelper helper) {
        List<Block> blocks = lockedWithInventories();
        for (int i = 0; i < blocks.size(); i++) {
            assertLockReachesClients(helper, spread(i), blocks.get(i));
        }
        helper.succeed();
    }

    /** Contents and lock survive a chunk save and load. */
    private static void lockedContainersSurviveSaveLoad(GameTestHelper helper) {
        HolderLookup.Provider registries = helper.getLevel().registryAccess();
        List<Block> blocks = lockedWithInventories();

        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            BlockPos pos = spread(i);
            helper.setBlock(pos, block);

            BaseStorageBlockEntity storage = helper.getBlockEntity(pos, BaseStorageBlockEntity.class);
            int last = storage.getItemStackStorageHandler().getSlots() - 1;
            storage.getItemStackStorageHandler().setStackInSlot(0, new ItemStack(Items.DIAMOND, 5));
            storage.getItemStackStorageHandler().setStackInSlot(last, new ItemStack(Items.EMERALD, 2));
            storage.setLockCode(CODE);

            CompoundTag saved = storage.saveWithFullMetadata(registries);
            BlockEntity reloaded = BlockEntity.loadStatic(helper.absolutePos(pos), storage.getBlockState(), saved, registries);

            helper.assertTrue(reloaded instanceof BaseStorageBlockEntity, name(block) + " did not load back as a storage block entity");
            BaseStorageBlockEntity loaded = (BaseStorageBlockEntity) reloaded;
            helper.assertValueEqual(loaded.getLockCode(), CODE, name(block) + " lost its lock code across a save/load");
            helper.assertTrue(loaded.getItemStackStorageHandler().getStackInSlot(0).is(Items.DIAMOND), name(block) + " lost its first slot across a save/load");
            helper.assertTrue(loaded.getItemStackStorageHandler().getStackInSlot(last).is(Items.EMERALD), name(block) + " lost its last slot across a save/load");
        }

        helper.succeed();
    }

    /**
     * A locked container saved by Assorted Storage 11 has its block entity under an {@code assortedstorage} id that a
     * part may own too. Whichever type that id reaches, it loads as this mod's.
     */
    private static void assortedstorageLockedContainersStillLoad(GameTestHelper helper) {
        HolderLookup.Provider registries = helper.getLevel().registryAccess();
        List<Block> blocks = new ArrayList<>(lockedWithInventories());
        blocks.add(LocksBlocks.LOCKED_ENDER_CHEST.get());

        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            BlockPos pos = spread(i);
            helper.setBlock(pos, block);
            BaseStorageBlockEntity storage = helper.getBlockEntity(pos, BaseStorageBlockEntity.class);
            storage.setLockCode(CODE);

            CompoundTag saved = storage.saveWithFullMetadata(registries);
            saved.putString("id", Constants.FAMILY_ID + ":" + name(block));
            BlockEntity reloaded = BlockEntity.loadStatic(helper.absolutePos(pos), storage.getBlockState(), saved, registries);

            helper.assertTrue(reloaded != null, "a " + Constants.FAMILY_ID + ":" + name(block) + " block entity did not load");
            helper.assertValueEqual(reloaded.getType(), storage.getType(), "the type " + Constants.FAMILY_ID + ":" + name(block) + " loads as");
            helper.assertValueEqual(((ILockable) reloaded).getLockCode(), CODE, "the lock of a " + Constants.FAMILY_ID + ":" + name(block));
        }

        helper.succeed();
    }

    private static LockedEnderChestBlockEntity enderChest(GameTestHelper helper, BlockPos pos, String code) {
        helper.setBlock(pos, LocksBlocks.LOCKED_ENDER_CHEST.get());
        LockedEnderChestBlockEntity chest = helper.getBlockEntity(pos, LockedEnderChestBlockEntity.class);
        chest.setLockCode(code);
        // The saved-data inventory is only bound when the platform handler is first asked for.
        chest.getStorageHandler();
        return chest;
    }

    /** Recreates the block entity from its update tag, as a client does, and checks the lock arrived. */
    private static void assertLockReachesClients(GameTestHelper helper, BlockPos rel, Block block) {
        helper.setBlock(rel, block);
        BlockEntity placed = helper.getLevel().getBlockEntity(helper.absolutePos(rel));
        String name = BuiltInRegistries.BLOCK.getKey(block).toString();
        helper.assertTrue(placed instanceof ILockable, name + " has no lockable block entity");
        ((ILockable) placed).setLockCode(CODE);
        helper.assertTrue(placed.getUpdatePacket() != null, name + " sends clients no update packet");

        HolderLookup.Provider registries = helper.getLevel().registryAccess();
        BlockEntity onClient = placed.getType().create(placed.getBlockPos(), placed.getBlockState());
        helper.assertTrue(onClient instanceof ILockable, name + " did not recreate as a lockable block entity");
        onClient.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, registries, placed.getUpdateTag(registries)));
        helper.assertValueEqual(((ILockable) onClient).getLockCode(), CODE, name + " lock code as a client receives it");
    }
}
