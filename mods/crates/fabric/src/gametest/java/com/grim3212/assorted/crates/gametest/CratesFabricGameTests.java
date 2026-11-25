package com.grim3212.assorted.crates.gametest;

import com.grim3212.assorted.crates.Constants;
import com.grim3212.assorted.lib.test.TestFunctions;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.TestFunction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Registers this mod's test functions on Fabric. The gametest source set is a dev-only second mod, so
 * nothing in {@code main} references it and the tests never ship. It is that mod's {@code fabric-gametest}
 * entrypoint, whose generator Fabric API hands to vanilla's registry.
 */
public class CratesFabricGameTests {

    @GameTestGenerator
    public Collection<TestFunction> tests() {
        List<TestFunction> tests = new ArrayList<>(TestFunctions.of(Constants.MOD_ID, CratesGameTests::forEach, CratesFabricGameTests.class::getResourceAsStream));
        // Fabric only: what the mod looks like through this loader's own transfer API.
        tests.addAll(TestFunctions.of(Constants.MOD_ID, CrateTransferTests::register, CratesFabricGameTests.class::getResourceAsStream));
        return tests;
    }
}
