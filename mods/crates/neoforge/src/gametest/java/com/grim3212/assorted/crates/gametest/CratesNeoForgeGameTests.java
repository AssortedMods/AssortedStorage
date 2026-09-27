package com.grim3212.assorted.crates.gametest;

import com.grim3212.assorted.crates.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

/**
 * Registers this mod's test functions on NeoForge. It lives in the gametest source set, so nothing
 * in {@code main} references it and release builds do not contain it.
 */
@EventBusSubscriber(modid = Constants.MOD_ID)
public final class CratesNeoForgeGameTests {

    private CratesNeoForgeGameTests() {
    }

    @SubscribeEvent
    public static void registerGameTests(final RegisterEvent event) {
        event.register(Registries.TEST_FUNCTION, helper -> {
            CratesGameTests.forEach((name, function) -> helper.register(Identifier.fromNamespaceAndPath(Constants.MOD_ID, name), function));
            // NeoForge only: what the mod looks like through this loader's own transfer API.
            CrateTransferTests.register((name, function) -> helper.register(Identifier.fromNamespaceAndPath(Constants.MOD_ID, name), function));
        });
    }
}
