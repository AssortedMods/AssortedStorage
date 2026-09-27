package com.grim3212.assorted.chests.common.block;

import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.chests.Constants;
import com.grim3212.assorted.chests.Family;
import com.grim3212.assorted.lib.core.storage.LockedMaterialContainer;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.core.storage.StorageTypes;
import com.grim3212.assorted.lib.core.storage.chest.LockedChestBlock;
import com.grim3212.assorted.lib.core.storage.chest.LockedChestBlockEntity;
import com.grim3212.assorted.chests.common.block.blockentity.ChestsBlockEntityTypes;
import com.grim3212.assorted.chests.common.inventory.ChestsContainerTypes;
import com.grim3212.assorted.chests.common.item.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class ChestsBlocks {

    public static final RegistryProvider<Block> BLOCKS = RegistryProvider.create(Registries.BLOCK, Constants.MOD_ID).aliasFrom(Family.ID);
    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Family.ID);

    // Lambdas, as the types register after the blocks that name them.
    public static final StorageTypes<LockedChestBlockEntity, LockedMaterialContainer> TYPES = new StorageTypes<>(() -> ChestsBlockEntityTypes.LOCKED_CHEST.get(), () -> ChestsContainerTypes.LOCKED_CHEST.get());

    public static final Map<StorageMaterial, IRegistryObject<LockedChestBlock>> CHESTS = new EnumMap<>(StorageMaterial.class);

    static {
        Stream.of(StorageMaterial.values()).forEach((type) -> CHESTS.put(type, registerChest("chest_" + type.toString(), key -> new LockedChestBlock(type, TYPES, type.getProps().setId(key)), itemProperties(type))));
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
        return type == StorageMaterial.NETHERITE ? (key) -> itemProperties(key).fireResistant() : ChestsBlocks::itemProperties;
    }

    private static <T extends LockedChestBlock> IRegistryObject<T> registerChest(String name, Function<ResourceKey<Block>, ? extends T> factory, Function<ResourceKey<Item>, Item.Properties> itemProperties) {
        return register(name, factory, name, (block, key) -> new ChestBlockItem(block.get(), itemProperties.apply(key)));
    }

    private static <T extends Block> IRegistryObject<T> register(String name, Function<ResourceKey<Block>, ? extends T> factory, String itemName, ItemFactory<T> itemFactory) {
        IRegistryObject<T> ret = registerNoItem(name, factory);
        final ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, itemName));
        ITEMS.register(itemName, () -> itemFactory.create(ret, itemKey));
        return ret;
    }

    // Since 1.21.2 a block has to know its own registry id before it is constructed, so the
    // registration name is turned into a ResourceKey here and handed to the factory to put on the
    // properties. The same is true of items.
    private static <T extends Block> IRegistryObject<T> registerNoItem(String name, Function<ResourceKey<Block>, ? extends T> factory) {
        final ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        return BLOCKS.register(name, () -> factory.apply(key));
    }

    @FunctionalInterface
    private interface ItemFactory<T extends Block> {
        BlockItem create(IRegistryObject<? extends Block> block, ResourceKey<Item> key);
    }

    public static void init() {

    }
}
