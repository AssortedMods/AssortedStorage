package com.grim3212.assorted.chests.gametest;

import net.minecraft.gametest.framework.GameTestHelper;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Automated in-world checks for Assorted Chests. The tests live in small {@code <Feature>Tests} classes;
 * this only lists them. Each name needs a matching {@code data/assortedchests/test_instance/<name>.json}.
 */
public final class ChestsGameTests {

    private ChestsGameTests() {
    }

    /** Every test in this mod, named once, so both loaders register the same set. */
    public static void forEach(BiConsumer<String, Consumer<GameTestHelper>> out) {
        AssetTests.register(out);
        AliasTests.register(out);
        CrossLoaderDataTests.register(out);
        ChestsBlockTests.register(out);
        MenuTests.register(out);
        TooltipTests.register(out);
        LockTests.register(out);
    }
}
