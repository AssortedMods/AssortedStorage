package com.grim3212.assorted.crates.gametest;

import com.grim3212.assorted.crates.api.LargeItemStack;
import com.grim3212.assorted.crates.common.block.CratesBlocks;
import com.grim3212.assorted.crates.common.block.blockentity.CrateBlockEntity;
import com.grim3212.assorted.crates.common.block.blockentity.CrateControllerBlockEntity;
import com.grim3212.assorted.crates.common.inventory.crates.CrateSidedInv;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.crates.gametest.CratesTestSupport.blockEntity;
import static com.grim3212.assorted.crates.gametest.CratesTestSupport.oakCrate;

/**
 * A crate as other mods reach it, through {@code Capabilities.ItemHandler.BLOCK}. The shared tests
 * cover the handler; these cover the NeoForge wrapper around it. NeoForge 21.1's item handler has
 * no transactions: a transfer that may not happen is a simulated one, which is what a mod asks
 * before it commits.
 */
final class CrateTransferTests {

    private CrateTransferTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("neoforge_crate_survives_a_rolled_back_transaction", CrateTransferTests::crateSurvivesASimulatedExtract);
        out.accept("neoforge_controller_storage_follows_the_network", CrateTransferTests::controllerStorageFollowsTheNetwork);
    }

    /**
     * A controller's handler keeps up with its network. It only finds its crates on a server tick, so
     * a lookup can land before it has found any, and the count moves as crates come and go.
     */
    private static void controllerStorageFollowsTheNetwork(GameTestHelper helper) {
        BlockPos controllerPos = new BlockPos(3, 1, 4);
        BlockPos cratePos = controllerPos.east();

        helper.setBlock(controllerPos, CratesBlocks.CRATE_CONTROLLER.get());
        CrateControllerBlockEntity controller = blockEntity(helper, controllerPos, CrateControllerBlockEntity.class);

        // Looked up before the controller has ticked.
        IItemHandler handler = helper.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, helper.absolutePos(controllerPos), null);
        helper.assertTrue(handler != null, "the controller exposed no item handler");
        helper.assertValueEqual(handler.getSlots(), 0, "the controller's slot count before it found any crates");

        // Now give it a crate and let it find one.
        helper.setBlock(cratePos, oakCrate());
        CrateSidedInv crate = blockEntity(helper, cratePos, CrateBlockEntity.class).getItemStackStorageHandler();
        crate.setItem(0, new LargeItemStack(new ItemStack(Items.COBBLESTONE), 300));
        controller.tick();

        helper.assertValueEqual(handler.getSlots(), 1, "the controller's slot count once it found a crate");
        helper.assertValueEqual(handler.getStackInSlot(0).getCount(), 300, "what the controller reports the crate holding");

        // And the crate going away takes its slot with it.
        helper.setBlock(cratePos, Blocks.AIR);
        controller.tick();
        helper.assertValueEqual(handler.getSlots(), 0, "the controller's slot count after the crate was removed");
        helper.succeed();
    }

    private static void crateSurvivesASimulatedExtract(GameTestHelper helper) {
        final BlockPos pos = new BlockPos(4, 1, 4);
        helper.setBlock(pos, oakCrate());

        CrateSidedInv crate = blockEntity(helper, pos, CrateBlockEntity.class).getItemStackStorageHandler();
        crate.setItem(0, new LargeItemStack(new ItemStack(Items.COBBLESTONE), 2000));

        IItemHandler handler = helper.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, helper.absolutePos(pos), null);
        helper.assertTrue(handler != null, "the crate exposed no item handler");

        // What a readout mod asks for: the slot's real amount.
        helper.assertValueEqual(handler.getStackInSlot(0).getCount(), 2000, "the amount the crate reports to an item handler");

        // A simulated transfer must leave the crate as it was.
        ItemStack simulated = handler.extractItem(0, 64, true);
        helper.assertTrue(simulated.is(Items.COBBLESTONE), "a simulated extract offered " + simulated + " rather than cobblestone");
        helper.assertValueEqual(simulated.getCount(), 64, "what the simulated extract would take");

        helper.assertValueEqual(crate.getLargeItemStack(0).getAmount(), 2000, "the amount left after a simulated extract");
        helper.assertValueEqual(handler.getStackInSlot(0).getCount(), 2000, "the amount reported after a simulated extract");

        // A real one really does take its items.
        ItemStack extracted = handler.extractItem(0, 64, false);
        helper.assertValueEqual(extracted.getCount(), 64, "what the extract took");

        helper.assertValueEqual(crate.getLargeItemStack(0).getAmount(), 1936, "the amount left after a real extract");
        helper.succeed();
    }
}
