package com.grim3212.assorted.chests.gametest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.grim3212.assorted.chests.Constants;
import com.grim3212.assorted.chests.common.block.ChestsBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Helpers, constants and fixtures shared by Assorted Chests' gametest classes, which import them
 * statically, alongside AssortedLib's {@code TestSupport}.
 */
final class ChestsTestSupport {

    private ChestsTestSupport() {
    }

    static final String CODE = "3212";

    /** Every material chest. */
    static List<Block> storageBlocks() {
        List<Block> blocks = new ArrayList<>();
        ChestsBlocks.CHESTS.values().forEach(chest -> blocks.add(chest.get()));
        return blocks;
    }

    /**
     * One marker item per block under test, so a dropped stack can only have come from one of
     * them. Long enough for every position {@link #spread(int)} hands out.
     */
    static final List<Item> MARKERS = List.of(
            Items.DIAMOND, Items.EMERALD, Items.GOLD_INGOT, Items.IRON_INGOT, Items.COPPER_INGOT,
            Items.COAL, Items.REDSTONE, Items.LAPIS_LAZULI, Items.QUARTZ, Items.AMETHYST_SHARD,
            Items.ECHO_SHARD, Items.FLINT, Items.BONE, Items.STRING, Items.PAPER,
            Items.BRICK, Items.CLAY_BALL, Items.SUGAR, Items.APPLE, Items.FEATHER,
            Items.WHEAT, Items.LEATHER, Items.SLIME_BALL, Items.GLOWSTONE_DUST, Items.BLAZE_ROD);

    /** Positions two apart on the test box floor, so nothing a block drops lands on a neighbour. */
    static BlockPos spread(int index) {
        return new BlockPos((index % 5) * 2, 1, (index / 5) * 2);
    }

    static String name(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    /** The mod's own en_us.json, off the classpath - it is a resource even on a headless server. */
    static JsonObject lang(GameTestHelper helper) {
        try (InputStream in = ChestsTestSupport.class.getResourceAsStream("/assets/" + Constants.MOD_ID + "/lang/en_us.json")) {
            helper.assertTrue(in != null, "this mod ships no en_us.json");
            return JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
        } catch (Exception e) {
            throw helper.assertionException("could not read en_us.json: " + e);
        }
    }

    static boolean resourceExists(String path) {
        try (InputStream in = ChestsTestSupport.class.getResourceAsStream(path)) {
            return in != null;
        } catch (Exception e) {
            return false;
        }
    }
}
