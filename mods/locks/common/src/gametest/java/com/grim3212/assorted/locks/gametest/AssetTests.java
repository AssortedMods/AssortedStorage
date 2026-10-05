package com.grim3212.assorted.locks.gametest;

import net.minecraft.locale.Language;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.server.MinecraftServer;
import com.grim3212.assorted.lib.platform.Services;
import java.io.BufferedReader;
import java.io.IOException;
import com.grim3212.assorted.locks.Constants;
import com.grim3212.assorted.locks.common.block.LocksBlocks;
import com.grim3212.assorted.locks.common.handlers.LocksCreativeItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.lib.test.TestSupport.*;
import static com.grim3212.assorted.locks.gametest.LocksTestSupport.*;

/**
 * What the mod ships: models and names, door textures, tag names and recipes that load.
 */
final class AssetTests {

    private AssetTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("every_block_and_item_has_a_model_and_a_name", AssetTests::everyBlockAndItemHasAModelAndAName);
        out.accept("every_recipe_loads_or_is_conditioned_off", AssetTests::everyRecipeLoadsOrIsConditionedOff);
        out.accept("every_item_tag_has_a_name", AssetTests::everyItemTagHasAName);
        out.accept("every_locked_door_has_its_textures", AssetTests::everyLockedDoorHasItsTextures);
        out.accept("locked_container_items_show_their_padlock", AssetTests::lockedContainerItemsShowTheirPadlock);
    }

    /**
     * The creative tab's locked containers draw their padlock. The chests and shulker box use a special renderer that
     * reads the stack's lock, the barrel's item model branches on it, and the hopper's sprite has the padlock drawn on.
     */
    private static void lockedContainerItemsShowTheirPadlock(GameTestHelper helper) {
        List<String> wrong = new ArrayList<>();

        for (String chest : List.of("locked_chest", "locked_ender_chest")) {
            JsonObject model = itemModel(chest);
            if (model == null || !"minecraft:special".equals(string(model, "type")) || !"assortedlocks:locked_chest".equals(string(model.getAsJsonObject("model"), "type"))) {
                wrong.add(chest + " is not drawn by assortedlocks:locked_chest");
            }
        }

        JsonObject shulker = itemModel("locked_shulker_box");
        if (shulker == null || !"minecraft:special".equals(string(shulker, "type")) || !"assortedlocks:locked_shulker_box".equals(string(shulker.getAsJsonObject("model"), "type"))) {
            wrong.add("locked_shulker_box is not drawn by assortedlocks:locked_shulker_box");
        }

        JsonObject barrel = itemModel("locked_barrel");
        if (barrel == null || !"minecraft:condition".equals(string(barrel, "type")) || !"assortedlocks:locked".equals(string(barrel, "property"))
                || !"assortedlocks:block/locked_barrel_locked".equals(string(barrel.getAsJsonObject("on_true"), "model"))) {
            wrong.add("locked_barrel does not draw assortedlocks:block/locked_barrel_locked when locked");
        }

        JsonObject hopper = itemModel("locked_hopper");
        JsonObject hopperModel = json("/assets/assortedlocks/models/item/locked_hopper.json");
        if (hopper == null || hopperModel == null || !"assortedlocks:item/locked_hopper".equals(string(hopperModel.getAsJsonObject("textures"), "layer0"))) {
            wrong.add("locked_hopper is not drawn with its padlock sprite");
        }
        if (!resourceExists("/assets/assortedlocks/textures/item/locked_hopper.png")) {
            wrong.add("the locked hopper's sprite is missing");
        }

        helper.assertTrue(wrong.isEmpty(), wrong.size() + " locked item model problem(s): " + String.join("; ", wrong));
        helper.succeed();
    }

    private static JsonObject itemModel(String path) {
        JsonObject item = json("/assets/assortedlocks/items/" + path + ".json");
        return item == null ? null : item.getAsJsonObject("model");
    }

    /**
     * Every locked door's generated model names two textures that are actually in the jar. A texture
     * a model asks for and does not get draws as the missing-texture checkerboard, which is the only
     * warning there is; this reads the model rather than guessing the path, so the waxed copper doors
     * pointing at the unwaxed pngs is checked rather than assumed.
     */
    private static void everyLockedDoorHasItsTextures(GameTestHelper helper) {
        List<String> missing = new ArrayList<>();

        for (Block door : LocksBlocks.lockedDoors()) {
            JsonObject model = json("/assets/" + Constants.MOD_ID + "/models/block/" + name(door) + "_bottom_left.json");
            if (model == null) {
                missing.add(name(door) + " has no bottom model");
                continue;
            }

            JsonObject textures = model.getAsJsonObject("textures");
            for (String slot : new String[]{"bottom", "top"}) {
                String texture = string(textures, slot);
                if (texture == null) {
                    missing.add(name(door) + " names no " + slot + " texture");
                    continue;
                }

                Identifier id = Identifier.parse(texture);
                if (!resourceExists("/assets/" + id.getNamespace() + "/textures/" + id.getPath() + ".png")) {
                    missing.add(name(door) + " " + slot + " points at " + texture + ", which is not in the jar");
                }
            }
        }

        helper.assertTrue(missing.isEmpty(), missing.size() + " door texture problem(s): " + String.join(", ", missing));
        helper.succeed();
    }

    /** Every block and item has a model and a name. Every gap is reported at once. */
    private static void everyBlockAndItemHasAModelAndAName(GameTestHelper helper) {
        JsonObject lang = lang(helper);
        List<String> missing = new ArrayList<>();

        for (Map.Entry<ResourceKey<Block>, Block> entry : BuiltInRegistries.BLOCK.entrySet()) {
            Identifier id = entry.getKey().identifier();
            if (!Constants.MOD_ID.equals(id.getNamespace())) {
                continue;
            }

            if (!resourceExists("/assets/" + Constants.MOD_ID + "/blockstates/" + id.getPath() + ".json")) {
                missing.add("blockstate " + id.getPath());
            }
            if (!lang.has(entry.getValue().getDescriptionId())) {
                missing.add("lang key " + entry.getValue().getDescriptionId());
            }
        }

        for (Map.Entry<ResourceKey<Item>, Item> entry : BuiltInRegistries.ITEM.entrySet()) {
            Identifier id = entry.getKey().identifier();
            if (!Constants.MOD_ID.equals(id.getNamespace())) {
                continue;
            }

            if (!resourceExists("/assets/" + Constants.MOD_ID + "/items/" + id.getPath() + ".json")) {
                missing.add("item model " + id.getPath());
            }
            if (!lang.has(entry.getValue().getDescriptionId())) {
                missing.add("lang key " + entry.getValue().getDescriptionId());
            }
        }

        helper.assertTrue(BuiltInRegistries.CREATIVE_MODE_TAB.containsKey(LocksCreativeItems.TAB), "the Assorted Storage creative tab is not registered");
        helper.assertTrue(lang.has("itemGroup." + Constants.FAMILY_ID), "the Assorted Storage creative tab has no name");

        helper.assertTrue(missing.isEmpty(), missing.size() + " missing assets: " + String.join(", ", missing));
        helper.succeed();
    }

    /**
     * Every recipe file either loaded or was skipped by this loader's own load conditions; anything
     * else failed to parse.
     */
    private static void everyRecipeLoadsOrIsConditionedOff(GameTestHelper helper) {
        MinecraftServer server = helper.getLevel().getServer();
        FileToIdConverter recipes = FileToIdConverter.json("recipe");
        String conditionsKey = Services.PLATFORM.getPlatformName().equals("Fabric") ? "fabric:load_conditions" : "neoforge:conditions";
        List<String> failed = new ArrayList<>();

        recipes.listMatchingResources(server.getResourceManager()).forEach((file, resource) -> {
            Identifier id = recipes.fileToId(file);
            if (!id.getNamespace().equals(Constants.MOD_ID) || server.getRecipeManager().byKey(ResourceKey.create(Registries.RECIPE, id)).isPresent()) {
                return;
            }

            try (BufferedReader reader = resource.openAsReader()) {
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                if (!json.has(conditionsKey)) {
                    failed.add(id.toString());
                }
            } catch (IOException e) {
                failed.add(id + " (" + e.getMessage() + ")");
            }
        });

        helper.assertTrue(failed.isEmpty(), failed.size() + " recipes failed to load without being conditioned off: " + String.join(", ", failed.subList(0, Math.min(10, failed.size()))));
        helper.succeed();
    }

    /**
     * Every non-vanilla item tag has a {@code tag.item.<namespace>.<path>} name, the check Fabric
     * API warns about at dev startup. Both loaders name the standard c: tags, so anything missing
     * is ours.
     */
    private static void everyItemTagHasAName(GameTestHelper helper) {
        Language language = Language.getInstance();
        List<String> missing = helper.getLevel().registryAccess().lookupOrThrow(Registries.ITEM).getTags()
                .map(tag -> tag.key().location())
                .filter(id -> !"minecraft".equals(id.getNamespace()))
                .map(id -> "tag.item." + id.getNamespace() + "." + id.getPath().replace('/', '.'))
                .filter(key -> !language.has(key))
                .sorted()
                .toList();
        helper.assertTrue(missing.isEmpty(), "item tags with no name in any lang file: " + missing);
        helper.succeed();
    }
}
