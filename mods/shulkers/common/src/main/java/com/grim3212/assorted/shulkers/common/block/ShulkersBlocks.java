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
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ShulkersBlocks {

    public static final RegistryProvider<Block> BLOCKS = RegistryProvider.create(Registries.BLOCK, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);
    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);

    // Lambdas, as the types register after the blocks that name them.
    public static final StorageTypes<LockedShulkerBoxBlockEntity, LockedMaterialContainer> TYPES = new StorageTypes<>(() -> ShulkersBlockEntityTypes.LOCKED_SHULKER_BOX.get(), () -> ShulkersContainerTypes.LOCKED_SHULKER_BOX.get());

    public static final Map<StorageMaterial, IRegistryObject<LockedShulkerBoxBlock>> SHULKERS = new EnumMap<>(StorageMaterial.class);

    static {
        Stream.of(StorageMaterial.values()).forEach((type) -> SHULKERS.put(type, register("shulker_box_" + type.toString(), () -> new LockedShulkerBoxBlock(type, TYPES, type.getProps()), block -> new ShulkerBoxBlockItem(block, itemProperties(type), ShulkersDataComponents.STORAGE_INFO))));
    }

    public static void initDispenserHandlers() {
        for (IRegistryObject<LockedShulkerBoxBlock> b : SHULKERS.values()) {
            DispenserBlock.registerBehavior(b.get(), new ShulkerBoxDispenseBehavior());
        }
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
