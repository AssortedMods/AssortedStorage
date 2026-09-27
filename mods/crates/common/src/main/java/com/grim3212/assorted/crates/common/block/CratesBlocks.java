package com.grim3212.assorted.crates.common.block;

import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.crates.Constants;
import com.grim3212.assorted.crates.Family;
import com.grim3212.assorted.crates.api.Wood;
import com.grim3212.assorted.crates.api.crates.CrateLayout;
import com.grim3212.assorted.crates.common.item.CratesBlockItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class CratesBlocks {

    public static final RegistryProvider<Block> BLOCKS = RegistryProvider.create(Registries.BLOCK, Constants.MOD_ID).aliasFrom(Family.ID);
    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Family.ID);

    public static final IRegistryObject<CrateCompactingBlock> CRATE_COMPACTING = register("crate_compacting", key -> new CrateCompactingBlock(CrateLayout.TRIPLE, BlockBehaviour.Properties.of().setId(key).mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F).sound(SoundType.STONE)));
    public static final IRegistryObject<CrateControllerBlock> CRATE_CONTROLLER = register("crate_controller", key -> new CrateControllerBlock(BlockBehaviour.Properties.of().setId(key).mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F).sound(SoundType.STONE)));
    public static final IRegistryObject<CrateBridgeBlock> CRATE_BRIDGE = register("crate_bridge", key -> new CrateBridgeBlock(BlockBehaviour.Properties.of().setId(key).mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F).sound(SoundType.STONE)));

    public static final List<CrateGroup> CRATES = new ArrayList<>();

    static {
        Stream.of(Wood.values()).forEach((type) -> CRATES.add(new CrateGroup(type)));
    }

    // Item properties carry a registry id since 1.21.2, so they have to be built per registration.
    private static Item.Properties itemProperties(ResourceKey<Item> key) {
        return new Item.Properties().useBlockDescriptionPrefix().setId(key);
    }

    private static <T extends Block> IRegistryObject<T> register(String name, Function<ResourceKey<Block>, ? extends T> factory) {
        IRegistryObject<T> ret = registerNoItem(name, factory);
        final ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        ITEMS.register(name, () -> new CratesBlockItem(ret.get(), itemProperties(itemKey)));
        return ret;
    }

    // Since 1.21.2 a block has to know its own registry id before it is constructed, so the
    // registration name is turned into a ResourceKey here and handed to the factory to put on the
    // properties. The same is true of items.
    private static <T extends Block> IRegistryObject<T> registerNoItem(String name, Function<ResourceKey<Block>, ? extends T> factory) {
        final ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        return BLOCKS.register(name, () -> factory.apply(key));
    }

    public static final class CrateGroup {
        public final IRegistryObject<CrateBlock> SINGLE;
        public final IRegistryObject<CrateBlock> DOUBLE;
        public final IRegistryObject<CrateBlock> TRIPLE;
        public final IRegistryObject<CrateBlock> QUADRUPLE;
        private final Wood type;

        public CrateGroup(Wood type) {
            this.type = type;

            this.SINGLE = register(type.toString() + "_crate", key -> new CrateBlock(type, CrateLayout.SINGLE, crateProps(type, key)));
            this.DOUBLE = register(type.toString() + "_crate_double", key -> new CrateBlock(type, CrateLayout.DOUBLE, crateProps(type, key)));
            this.TRIPLE = register(type.toString() + "_crate_triple", key -> new CrateBlock(type, CrateLayout.TRIPLE, crateProps(type, key)));
            this.QUADRUPLE = register(type.toString() + "_crate_quadruple", key -> new CrateBlock(type, CrateLayout.QUADRUPLE, crateProps(type, key)));
        }

        private static BlockBehaviour.Properties crateProps(Wood type, ResourceKey<Block> key) {
            BlockState woodState = type.getLog().defaultBlockState();
            MapColor color = woodState.getBlock().defaultMapColor();
            SoundType sound = woodState.getSoundType();
            return BlockBehaviour.Properties.of().setId(key).mapColor(color).instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(sound);
        }

        public Wood getType() {
            return type;
        }
    }

    public static void init() {

    }
}
