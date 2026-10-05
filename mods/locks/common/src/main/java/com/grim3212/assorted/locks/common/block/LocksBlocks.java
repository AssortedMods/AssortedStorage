package com.grim3212.assorted.locks.common.block;

import com.grim3212.assorted.lib.core.storage.LockedMaterialContainer;
import com.grim3212.assorted.lib.core.storage.StorageBlockItem;
import com.grim3212.assorted.lib.core.storage.StorageTypes;
import com.grim3212.assorted.lib.core.storage.barrel.LockedBarrelBlock;
import com.grim3212.assorted.lib.core.storage.barrel.LockedBarrelBlockEntity;
import com.grim3212.assorted.lib.core.storage.chest.LockedChestBlock;
import com.grim3212.assorted.lib.core.storage.chest.LockedChestBlockEntity;
import com.grim3212.assorted.lib.core.storage.hopper.LockedHopperBlock;
import com.grim3212.assorted.lib.core.storage.hopper.LockedHopperBlockEntity;
import com.grim3212.assorted.lib.core.storage.hopper.LockedHopperContainer;
import com.grim3212.assorted.lib.core.storage.shulker.LockedShulkerBoxBlock;
import com.grim3212.assorted.lib.core.storage.shulker.LockedShulkerBoxBlockEntity;
import com.grim3212.assorted.lib.core.storage.shulker.ShulkerBoxBlockItem;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.locks.Constants;
import com.grim3212.assorted.locks.api.Wood;
import com.grim3212.assorted.locks.common.block.blockentity.LocksBlockEntityTypes;
import com.grim3212.assorted.locks.common.inventory.LocksContainerTypes;
import com.grim3212.assorted.locks.common.item.LocksDataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public class LocksBlocks {

    public static final RegistryProvider<Block> BLOCKS = RegistryProvider.create(Registries.BLOCK, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);
    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);

    /** Assorted Building Blocks' doors, by id since it may not be installed. They were Assorted Decor's. */
    public static final String BUILDING_BLOCKS_ID = "assortedbuildingblocks";
    public static final Identifier QUARTZ_DOOR = Identifier.fromNamespaceAndPath(BUILDING_BLOCKS_ID, "quartz_door");
    public static final Identifier GLASS_DOOR = Identifier.fromNamespaceAndPath(BUILDING_BLOCKS_ID, "glass_door");
    public static final Identifier STEEL_DOOR = Identifier.fromNamespaceAndPath(BUILDING_BLOCKS_ID, "steel_door");
    public static final Identifier CHAIN_LINK_DOOR = Identifier.fromNamespaceAndPath(BUILDING_BLOCKS_ID, "chain_link_door");

    /**
     * Every locked door that stands in for a vanilla one, keyed on that vanilla door: what the padlock converts
     * through and what the loot tables drop. Declared before the first {@link #registerVanillaDoor} call, which fills it.
     */
    public static final Map<Block, IRegistryObject<LockedDoorBlock>> VANILLA_DOORS = new LinkedHashMap<>();

    public static final IRegistryObject<LocksmithWorkbenchBlock> LOCKSMITH_WORKBENCH = register("locksmith_workbench", key -> new LocksmithWorkbenchBlock(Block.Properties.of().setId(key).mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD).strength(3.0f, 5.0f)));

    // Lambdas, as the types register after the blocks that name them.
    public static final StorageTypes<LockedChestBlockEntity, LockedMaterialContainer> CHEST_TYPES = new StorageTypes<>(() -> LocksBlockEntityTypes.LOCKED_CHEST.get(), () -> LocksContainerTypes.LOCKED_CHEST.get());
    public static final StorageTypes<LockedBarrelBlockEntity, LockedMaterialContainer> BARREL_TYPES = new StorageTypes<>(() -> LocksBlockEntityTypes.LOCKED_BARREL.get(), () -> LocksContainerTypes.LOCKED_BARREL.get());
    public static final StorageTypes<LockedHopperBlockEntity, LockedHopperContainer> HOPPER_TYPES = new StorageTypes<>(() -> LocksBlockEntityTypes.LOCKED_HOPPER.get(), () -> LocksContainerTypes.LOCKED_HOPPER.get());
    public static final StorageTypes<LockedShulkerBoxBlockEntity, LockedMaterialContainer> SHULKER_TYPES = new StorageTypes<>(() -> LocksBlockEntityTypes.LOCKED_SHULKER_BOX.get(), () -> LocksContainerTypes.LOCKED_SHULKER_BOX.get());

    public static final IRegistryObject<LockedEnderChestBlock> LOCKED_ENDER_CHEST = registerStorage("locked_ender_chest", key -> new LockedEnderChestBlock(Block.Properties.of().setId(key).mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(22.5F, 600.0F).lightLevel(state -> 7)), LocksBlocks::storageItem);
    public static final IRegistryObject<LockedChestBlock> LOCKED_CHEST = registerStorage("locked_chest", key -> new LockedChestBlock(null, CHEST_TYPES, woodStorage(key)), LocksBlocks::storageItem);
    public static final IRegistryObject<LockedShulkerBoxBlock> LOCKED_SHULKER_BOX = registerStorage("locked_shulker_box", key -> new LockedShulkerBoxBlock(null, SHULKER_TYPES, Block.Properties.of().setId(key).mapColor(MapColor.COLOR_PURPLE)), LocksBlocks::shulkerItem);
    public static final IRegistryObject<LockedBarrelBlock> LOCKED_BARREL = registerStorage("locked_barrel", key -> new LockedBarrelBlock(null, BARREL_TYPES, woodStorage(key)), LocksBlocks::storageItem);
    public static final IRegistryObject<LockedHopperBlock> LOCKED_HOPPER = registerStorage("locked_hopper", key -> new LockedHopperBlock(null, HOPPER_TYPES, Block.Properties.of().setId(key).mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3.0F, 4.8F).sound(SoundType.METAL).noOcclusion()), LocksBlocks::storageItem);

    public static final IRegistryObject<LockedDoorBlock> LOCKED_IRON_DOOR = registerVanillaDoor(Blocks.IRON_DOOR, key -> new LockedDoorBlock((DoorBlock) Blocks.IRON_DOOR, Block.Properties.of().setId(key).mapColor(Blocks.IRON_DOOR.defaultMapColor()).requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.METAL).noOcclusion()));

    public static final IRegistryObject<LockedDoorBlock> LOCKED_QUARTZ_DOOR = registerNoItem("locked_quartz_door", key -> new LockedDoorBlock(QUARTZ_DOOR, BlockSetType.IRON, Block.Properties.of().setId(key).mapColor(MapColor.QUARTZ).requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.METAL).noOcclusion()));
    public static final IRegistryObject<LockedDoorBlock> LOCKED_GLASS_DOOR = registerNoItem("locked_glass_door", key -> new LockedDoorBlock(GLASS_DOOR, BlockSetType.IRON, Block.Properties.of().setId(key).mapColor(Blocks.GLASS.defaultMapColor()).instrument(NoteBlockInstrument.HAT).strength(0.75F, 7.5F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryObject<LockedDoorBlock> LOCKED_STEEL_DOOR = registerNoItem("locked_steel_door", key -> new LockedDoorBlock(STEEL_DOOR, BlockSetType.IRON, Block.Properties.of().setId(key).mapColor(MapColor.METAL).strength(1.0F, 10.0F).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion()));
    public static final IRegistryObject<LockedDoorBlock> LOCKED_CHAIN_LINK_DOOR = registerNoItem("locked_chain_link_door", key -> new LockedDoorBlock(CHAIN_LINK_DOOR, BlockSetType.IRON, Block.Properties.of().setId(key).mapColor(MapColor.METAL).strength(0.5F, 5.0F).sound(SoundType.METAL).noOcclusion()));

    static {
        Stream.of(Wood.values()).forEach((type) -> registerVanillaDoor(type.getDoor(), key -> new LockedDoorBlock((DoorBlock) type.getDoor(), Block.Properties.of().setId(key).mapColor(type.getDoor().defaultMapColor()).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(3.0F).sound(SoundType.WOOD).noOcclusion())));

        // Copper doors are eight blocks - four oxidation stages, waxed and unwaxed - and each keeps
        // its own locked stand-in so the padlock round trips back to exactly the door it was put on.
        // The four unwaxed ones go on oxidising while locked, carrying the lock with them; the waxed
        // four never change, so they are plain locked doors. Properties are vanilla's own copper
        // door, which is the same for every stage but the map colour.
        WeatheringCopperCollection.zipApply(WeatheringCopperCollection.STATES, Blocks.COPPER_DOOR.weathering(), (age, door) ->
                registerVanillaDoor(door, key -> new LockedCopperDoorBlock((DoorBlock) door, age, copperDoorProps(door, key, age != WeatherState.OXIDIZED))));
        Blocks.COPPER_DOOR.waxed().forEach(door ->
                registerVanillaDoor(door, key -> new LockedDoorBlock((DoorBlock) door, copperDoorProps(door, key, false))));
    }

    /** The vanilla stand-ins plus the four Assorted Building Blocks doors, which are registered whether it is loaded or not. */
    public static Block[] lockedDoors() {
        List<Block> doors = new ArrayList<>();
        VANILLA_DOORS.values().forEach(door -> doors.add(door.get()));
        doors.add(LOCKED_QUARTZ_DOOR.get());
        doors.add(LOCKED_GLASS_DOOR.get());
        doors.add(LOCKED_STEEL_DOOR.get());
        doors.add(LOCKED_CHAIN_LINK_DOOR.get());
        return doors.toArray(new Block[0]);
    }

    /**
     * A locked door standing in for a vanilla one, named {@code locked_<the vanilla door's path>} and
     * recorded in {@link #VANILLA_DOORS}. Doors have no item: the only way to get one is a padlock.
     */
    private static IRegistryObject<LockedDoorBlock> registerVanillaDoor(Block parent, Function<ResourceKey<Block>, ? extends LockedDoorBlock> factory) {
        String name = "locked_" + BuiltInRegistries.BLOCK.getKey(parent).getPath();
        IRegistryObject<LockedDoorBlock> door = registerNoItem(name, factory);
        VANILLA_DOORS.put(parent, door);
        return door;
    }

    /**
     * Vanilla's copper door properties. {@code randomTicks} is what actually drives oxidising, and it is baked into
     * the block state cache, so it cannot be worked out from the loaders' oxidation registries, which load later.
     */
    private static BlockBehaviour.Properties copperDoorProps(Block parent, ResourceKey<Block> key, boolean oxidises) {
        BlockBehaviour.Properties props = Block.Properties.of().setId(key).mapColor(parent.defaultMapColor()).requiresCorrectToolForDrops().strength(3.0F).sound(SoundType.COPPER).noOcclusion();
        return oxidises ? props.randomTicks() : props;
    }

    /** The locked vanilla chests and barrels, which burn and break like the wooden ones they stand in for. */
    private static BlockBehaviour.Properties woodStorage(ResourceKey<Block> key) {
        return BlockBehaviour.Properties.of().setId(key).mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD);
    }

    /** A locked container, whose item shows the lock it carries. */
    private static <T extends Block> IRegistryObject<T> registerStorage(String name, Function<ResourceKey<Block>, ? extends T> factory, BiFunction<Block, Item.Properties, BlockItem> item) {
        IRegistryObject<T> ret = registerNoItem(name, factory);
        final ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        ITEMS.register(name, () -> item.apply(ret.get(), new Item.Properties().useBlockDescriptionPrefix().setId(itemKey)));
        return ret;
    }

    private static BlockItem storageItem(Block block, Item.Properties props) {
        return new StorageBlockItem(block, props, LocksDataComponents.STORAGE_INFO);
    }

    private static BlockItem shulkerItem(Block block, Item.Properties props) {
        return new ShulkerBoxBlockItem(block, props, LocksDataComponents.STORAGE_INFO);
    }

    /** The locked containers, in the order they show in the creative tab and the manual. */
    public static Block[] lockedContainers() {
        return new Block[]{LOCKED_ENDER_CHEST.get(), LOCKED_CHEST.get(), LOCKED_SHULKER_BOX.get(), LOCKED_BARREL.get(), LOCKED_HOPPER.get()};
    }

    private static <T extends Block> IRegistryObject<T> register(String name, Function<ResourceKey<Block>, ? extends T> factory) {
        IRegistryObject<T> ret = registerNoItem(name, factory);
        final ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties().useBlockDescriptionPrefix().setId(itemKey)));
        return ret;
    }

    // Since 1.21.2 a block has to know its own registry id before it is constructed, so the
    // registration name is turned into a ResourceKey here and handed to the factory to put on the
    // properties. The same is true of items.
    private static <T extends Block> IRegistryObject<T> registerNoItem(String name, Function<ResourceKey<Block>, ? extends T> factory) {
        final ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        return BLOCKS.register(name, () -> factory.apply(key));
    }

    public static void init() {

    }
}
