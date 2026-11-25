package com.grim3212.assorted.hoppers.gametest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.grim3212.assorted.hoppers.Constants;
import com.grim3212.assorted.hoppers.common.handlers.HoppersCreativeItems;
import com.grim3212.assorted.lib.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.locale.Language;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.hoppers.gametest.HoppersTestSupport.*;
import static com.grim3212.assorted.lib.test.TestSupport.*;

/**
 * What the mod ships: models and names, loader keys both loaders read, and recipes that load.
 */
final class AssetTests {

    private AssetTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("every_block_and_item_has_a_model_and_a_name", AssetTests::everyBlockAndItemHasAModelAndAName);
        out.accept("loader_models_are_read_on_both_loaders", AssetTests::loaderModelsAreReadOnBothLoaders);
        out.accept("every_recipe_loads_or_is_conditioned_off", AssetTests::everyRecipeLoadsOrIsConditionedOff);
        out.accept("every_item_tag_has_a_name", AssetTests::everyItemTagHasAName);
    }

    /** Every block and item has a model and a name. Every gap is reported at once. */
    private static void everyBlockAndItemHasAModelAndAName(GameTestHelper helper) {
        JsonObject lang = lang(helper);
        List<String> missing = new ArrayList<>();

        for (Map.Entry<ResourceKey<Block>, Block> entry : BuiltInRegistries.BLOCK.entrySet()) {
            ResourceLocation id = entry.getKey().location();
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
            ResourceLocation id = entry.getKey().location();
            if (!Constants.MOD_ID.equals(id.getNamespace())) {
                continue;
            }

            // 1.21.1 reads an item's model straight from models/item; the items/ definitions came with 1.21.4.
            if (!resourceExists("/assets/" + Constants.MOD_ID + "/models/item/" + id.getPath() + ".json")) {
                missing.add("item model " + id.getPath());
            }
            if (!lang.has(entry.getValue().getDescriptionId())) {
                missing.add("lang key " + entry.getValue().getDescriptionId());
            }
        }

        helper.assertTrue(BuiltInRegistries.CREATIVE_MODE_TAB.containsKey(HoppersCreativeItems.TAB), "the Assorted Storage creative tab is not registered");
        helper.assertTrue(lang.has("itemGroup." + HoppersCreativeItems.TAB.location().getNamespace()), "the Assorted Storage creative tab has no name");

        helper.assertTrue(missing.isEmpty(), missing.size() + " missing assets: " + String.join(", ", missing));
        helper.succeed();
    }

    /**
     * Every hopper's blockstate reaches the {@code assortedhoppers:locked} loader model, named by the one
     * {@code "loader"} key both loaders read in 1.21.1 (Fabric through AssortedLib), and that model names both of
     * its children. A blockstate pointing at a plain model never shows the lock, and nothing warns; the 26.x keys,
     * a variant {@code "type"} and a model's {@code "fabric:type"}, are read by neither loader here.
     */
    private static void loaderModelsAreReadOnBothLoaders(GameTestHelper helper) {
        String lockedLoader = Constants.MOD_ID + ":locked";
        List<String> wrong = new ArrayList<>();
        int loaderModels = 0;

        for (Map.Entry<ResourceKey<Block>, Block> entry : BuiltInRegistries.BLOCK.entrySet()) {
            ResourceLocation id = entry.getKey().location();
            if (!Constants.MOD_ID.equals(id.getNamespace())) {
                continue;
            }
            JsonObject blockstate = json("/assets/" + id.getNamespace() + "/blockstates/" + id.getPath() + ".json");
            if (blockstate == null) {
                continue;
            }

            for (JsonObject variant : blockstateVariants(blockstate)) {
                if (variant.has("type") || variant.has("fabric:type")) {
                    wrong.add("blockstate " + id.getPath() + " still has the 26.x key " + (variant.has("type") ? "type" : "fabric:type"));
                }

                String model = variant.has("model") ? variant.get("model").getAsString() : null;
                if (model == null || !model.startsWith(Constants.MOD_ID + ":")) {
                    wrong.add("blockstate " + id.getPath() + " names " + model + " rather than one of this mod's models");
                    continue;
                }

                ResourceLocation modelId = ResourceLocation.parse(model);
                JsonObject modelJson = json("/assets/" + modelId.getNamespace() + "/models/" + modelId.getPath() + ".json");
                if (modelJson == null) {
                    wrong.add("blockstate " + id.getPath() + " names " + model + ", which does not exist");
                    continue;
                }
                if (modelJson.has("fabric:type")) {
                    wrong.add("model " + model + " still has the 26.x key fabric:type");
                }
                if (!modelJson.has("loader") || !lockedLoader.equals(modelJson.get("loader").getAsString())) {
                    wrong.add("model " + model + " has loader " + modelJson.get("loader") + " rather than " + lockedLoader);
                    continue;
                }
                loaderModels++;
                for (String child : List.of("unlocked", "locked")) {
                    ResourceLocation childId = modelJson.has(child) ? ResourceLocation.parse(modelJson.get(child).getAsString()) : null;
                    if (childId == null || json("/assets/" + childId.getNamespace() + "/models/" + childId.getPath() + ".json") == null) {
                        wrong.add("model " + model + " names no " + child + " model that exists");
                    }
                }
            }
        }

        helper.assertTrue(loaderModels > 0, "no locked loader models were found to check");
        helper.assertTrue(wrong.isEmpty(), wrong.size() + " json(s) that would draw a static model: " + String.join("; ", wrong));
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
            ResourceLocation id = recipes.fileToId(file);
            if (!id.getNamespace().equals(Constants.MOD_ID) || server.getRecipeManager().byKey(id).isPresent()) {
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
        List<String> missing = helper.getLevel().registryAccess().lookupOrThrow(Registries.ITEM).listTags()
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
