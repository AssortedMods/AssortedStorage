package com.grim3212.assorted.barrels.common.block;

import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.barrels.Constants;
import com.grim3212.assorted.lib.core.storage.LockedMaterialContainer;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.core.storage.StorageTypes;
import com.grim3212.assorted.lib.core.storage.barrel.LockedBarrelBlock;
import com.grim3212.assorted.lib.core.storage.barrel.LockedBarrelBlockEntity;
import com.grim3212.assorted.barrels.common.block.blockentity.BarrelsBlockEntityTypes;
import com.grim3212.assorted.barrels.common.inventory.BarrelsContainerTypes;
import com.grim3212.assorted.barrels.common.item.BarrelsBlockItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class BarrelsBlocks {

    public static final RegistryProvider<Block> BLOCKS = RegistryProvider.create(Registries.BLOCK, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);
    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);

    // Lambdas, as the types register after the blocks that name them.
    public static final StorageTypes<LockedBarrelBlockEntity, LockedMaterialContainer> TYPES = new StorageTypes<>(() -> BarrelsBlockEntityTypes.LOCKED_BARREL.get(), () -> BarrelsContainerTypes.LOCKED_BARREL.get());

    public static final Map<StorageMaterial, IRegistryObject<LockedBarrelBlock>> BARRELS = new EnumMap<>(StorageMaterial.class);

    static {
        Stream.of(StorageMaterial.values()).forEach((type) -> BARRELS.put(type, register("barrel_" + type.toString(), () -> new LockedBarrelBlock(type, TYPES, type.getProps()), block -> new BarrelsBlockItem(block, itemProperties(type)))));
    }

    /** Netherite storage keeps its fire resistance; everything else takes plain item properties. */
    private static Item.Properties itemProperties(StorageMaterial type) {
        Item.Properties props = new Item.Properties();
        return type == StorageMaterial.NETHERITE ? props.fireResistant() : props;
    }

    private static <T extends Block> IRegistryObject<T> register(String name, Supplier<? extends T> block, Function<T, ? extends Item> item) {
        IRegistryObject<T> ret = BLOCKS.register(name, block);
        ITEMS.register(name, () -> item.apply(ret.get()));
        return ret;
    }

    public static void init() {

    }
}
