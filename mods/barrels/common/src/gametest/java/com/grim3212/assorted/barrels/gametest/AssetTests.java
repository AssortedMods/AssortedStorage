package com.grim3212.assorted.barrels.gametest;

import net.minecraft.locale.Language;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.server.MinecraftServer;
import com.grim3212.assorted.lib.platform.Services;
import java.io.BufferedReader;
import java.io.IOException;
import com.grim3212.assorted.barrels.Constants;
import com.grim3212.assorted.barrels.common.block.BarrelsBlocks;
import com.grim3212.assorted.barrels.common.handlers.BarrelsCreativeItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.barrels.gametest.BarrelsTestSupport.*;
import static com.grim3212.assorted.lib.test.TestSupport.blockstateVariants;

/**
 * What the mod ships: models and names, barrel item padlocks, loader keys both loaders read, item tag names and recipes that load.
 */
final class AssetTests {

    private AssetTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("every_block_and_item_has_a_model_and_a_name", AssetTests::everyBlockAndItemHasAModelAndAName);
        out.accept("barrel_items_show_their_padlock", AssetTests::barrelItemsShowTheirPadlock);
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

            if (!resourceExists("/assets/" + Constants.MOD_ID + "/models/item/" + id.getPath() + ".json")) {
                missing.add("item model " + id.getPath());
            }
            if (!lang.has(entry.getValue().getDescriptionId())) {
                missing.add("lang key " + entry.getValue().getDescriptionId());
            }
        }

        helper.assertTrue(BuiltInRegistries.CREATIVE_MODE_TAB.containsKey(BarrelsCreativeItems.TAB), "the Assorted Storage creative tab is not registered");
        helper.assertTrue(lang.has("itemGroup." + Constants.FAMILY_ID), "the Assorted Storage creative tab has no name");

        helper.assertTrue(missing.isEmpty(), missing.size() + " missing assets: " + String.join(", ", missing));
        helper.succeed();
    }

    /**
     * Every material barrel's item shows its padlock when locked. An item has no block entity for the locked loader
     * model to read, so the item json has to choose between the two models on the stack's lock.
     */
    private static void barrelItemsShowTheirPadlock(GameTestHelper helper) {
        List<Block> barrels = new ArrayList<>();
        BarrelsBlocks.BARRELS.values().forEach(barrel -> barrels.add(barrel.get()));
        helper.assertTrue(barrels.size() > 1, "only " + barrels.size() + " barrels are registered");

        List<String> wrong = new ArrayList<>();
        for (Block barrel : barrels) {
            String path = name(barrel);
            // 1.21.1 item models choose a child through "overrides" on a registered item property.
            String locked = Constants.MOD_ID + ":block/" + path + "_locked";
            String unlocked = Constants.MOD_ID + ":block/" + path + "_unlocked";
            JsonObject item = json("/assets/" + Constants.MOD_ID + "/models/item/" + path + ".json");
            if (item == null) {
                wrong.add(path + " has no item model");
                continue;
            }
            if (!unlocked.equals(string(item, "parent"))) {
                wrong.add(path + " does not draw " + unlocked + " when unlocked, got " + string(item, "parent"));
            }

            boolean drawsLocked = false;
            if (item.has("overrides")) {
                for (JsonElement element : item.getAsJsonArray("overrides")) {
                    JsonObject override = element.getAsJsonObject();
                    JsonObject predicate = override.getAsJsonObject("predicate");
                    if (predicate != null && predicate.has(Constants.MOD_ID + ":locked") && predicate.get(Constants.MOD_ID + ":locked").getAsFloat() == 1.0F && locked.equals(string(override, "model"))) {
                        drawsLocked = true;
                    }
                }
            }
            if (!drawsLocked) {
                wrong.add(path + " does not draw " + locked + " when " + Constants.MOD_ID + ":locked is 1");
            }

            JsonObject lockedModel = json("/assets/" + Constants.MOD_ID + "/models/block/" + path + "_locked.json");
            String top = lockedModel == null ? null : string(lockedModel.getAsJsonObject("textures"), "top");
            if (top == null || !top.contains("locked_barrel_top")) {
                wrong.add(path + "_locked has no padlock top, got " + top);
            }
        }

        helper.assertTrue(wrong.isEmpty(), wrong.size() + " barrel item model problem(s): " + String.join("; ", wrong));
        helper.succeed();
    }

    /**
     * Every loader model a blockstate names is read on both loaders. On 1.21.1 both read NeoForge's
     * {@code "loader"} key (AssortedLib's Fabric side looks it up too), so each one names the loader
     * this mod registers and children that exist; a 26.x {@code "fabric:type"} or variant
     * {@code "type"} key would be read by neither, and the barrel would draw as a static model.
     */
    private static void loaderModelsAreReadOnBothLoaders(GameTestHelper helper) {
        // BarrelsClient names it, but is a client class the test server cannot load.
        String loader = Constants.MOD_ID + ":locked";
        List<String> wrong = new ArrayList<>();
        List<String> checkedModels = new ArrayList<>();
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
                    wrong.add("blockstate " + id.getPath() + " has a 26.x custom variant " + variant);
                }

                String model = variant.has("model") ? variant.get("model").getAsString() : null;
                if (model == null || checkedModels.contains(model) || !model.startsWith(Constants.MOD_ID + ":")) {
                    continue;
                }
                checkedModels.add(model);

                ResourceLocation modelId = ResourceLocation.parse(model);
                JsonObject modelJson = json("/assets/" + modelId.getNamespace() + "/models/" + modelId.getPath() + ".json");
                if (modelJson == null) {
                    wrong.add("model " + model + " does not exist");
                    continue;
                }
                if (modelJson.has("fabric:type")) {
                    wrong.add("model " + model + " has a 26.x fabric:type key");
                }
                if (!modelJson.has("loader")) {
                    continue;
                }

                loaderModels++;
                if (!loader.equals(string(modelJson, "loader"))) {
                    wrong.add("model " + model + " has loader " + modelJson.get("loader") + " but this mod registers " + loader);
                }
                for (String child : List.of("unlocked", "locked")) {
                    String childModel = string(modelJson, child);
                    ResourceLocation childId = childModel == null ? null : ResourceLocation.tryParse(childModel);
                    if (childId == null || json("/assets/" + childId.getNamespace() + "/models/" + childId.getPath() + ".json") == null) {
                        wrong.add("model " + model + " names no existing " + child + " child, got " + childModel);
                    }
                }
            }
        }

        helper.assertTrue(loaderModels > 0, "no loader models were found to check");
        helper.assertTrue(wrong.isEmpty(), wrong.size() + " json(s) Fabric would read as static: " + String.join("; ", wrong));
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
