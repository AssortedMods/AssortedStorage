package com.grim3212.assorted.containers.common.block;

import com.grim3212.assorted.containers.Constants;
import com.grim3212.assorted.containers.api.Wood;
import com.grim3212.assorted.containers.common.item.ContainersBlockItem;
import com.grim3212.assorted.containers.common.item.LockerItem;
import com.grim3212.assorted.containers.common.item.WarehouseCrateBlockItem;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
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

    public static final IRegistryObject<WoodCabinetBlock> WOOD_CABINET = registerStorageItem("wood_cabinet", props -> new WoodCabinetBlock(props.mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD)));
    public static final IRegistryObject<GlassCabinetBlock> GLASS_CABINET = registerStorageItem("glass_cabinet", props -> new GlassCabinetBlock(props.mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD)));
    public static final IRegistryObject<GoldSafeBlock> GOLD_SAFE = registerStorageItem("gold_safe", props -> new GoldSafeBlock(props.mapColor(MapColor.METAL).sound(SoundType.METAL)));
    public static final IRegistryObject<ObsidianSafeBlock> OBSIDIAN_SAFE = registerStorageItem("obsidian_safe", props -> new ObsidianSafeBlock(props.mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE)));
    public static final IRegistryObject<LockerBlock> LOCKER = registerLocker("locker", props -> new LockerBlock(props.mapColor(MapColor.METAL).sound(SoundType.METAL)));
    public static final IRegistryObject<ItemTowerBlock> ITEM_TOWER = registerStorageItem("item_tower", props -> new ItemTowerBlock(props.mapColor(MapColor.METAL).sound(SoundType.METAL)));

    public static final Map<Wood, IRegistryObject<WarehouseCrateBlock>> WAREHOUSE_CRATES = new EnumMap<>(Wood.class);

    static {
        Stream.of(Wood.values()).forEach((type) -> WAREHOUSE_CRATES.put(type, registerCrate(type + "_warehouse_crate", props -> new WarehouseCrateBlock(type, warehouseCrateProps(props)))));
    }

    private static BlockBehaviour.Properties warehouseCrateProps(BlockBehaviour.Properties props) {
        return props.mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD).strength(3.0f, 5.0f);
    }

    private static <T extends Block> IRegistryObject<T> registerCrate(String name, Function<BlockBehaviour.Properties, ? extends T> factory) {
        return register(name, factory, block -> new WarehouseCrateBlockItem(block.get(), new Item.Properties()));
    }

    private static <T extends Block> IRegistryObject<T> registerStorageItem(String name, Function<BlockBehaviour.Properties, ? extends T> factory) {
        return register(name, factory, block -> new ContainersBlockItem(block.get(), new Item.Properties()));
    }

    private static <T extends Block> IRegistryObject<T> registerLocker(String name, Function<BlockBehaviour.Properties, ? extends T> factory) {
        return register(name, factory, block -> new LockerItem(block.get(), new Item.Properties()));
    }

    private static <T extends Block> IRegistryObject<T> register(String name, Function<BlockBehaviour.Properties, ? extends T> factory, Function<IRegistryObject<T>, ? extends BlockItem> itemFactory) {
        IRegistryObject<T> ret = BLOCKS.register(name, () -> factory.apply(BlockBehaviour.Properties.of()));
        ITEMS.register(name, () -> itemFactory.apply(ret));
        return ret;
    }

    public static void init() {

    }
}
