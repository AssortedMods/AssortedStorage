package com.grim3212.assorted.barrels.gametest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.grim3212.assorted.barrels.Constants;
import com.grim3212.assorted.lib.core.inventory.locking.ILockable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.TagValueInput;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Helpers, constants and fixtures shared by Assorted Barrels' gametest classes, which import them
 * statically, alongside AssortedLib's {@code TestSupport}.
 */
final class BarrelsTestSupport {

    private BarrelsTestSupport() {
    }

    static final String CODE = "3212";

    static void assertLockReachesClients(GameTestHelper helper, BlockPos rel, Block block) {
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

    /** A json off the mod's own classpath, or null if it is not there. */
    static JsonObject json(String path) {
        try (InputStream in = BarrelsTestSupport.class.getResourceAsStream(path)) {
            return in == null ? null : JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
        } catch (Exception e) {
            return null;
        }
    }

    static String string(JsonObject object, String key) {
        return object != null && object.has(key) ? object.get(key).getAsString() : null;
    }

    static String name(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    /** The mod's own en_us.json, off the classpath - it is a resource even on a headless server. */
    static JsonObject lang(GameTestHelper helper) {
        try (InputStream in = BarrelsTestSupport.class.getResourceAsStream("/assets/" + Constants.MOD_ID + "/lang/en_us.json")) {
            helper.assertTrue(in != null, "this mod ships no en_us.json");
            return JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
        } catch (Exception e) {
            throw helper.assertionException("could not read en_us.json: " + e);
        }
    }

    static boolean resourceExists(String path) {
        try (InputStream in = BarrelsTestSupport.class.getResourceAsStream(path)) {
            return in != null;
        } catch (Exception e) {
            return false;
        }
    }
}
