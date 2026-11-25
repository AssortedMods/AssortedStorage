package com.grim3212.assorted.crates.gametest;

import com.grim3212.assorted.crates.Constants;
import com.grim3212.assorted.lib.test.TestFunctions;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.TestFunction;
import net.neoforged.neoforge.gametest.GameTestHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Registers this mod's test functions on NeoForge. It lives in the gametest source set, so nothing in
 * {@code main} references it and the tests never ship. NeoForge finds the {@link GameTestHolder} by
 * its annotation and asks the generator for the tests, built from the shared list and test instance jsons.
 */
@GameTestHolder(Constants.MOD_ID)
public final class CratesNeoForgeGameTests {

    private CratesNeoForgeGameTests() {
    }

    @GameTestGenerator
    public static Collection<TestFunction> tests() {
        List<TestFunction> tests = new ArrayList<>(TestFunctions.of(Constants.MOD_ID, CratesGameTests::forEach, CratesNeoForgeGameTests.class::getResourceAsStream));
        // NeoForge only: what the mod looks like through this loader's own item handler.
        tests.addAll(TestFunctions.of(Constants.MOD_ID, CrateTransferTests::register, CratesNeoForgeGameTests.class::getResourceAsStream));
        return tests;
    }
}
