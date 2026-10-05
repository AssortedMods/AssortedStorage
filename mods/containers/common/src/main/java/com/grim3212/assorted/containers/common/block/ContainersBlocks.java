package com.grim3212.assorted.containers.common.block;

import com.grim3212.assorted.containers.Constants;
import com.grim3212.assorted.containers.api.Wood;
import com.grim3212.assorted.containers.common.item.ContainersBlockItem;
import com.grim3212.assorted.containers.common.item.LockerItem;
import com.grim3212.assorted.containers.common.item.WarehouseCrateBlockItem;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class ContainersBlocks {

    public static final RegistryProvider<Block> BLOCKS = RegistryProvider.create(Registries.BLOCK, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);
    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);

    public static final IRegistryObject<WoodCabinetBlock> WOOD_CABINET = registerStorageItem("wood_cabinet", key -> new WoodCabinetBlock(Block.Properties.of().setId(key).mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD)));
    public static final IRegistryObject<GlassCabinetBlock> GLASS_CABINET = registerStorageItem("glass_cabinet", key -> new GlassCabinetBlock(Block.Properties.of().setId(key).mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD)));
    public static final IRegistryObject<GoldSafeBlock> GOLD_SAFE = registerStorageItem("gold_safe", key -> new GoldSafeBlock(Block.Properties.of().setId(key).mapColor(MapColor.METAL).sound(SoundType.METAL)));
    public static final IRegistryObject<ObsidianSafeBlock> OBSIDIAN_SAFE = registerStorageItem("obsidian_safe", key -> new ObsidianSafeBlock(Block.Properties.of().setId(key).mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE)));
    public static final IRegistryObject<LockerBlock> LOCKER = registerLocker("locker", key -> new LockerBlock(Block.Properties.of().setId(key).mapColor(MapColor.METAL).sound(SoundType.METAL)));
    public static final IRegistryObject<ItemTowerBlock> ITEM_TOWER = registerStorageItem("item_tower", key -> new ItemTowerBlock(Block.Properties.of().setId(key).mapColor(MapColor.METAL).sound(SoundType.METAL)));

    public static final Map<Wood, IRegistryObject<WarehouseCrateBlock>> WAREHOUSE_CRATES = new EnumMap<>(Wood.class);

    static {
        Stream.of(Wood.values()).forEach((type) -> WAREHOUSE_CRATES.put(type, registerCrate(type + "_warehouse_crate", key -> new WarehouseCrateBlock(type, warehouseCrateProps(key)))));
    }

    private static BlockBehaviour.Properties warehouseCrateProps(ResourceKey<Block> key) {
        return Block.Properties.of().setId(key).mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD).strength(3.0f, 5.0f);
    }

    /**
     * Item properties carry a registry id since 1.21.2, so they have to be built per registration
     * rather than shared.
     */
    private static Item.Properties itemProperties(ResourceKey<Item> key) {
        return new Item.Properties().useBlockDescriptionPrefix().setId(key);
    }

    private static <T extends Block> IRegistryObject<T> registerCrate(String name, Function<ResourceKey<Block>, ? extends T> factory) {
        return register(name, factory, (block, key) -> new WarehouseCrateBlockItem(block.get(), itemProperties(key)));
    }

    private static <T extends Block> IRegistryObject<T> registerStorageItem(String name, Function<ResourceKey<Block>, ? extends T> factory) {
        return register(name, factory, (block, key) -> new ContainersBlockItem(block.get(), itemProperties(key)));
    }

    private static <T extends Block> IRegistryObject<T> registerLocker(String name, Function<ResourceKey<Block>, ? extends T> factory) {
        return register(name, factory, (block, key) -> new LockerItem(block.get(), itemProperties(key)));
    }

    // Since 1.21.2 a block has to know its own registry id before it is constructed, so the
    // registration name is turned into a ResourceKey here and handed to the factory to put on the
    // properties. The same is true of items.
    private static <T extends Block> IRegistryObject<T> register(String name, Function<ResourceKey<Block>, ? extends T> factory, ItemFactory<T> itemFactory) {
        final ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        IRegistryObject<T> ret = BLOCKS.register(name, () -> factory.apply(key));
        final ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        ITEMS.register(name, () -> itemFactory.create(ret, itemKey));
        return ret;
    }

    @FunctionalInterface
    private interface ItemFactory<T extends Block> {
        BlockItem create(IRegistryObject<? extends Block> block, ResourceKey<Item> key);
    }

    public static void init() {

    }
}
