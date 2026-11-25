package com.grim3212.assorted.crates.common.block;

import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.crates.Constants;
import com.grim3212.assorted.crates.api.Wood;
import com.grim3212.assorted.crates.api.crates.CrateLayout;
import com.grim3212.assorted.crates.common.item.CratesBlockItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class CratesBlocks {

    public static final RegistryProvider<Block> BLOCKS = RegistryProvider.create(Registries.BLOCK, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);
    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);

    public static final IRegistryObject<CrateCompactingBlock> CRATE_COMPACTING = register("crate_compacting", () -> new CrateCompactingBlock(CrateLayout.TRIPLE, BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F).sound(SoundType.STONE)));
    public static final IRegistryObject<CrateControllerBlock> CRATE_CONTROLLER = register("crate_controller", () -> new CrateControllerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F).sound(SoundType.STONE)));
    public static final IRegistryObject<CrateBridgeBlock> CRATE_BRIDGE = register("crate_bridge", () -> new CrateBridgeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F).sound(SoundType.STONE)));

    public static final List<CrateGroup> CRATES = new ArrayList<>();

    static {
        Stream.of(Wood.values()).forEach((type) -> CRATES.add(new CrateGroup(type)));
    }

    private static <T extends Block> IRegistryObject<T> register(String name, Supplier<? extends T> factory) {
        IRegistryObject<T> ret = BLOCKS.register(name, factory);
        ITEMS.register(name, () -> new CratesBlockItem(ret.get(), new Item.Properties()));
        return ret;
    }

    public static final class CrateGroup {
        public final IRegistryObject<CrateBlock> SINGLE;
        public final IRegistryObject<CrateBlock> DOUBLE;
        public final IRegistryObject<CrateBlock> TRIPLE;
        public final IRegistryObject<CrateBlock> QUADRUPLE;
        private final Wood type;

        public CrateGroup(Wood type) {
            this.type = type;

            this.SINGLE = register(type.toString() + "_crate", () -> new CrateBlock(type, CrateLayout.SINGLE, crateProps(type)));
            this.DOUBLE = register(type.toString() + "_crate_double", () -> new CrateBlock(type, CrateLayout.DOUBLE, crateProps(type)));
            this.TRIPLE = register(type.toString() + "_crate_triple", () -> new CrateBlock(type, CrateLayout.TRIPLE, crateProps(type)));
            this.QUADRUPLE = register(type.toString() + "_crate_quadruple", () -> new CrateBlock(type, CrateLayout.QUADRUPLE, crateProps(type)));
        }

        @SuppressWarnings("deprecation") // NeoForge deprecates BlockState#getSoundType() for a level aware overload common cannot call; there is no level before registration anyway
        private static BlockBehaviour.Properties crateProps(Wood type) {
            BlockState woodState = type.getLog().defaultBlockState();
            MapColor color = woodState.getBlock().defaultMapColor();
            SoundType sound = woodState.getSoundType();
            return BlockBehaviour.Properties.of().mapColor(color).instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(sound);
        }

        public Wood getType() {
            return type;
        }
    }

    public static void init() {

    }
}
