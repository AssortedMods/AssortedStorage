package com.grim3212.assorted.locks.gametest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.grim3212.assorted.lib.core.inventory.locking.LockConversions;
import com.grim3212.assorted.locks.Constants;
import com.grim3212.assorted.locks.common.inventory.LocksmithWorkbenchContainer;
import com.grim3212.assorted.locks.common.network.SetLockPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static com.grim3212.assorted.lib.test.TestSupport.craft;

/**
 * Helpers, constants and fixtures shared by Assorted Locks' gametest classes, which import them
 * statically, alongside AssortedLib's {@code TestSupport}.
 */
final class LocksTestSupport {

    private LocksTestSupport() {
    }

    static final BlockPos BLOCK = new BlockPos(4, 1, 4);

    static final String CODE = "3212";

    /** A json off the mod's own classpath, or null if it is not there. */
    static JsonObject json(String path) {
        try (InputStream in = LocksTestSupport.class.getResourceAsStream(path)) {
            return in == null ? null : JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
        } catch (Exception e) {
            return null;
        }
    }

    static String string(JsonObject object, String key) {
        return object != null && object.has(key) ? object.get(key).getAsString() : null;
    }

    /** Positions two apart on the test box floor, so nothing a block drops lands on a neighbour. */
    static BlockPos spread(int index) {
        return new BlockPos((index % 5) * 2, 1, (index / 5) * 2);
    }

    static String name(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    /** Hangs an oak door at {@code lower} and puts a padlock set to {@code code} on it, the way the padlock item does. */
    static void lockedOakDoor(GameTestHelper helper, BlockPos lower, String code) {
        helper.setBlock(lower, Blocks.OAK_DOOR.defaultBlockState().setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER));
        helper.setBlock(lower.above(), Blocks.OAK_DOOR.defaultBlockState().setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER));
        helper.assertTrue(LockConversions.tryLock(helper.getLevel(), helper.absolutePos(lower), code), "an oak door refused a coded padlock");
    }

    /** Right clicks the north face of a door with whatever the player is carrying. */
    static InteractionResult knock(GameTestHelper helper, Player player, BlockPos door) {
        BlockPos pos = helper.absolutePos(door);
        BlockHitResult hit = new BlockHitResult(Vec3.atCenterOf(pos), Direction.NORTH, pos, false);
        BlockState state = helper.getLevel().getBlockState(pos);
        return state.useItemOn(player.getItemInHand(InteractionHand.MAIN_HAND), helper.getLevel(), player, InteractionHand.MAIN_HAND, hit);
    }

    /** The result of the one crafting recipe that matches {@code input}. */
    static ItemStack crafted(GameTestHelper helper, CraftingInput input) {
        return craft(helper, input, "the ingredients laid out");
    }

    /** Puts {@code blank} in the workbench, types {@link #CODE} at it, and takes the result. */
    static ItemStack typeCode(GameTestHelper helper, ServerPlayer player, ItemStack blank) {
        LocksmithWorkbenchContainer menu = (LocksmithWorkbenchContainer) player.containerMenu;
        menu.getSlot(0).set(blank);

        SetLockPacket.handle(new SetLockPacket(CODE), player);
        ItemStack result = menu.getSlot(1).getItem();
        helper.assertFalse(result.isEmpty(), "the workbench made nothing from a typed code");

        menu.getSlot(0).set(ItemStack.EMPTY);
        return result;
    }

    /** The mod's own en_us.json, off the classpath - it is a resource even on a headless server. */
    static JsonObject lang(GameTestHelper helper) {
        try (InputStream in = LocksTestSupport.class.getResourceAsStream("/assets/" + Constants.MOD_ID + "/lang/en_us.json")) {
            helper.assertTrue(in != null, "this mod ships no en_us.json");
            return JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
        } catch (Exception e) {
            throw helper.assertionException("could not read en_us.json: " + e);
        }
    }

    static boolean resourceExists(String path) {
        try (InputStream in = LocksTestSupport.class.getResourceAsStream(path)) {
            return in != null;
        } catch (Exception e) {
            return false;
        }
    }
}
