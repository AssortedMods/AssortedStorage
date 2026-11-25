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
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class HoppersBlocks {

    public static final RegistryProvider<Block> BLOCKS = RegistryProvider.create(Registries.BLOCK, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);
    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);

    // Lambdas, as the types register after the blocks that name them.
    public static final StorageTypes<LockedHopperBlockEntity, LockedHopperContainer> TYPES = new StorageTypes<>(() -> HoppersBlockEntityTypes.LOCKED_HOPPER.get(), () -> HoppersContainerTypes.LOCKED_HOPPER.get());

    public static final Map<StorageMaterial, IRegistryObject<LockedHopperBlock>> HOPPERS = new EnumMap<>(StorageMaterial.class);

    static {
        Stream.of(StorageMaterial.values()).forEach((type) -> {
            HOPPERS.put(type, register("hopper_" + type.toString(), () -> new LockedHopperBlock(type, TYPES, type.getProps()), itemProperties(type)));
        });
    }

    /** Netherite storage keeps its fire resistance; everything else takes plain item properties. */
    private static Supplier<Item.Properties> itemProperties(StorageMaterial type) {
        return type == StorageMaterial.NETHERITE ? () -> new Item.Properties().fireResistant() : Item.Properties::new;
    }

    private static <T extends LockedHopperBlock> IRegistryObject<T> register(String name, Supplier<? extends T> block, Supplier<Item.Properties> itemProperties) {
        IRegistryObject<T> ret = BLOCKS.register(name, block);
        ITEMS.register(name, () -> new HoppersBlockItem(ret.get(), itemProperties.get()));
        return ret;
    }

    public static void init() {

    }
}
