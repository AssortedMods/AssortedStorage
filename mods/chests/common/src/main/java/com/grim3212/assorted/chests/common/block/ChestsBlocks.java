package com.grim3212.assorted.chests.common.block;

import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.chests.Constants;
import com.grim3212.assorted.lib.core.storage.LockedMaterialContainer;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.core.storage.StorageTypes;
import com.grim3212.assorted.lib.core.storage.chest.LockedChestBlock;
import com.grim3212.assorted.lib.core.storage.chest.LockedChestBlockEntity;
import com.grim3212.assorted.chests.common.block.blockentity.ChestsBlockEntityTypes;
import com.grim3212.assorted.chests.common.inventory.ChestsContainerTypes;
import com.grim3212.assorted.chests.common.item.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ChestsBlocks {

    public static final RegistryProvider<Block> BLOCKS = RegistryProvider.create(Registries.BLOCK, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);
    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);

    // Lambdas, as the types register after the blocks that name them.
    public static final StorageTypes<LockedChestBlockEntity, LockedMaterialContainer> TYPES = new StorageTypes<>(() -> ChestsBlockEntityTypes.LOCKED_CHEST.get(), () -> ChestsContainerTypes.LOCKED_CHEST.get());

    public static final Map<StorageMaterial, IRegistryObject<LockedChestBlock>> CHESTS = new EnumMap<>(StorageMaterial.class);

    static {
        Stream.of(StorageMaterial.values()).forEach((type) -> CHESTS.put(type, register("chest_" + type.toString(), () -> new LockedChestBlock(type, TYPES, type.getProps()), block -> new ChestBlockItem(block, itemProperties(type)))));
    }

    /** Netherite storage keeps its fire resistance; everything else takes plain item properties. */
    private static Item.Properties itemProperties(StorageMaterial type) {
        Item.Properties props = new Item.Properties();
        return type == StorageMaterial.NETHERITE ? props.fireResistant() : props;
    }

    private static <T extends Block> IRegistryObject<T> register(String name, Supplier<? extends T> block, Function<T, ? extends BlockItem> item) {
        IRegistryObject<T> ret = BLOCKS.register(name, block);
        ITEMS.register(name, () -> item.apply(ret.get()));
        return ret;
    }

    public static void init() {

    }
}
