package com.grim3212.assorted.barrels.common.block;

import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.barrels.Constants;
import com.grim3212.assorted.barrels.Family;
import com.grim3212.assorted.lib.core.storage.LockedMaterialContainer;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.core.storage.StorageTypes;
import com.grim3212.assorted.lib.core.storage.barrel.LockedBarrelBlock;
import com.grim3212.assorted.lib.core.storage.barrel.LockedBarrelBlockEntity;
import com.grim3212.assorted.barrels.common.block.blockentity.BarrelsBlockEntityTypes;
import com.grim3212.assorted.barrels.common.inventory.BarrelsContainerTypes;
import com.grim3212.assorted.barrels.common.item.BarrelsBlockItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class BarrelsBlocks {

    public static final RegistryProvider<Block> BLOCKS = RegistryProvider.create(Registries.BLOCK, Constants.MOD_ID).aliasFrom(Family.ID);
    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Family.ID);

    // Lambdas, as the types register after the blocks that name them.
    public static final StorageTypes<LockedBarrelBlockEntity, LockedMaterialContainer> TYPES = new StorageTypes<>(() -> BarrelsBlockEntityTypes.LOCKED_BARREL.get(), () -> BarrelsContainerTypes.LOCKED_BARREL.get());

    public static final Map<StorageMaterial, IRegistryObject<LockedBarrelBlock>> BARRELS = new EnumMap<>(StorageMaterial.class);

    static {
        Stream.of(StorageMaterial.values()).forEach((type) -> BARRELS.put(type, register("barrel_" + type.toString(), key -> new LockedBarrelBlock(type, TYPES, type.getProps().setId(key)), itemProperties(type))));
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
        return type == StorageMaterial.NETHERITE ? (key) -> itemProperties(key).fireResistant() : BarrelsBlocks::itemProperties;
    }

    // Since 1.21.2 a block has to know its own registry id before it is constructed, so the
    // registration name is turned into a ResourceKey here and handed to the factory to put on the
    // properties. The same is true of items.
    private static <T extends Block> IRegistryObject<T> register(String name, Function<ResourceKey<Block>, ? extends T> factory, Function<ResourceKey<Item>, Item.Properties> itemProperties) {
        final ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        IRegistryObject<T> block = BLOCKS.register(name, () -> factory.apply(key));
        final ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        ITEMS.register(name, () -> new BarrelsBlockItem(block.get(), itemProperties.apply(itemKey)));
        return block;
    }

    public static void init() {

    }
}
