package com.grim3212.assorted.bags.client.data;

import com.grim3212.assorted.bags.Constants;
import com.grim3212.assorted.bags.client.properties.HasStorageTagProperty;
import com.grim3212.assorted.bags.common.item.BagItem;
import com.grim3212.assorted.bags.common.item.BagsItems;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

/**
 * Item models for the bags: each bag's model picks one of its variants through {@code overrides} on
 * {@link HasStorageTagProperty}, and {@code BagColor} tints the body (layer 0) and the strap (layer 1) with the dye.
 */
public class BagsItemModelProvider extends ItemModelProvider {

    private static final ResourceLocation GENERATED = ResourceLocation.withDefaultNamespace("item/generated");

    public BagsItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Assorted Bags item models";
    }

    @Override
    protected void registerModels() {
        enderBag(BagsItems.ENDER_BAG.get());

        bag(BagsItems.BAG.get());
        for (IRegistryObject<BagItem> bag : BagsItems.BAGS.values()) {
            materialBag(bag.get());
        }
    }

    /** The ender bag carries no dye, only a padlock, so it has a single override. */
    private void enderBag(Item item) {
        String name = name(item);
        ModelFile locked = this.withExistingParent(name + "_locked", GENERATED).texture("layer0", itemTexture("ender_bag_locked"));
        this.withExistingParent(name, GENERATED).texture("layer0", itemTexture("ender_bag"))
                .override().predicate(HasStorageTagProperty.LOCKED.id(), 1.0F).model(locked).end();
    }

    /** The plain bag: its own body plus the shared strap. */
    private void bag(Item item) {
        String name = name(item);
        bagTree(variant(name, "", "bag", "bag_strap", null),
                variant(name, "_colored", "bag_colored", "bag_strap", null),
                variant(name, "_locked", "bag_lock_cutout", "bag_strap_lock_cutout", null),
                variant(name, "_locked_colored", "bag_colored_lock_cutout", "bag_strap_lock_cutout", null));
    }

    /** A material bag: the shared body and strap with the material's own colour layer on top. */
    private void materialBag(BagItem item) {
        String name = name(item);
        bagTree(variant(name, "", "bag_material", "bag_strap", name),
                variant(name, "_colored", "bag_material_colored", "bag_strap", name),
                variant(name, "_locked", "bag_material_lock_cutout", "bag_strap_lock_cutout", name),
                variant(name, "_locked_colored", "bag_material_colored_lock_cutout", "bag_strap_lock_cutout", name));
    }

    /**
     * Gives the plain model the overrides that pick the other three. The lock picks the texture set; {@code dyed}
     * picks the greyscale body that takes the tint, because tinting the plain body comes out muddy. The last
     * override that matches wins, so the one needing both comes last.
     */
    private void bagTree(ItemModelBuilder plain, ModelFile colored, ModelFile locked, ModelFile lockedColored) {
        plain.override().predicate(HasStorageTagProperty.DYED.id(), 1.0F).model(colored).end()
                .override().predicate(HasStorageTagProperty.LOCKED.id(), 1.0F).model(locked).end()
                .override().predicate(HasStorageTagProperty.LOCKED.id(), 1.0F).predicate(HasStorageTagProperty.DYED.id(), 1.0F).model(lockedColored).end();
    }

    /**
     * One bag variant: body and strap on layers 0 and 1, which take the tint, then the material and the padlock
     * above them, left alone the way vanilla's leather armour leaves its overlay untinted.
     */
    private ItemModelBuilder variant(String name, String suffix, String body, String strap, @Nullable String material) {
        ItemModelBuilder model = this.withExistingParent(name + suffix, GENERATED)
                .texture("layer0", itemTexture(body))
                .texture("layer1", itemTexture(strap));
        int layer = 2;
        if (material != null) {
            model.texture("layer" + layer++, itemTexture(material));
        }
        if (suffix.startsWith("_locked")) {
            model.texture("layer" + layer, itemTexture("bag_lock"));
        }
        return model;
    }

    private static String name(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).getPath();
    }

    private static ResourceLocation itemTexture(String name) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "item/" + name);
    }
}
