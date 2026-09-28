package com.grim3212.assorted.hoppers.common.block;

import com.grim3212.assorted.hoppers.Constants;
import com.grim3212.assorted.hoppers.common.item.HoppersBlockItem;
import com.grim3212.assorted.hoppers.common.block.blockentity.HoppersBlockEntityTypes;
import com.grim3212.assorted.hoppers.common.inventory.HoppersContainerTypes;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.core.storage.StorageTypes;
import com.grim3212.assorted.lib.core.storage.hopper.LockedHopperBlock;
import com.grim3212.assorted.lib.core.storage.hopper.LockedHopperBlockEntity;
import com.grim3212.assorted.lib.core.storage.hopper.LockedHopperContainer;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class HoppersBlocks {

    public static final RegistryProvider<Block> BLOCKS = RegistryProvider.create(Registries.BLOCK, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);
    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);

    // Lambdas, as the types register after the blocks that name them.
    public static final StorageTypes<LockedHopperBlockEntity, LockedHopperContainer> TYPES = new StorageTypes<>(() -> HoppersBlockEntityTypes.LOCKED_HOPPER.get(), () -> HoppersContainerTypes.LOCKED_HOPPER.get());

    public static final Map<StorageMaterial, IRegistryObject<LockedHopperBlock>> HOPPERS = new EnumMap<>(StorageMaterial.class);

    static {
        Stream.of(StorageMaterial.values()).forEach((type) -> {
            HOPPERS.put(type, register("hopper_" + type.toString(), key -> new LockedHopperBlock(type, TYPES, type.getProps().setId(key)), itemProperties(type)));
        });
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
        return type == StorageMaterial.NETHERITE ? (key) -> itemProperties(key).fireResistant() : HoppersBlocks::itemProperties;
    }

    // Since 1.21.2 a block has to know its own registry id before it is constructed, so the
    // registration name is turned into a ResourceKey here and handed to the factory to put on the
    // properties. The same is true of items.
    private static <T extends LockedHopperBlock> IRegistryObject<T> register(String name, Function<ResourceKey<Block>, ? extends T> factory, Function<ResourceKey<Item>, Item.Properties> itemProperties) {
        final ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        IRegistryObject<T> ret = BLOCKS.register(name, () -> factory.apply(key));
        final ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        ITEMS.register(name, () -> new HoppersBlockItem(ret.get(), itemProperties.apply(itemKey)));
        return ret;
    }

    public static void init() {

    }
}
