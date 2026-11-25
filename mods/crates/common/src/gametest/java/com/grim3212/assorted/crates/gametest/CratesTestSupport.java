package com.grim3212.assorted.crates.gametest;

import com.mojang.authlib.GameProfile;
import io.netty.channel.embedded.EmbeddedChannel;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.grim3212.assorted.crates.Constants;
import com.grim3212.assorted.crates.api.Wood;
import com.grim3212.assorted.lib.core.inventory.locking.ILockable;
import com.grim3212.assorted.crates.common.block.CratesBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Helpers, constants and fixtures shared by Assorted Crates' gametest classes, which import them
 * statically, alongside AssortedLib's {@code TestSupport}.
 */
final class CratesTestSupport {

    private CratesTestSupport() {
    }

    static final BlockPos BLOCK = new BlockPos(4, 1, 4);

    static final String CODE = "3212";

    /**
     * A creative server player who is never placed in the level or the player list, so nothing a
     * crate syncs to the players near it reaches them: NeoForge refuses a payload to a connection
     * that negotiated no channels. It still has a connection of its own, which changing its game
     * mode writes to.
     */
    static ServerPlayer unplacedCreativePlayer(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        CommonListenerCookie cookie = CommonListenerCookie.createInitial(new GameProfile(UUID.randomUUID(), "crates-test-creative"), false);
        ServerPlayer player = new ServerPlayer(level.getServer(), level, cookie.gameProfile(), cookie.clientInformation());
        Connection connection = new Connection(PacketFlow.SERVERBOUND);
        new EmbeddedChannel(connection);
        new ServerGamePacketListenerImpl(level.getServer(), connection, player, cookie);
        player.setGameMode(GameType.CREATIVE);
        helper.assertTrue(player.isCreative(), "the unplaced test player is not in creative");
        return player;
    }

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
        onClient.loadWithComponents(placed.getUpdateTag(registries), registries);
        helper.assertValueEqual(((ILockable) onClient).getLockCode(), CODE, name + " lock code as a client receives it");
    }

    /** A json off the mod's own classpath, or null if it is not there. */
    static JsonObject json(String path) {
        try (InputStream in = CratesTestSupport.class.getResourceAsStream(path)) {
            return in == null ? null : JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
        } catch (Exception e) {
            return null;
        }
    }

    /** The mod's own en_us.json, off the classpath - it is a resource even on a headless server. */
    static JsonObject lang(GameTestHelper helper) {
        try (InputStream in = CratesTestSupport.class.getResourceAsStream("/assets/" + Constants.MOD_ID + "/lang/en_us.json")) {
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
        try (InputStream in = CratesTestSupport.class.getResourceAsStream(path)) {
            return in != null;
        } catch (Exception e) {
            return false;
        }
    }

    static Block oakCrate() {
        return CratesBlocks.CRATES.stream().filter(group -> group.getType() == Wood.OAK).findFirst().orElseThrow().SINGLE.get();
    }

    static Block oakCrateQuadruple() {
        return CratesBlocks.CRATES.stream().filter(group -> group.getType() == Wood.OAK).findFirst().orElseThrow().QUADRUPLE.get();
    }
}
