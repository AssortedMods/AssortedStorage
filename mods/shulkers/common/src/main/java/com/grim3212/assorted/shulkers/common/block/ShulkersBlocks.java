package com.grim3212.assorted.shulkers.common.block;

import com.grim3212.assorted.lib.core.storage.LockedMaterialContainer;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.core.storage.StorageTypes;
import com.grim3212.assorted.lib.core.storage.shulker.LockedShulkerBoxBlock;
import com.grim3212.assorted.lib.core.storage.shulker.LockedShulkerBoxBlockEntity;
import com.grim3212.assorted.lib.core.storage.shulker.ShulkerBoxBlockItem;
import com.grim3212.assorted.shulkers.common.block.blockentity.ShulkersBlockEntityTypes;
import com.grim3212.assorted.shulkers.common.inventory.ShulkersContainerTypes;
import com.grim3212.assorted.shulkers.common.item.ShulkersDataComponents;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.shulkers.Constants;
import net.minecraft.core.dispenser.ShulkerBoxDispenseBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class ShulkersBlocks {

    public static final RegistryProvider<Block> BLOCKS = RegistryProvider.create(Registries.BLOCK, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);
    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);

    // Lambdas, as the types register after the blocks that name them.
    public static final StorageTypes<LockedShulkerBoxBlockEntity, LockedMaterialContainer> TYPES = new StorageTypes<>(() -> ShulkersBlockEntityTypes.LOCKED_SHULKER_BOX.get(), () -> ShulkersContainerTypes.LOCKED_SHULKER_BOX.get());

    public static final Map<StorageMaterial, IRegistryObject<LockedShulkerBoxBlock>> SHULKERS = new EnumMap<>(StorageMaterial.class);

    static {
        Stream.of(StorageMaterial.values()).forEach((type) -> {
            SHULKERS.put(type, registerShulker("shulker_box_" + type.toString(), key -> new LockedShulkerBoxBlock(type, TYPES, type.getProps().setId(key)), itemProperties(type)));
        });
    }

    public static void initDispenserHandlers() {
        for (IRegistryObject<LockedShulkerBoxBlock> b : SHULKERS.values()) {
            DispenserBlock.registerBehavior(b.get(), new ShulkerBoxDispenseBehavior());
        }
    }

    /**
     * Netherite storage keeps its fire resistance; everything else takes plain item properties.
     * Item properties carry a registry id since 1.21.2, so they have to be built per registration
     * rather than shared, which is why this is a factory.
     */
    private static Item.Properties itemProperties(ResourceKey<Item> key) {
        return new Item.Properties().useBlockDescriptionPrefix().setId(key);
    }

    private static Function<ResourceKey<Item>, Item.Properties> itemProperties(StorageMaterial type) {
        return type == StorageMaterial.NETHERITE ? (key) -> itemProperties(key).fireResistant() : ShulkersBlocks::itemProperties;
    }

    // Since 1.21.2 a block has to know its own registry id before it is constructed, so the
    // registration name is turned into a ResourceKey here and handed to the factory to put on the
    // properties. The same is true of items.
    private static <T extends LockedShulkerBoxBlock> IRegistryObject<T> registerShulker(String name, Function<ResourceKey<Block>, ? extends T> factory, Function<ResourceKey<Item>, Item.Properties> itemProperties) {
        final ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        IRegistryObject<T> ret = BLOCKS.register(name, () -> factory.apply(key));
        final ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        ITEMS.register(name, () -> new ShulkerBoxBlockItem(ret.get(), itemProperties.apply(itemKey), ShulkersDataComponents.STORAGE_INFO));
        return ret;
    }

    public static void init() {

    }
}
