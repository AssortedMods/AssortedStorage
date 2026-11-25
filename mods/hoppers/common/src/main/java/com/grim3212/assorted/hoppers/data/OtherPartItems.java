package com.grim3212.assorted.hoppers.data;

import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Ingredients naming another part's item, which is not registered while this mod's data is generated. 1.21.1 writes
 * an item ingredient from the item itself, so each is built as a tag ingredient under a placeholder id, and
 * {@link #wrap} rewrites that to the plain {@code {"item": ...}} ingredient as the file is saved.
 */
final class OtherPartItems {

    private final String placeholderPrefix;

    OtherPartItems(String modId) {
        this.placeholderPrefix = modId + ":other_part_item/";
    }

    /** An ingredient of {@code modId:path}, written as that item once it goes through {@link #wrap}. */
    Ingredient of(String modId, String path) {
        ResourceLocation placeholder = ResourceLocation.parse(this.placeholderPrefix + modId + "/" + path);
        return Ingredient.of(TagKey.create(Registries.ITEM, placeholder));
    }

    /** The output, with every placeholder ingredient {@link #of} made turned into the item it stands for. */
    CachedOutput wrap(CachedOutput output) {
        return (path, data, hash) -> {
            if (!path.toString().endsWith(".json")) {
                output.writeIfNeeded(path, data, hash);
                return;
            }

            JsonElement json = JsonParser.parseString(new String(data, StandardCharsets.UTF_8));
            if (!this.replace(json)) {
                output.writeIfNeeded(path, data, hash);
                return;
            }

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            @SuppressWarnings("deprecation") // sha1 is what the data generator's cache compares against
            HashingOutputStream hashed = new HashingOutputStream(Hashing.sha1(), bytes);
            try (JsonWriter writer = new JsonWriter(new OutputStreamWriter(hashed, StandardCharsets.UTF_8))) {
                writer.setSerializeNulls(false);
                writer.setIndent("  ");
                GsonHelper.writeValue(writer, json, DataProvider.KEY_COMPARATOR);
            } catch (IOException e) {
                throw new IllegalStateException("Cannot write " + path, e);
            }
            output.writeIfNeeded(path, bytes.toByteArray(), hashed.hash());
        };
    }

    /** Rewrites every placeholder tag ingredient in place, and says whether there was one. */
    private boolean replace(JsonElement json) {
        boolean changed = false;
        if (json.isJsonArray()) {
            for (JsonElement element : json.getAsJsonArray()) {
                changed |= this.replace(element);
            }
        } else if (json.isJsonObject()) {
            JsonObject object = json.getAsJsonObject();
            if (object.size() == 1 && object.has("tag") && object.get("tag").isJsonPrimitive()
                    && object.get("tag").getAsString().startsWith(this.placeholderPrefix)) {
                String item = object.get("tag").getAsString().substring(this.placeholderPrefix.length());
                int split = item.indexOf('/');
                object.remove("tag");
                object.addProperty("item", item.substring(0, split) + ":" + item.substring(split + 1));
                return true;
            }
            for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                changed |= this.replace(entry.getValue());
            }
        }
        return changed;
    }
}
