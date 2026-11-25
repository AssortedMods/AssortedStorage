package com.grim3212.assorted.levelupgrades.gametest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.grim3212.assorted.levelupgrades.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.entity.BlockEntity;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Helpers shared by Assorted Level Upgrades' gametest classes, which import them statically,
 * alongside AssortedLib's {@code TestSupport}.
 */
final class LevelUpgradesTestSupport {

    private LevelUpgradesTestSupport() {
    }

    /** The mod's own en_us.json, off the classpath - it is a resource even on a headless server. */
    static JsonObject lang(GameTestHelper helper) {
        try (InputStream in = LevelUpgradesTestSupport.class.getResourceAsStream("/assets/" + Constants.MOD_ID + "/lang/en_us.json")) {
            helper.assertTrue(in != null, "this mod ships no en_us.json");
            return JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
        } catch (Exception e) {
            throw new GameTestAssertException("could not read en_us.json: " + e);
        }
    }

    /** The block entity at a test position, failing the test if it is missing or of another type. */
    static <T extends BlockEntity> T blockEntity(GameTestHelper helper, BlockPos pos, Class<T> type) {
        BlockEntity found = helper.getBlockEntity(pos);
        if (!type.isInstance(found)) {
            throw new GameTestAssertException("expected a " + type.getSimpleName() + " at " + pos + " but found " + found);
        }
        return type.cast(found);
    }

    static boolean resourceExists(String path) {
        try (InputStream in = LevelUpgradesTestSupport.class.getResourceAsStream(path)) {
            return in != null;
        } catch (Exception e) {
            return false;
        }
    }
}
