package com.grim3212.assorted.crates.client.data;

import com.grim3212.assorted.lib.client.data.LockedModelBuilder;
import com.grim3212.assorted.crates.Constants;
import com.grim3212.assorted.crates.api.crates.CrateLayout;
import com.grim3212.assorted.crates.client.model.baked.LockedModel;
import com.grim3212.assorted.crates.common.block.CrateBlock;
import com.grim3212.assorted.crates.common.block.CrateCompactingBlock;
import com.grim3212.assorted.crates.common.block.CrateControllerBlock;
import com.grim3212.assorted.crates.common.block.CratesBlocks;
import com.grim3212.assorted.crates.common.block.CratesBlocks.CrateGroup;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelBuilder.FaceRotation;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * Block states, block models and the block items' models. This owns every block and block item, and
 * {@link CratesItemModelProvider} owns the rest, so the two never write the same file.
 */
public class CratesBlockstateProvider extends BlockStateProvider {

    private static final String FACING = "#facing";
    private static final String SIDES = "#sides";
    private static final String EDGES = "#edges";
    private static final String TOP = "#top";
    private static final String FACING_COLUMNS = "#facing_columns";

    public CratesBlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Constants.MOD_ID, exFileHelper);
    }

    @Override
    public String getName() {
        return "Assorted Crates block states";
    }

    @Override
    protected void registerStatesAndModels() {
        baseCrateModels();

        for (CrateGroup group : CratesBlocks.CRATES) {
            storageCrate(group.SINGLE.get());
            storageCrate(group.DOUBLE.get());
            storageCrate(group.TRIPLE.get());
            storageCrate(group.QUADRUPLE.get());
        }

        compactingStorageCrate(CratesBlocks.CRATE_COMPACTING.get());
        crateController();

        Block bridge = CratesBlocks.CRATE_BRIDGE.get();
        ModelFile bridgeModel = models().cubeAll(name(bridge), resource("block/crates/crate_bridge"));
        simpleBlock(bridge, bridgeModel);
        simpleBlockItem(bridge, bridgeModel);
    }

    // ------------------------------------------------------------------ crates

    private void storageCrate(CrateBlock b) {
        String name = b.getWoodType() != null ? b.getWoodType().toString() : name(b);
        ResourceLocation sides = b.getWoodType() != null ? ResourceLocation.withDefaultNamespace("block/" + b.getWoodType().getLogTextureName()) : resource("block/crates/" + name + "_sides");

        crateState(b, b.getLayout(), resource("block/crates/" + name + "_facing"), sides, sides);
    }

    private void compactingStorageCrate(CrateCompactingBlock b) {
        crateState(b, b.getLayout(), resource("block/crates/" + name(b) + "_facing"), resource("block/crates/crate_bridge"), resource("block/crates/crate_compacting_columns"));
    }

    /**
     * A crate's block state: rotated to its facing, with separate models for the two vertical
     * faces. {@code WATERLOGGED} is not dispatched on. {@code facing_columns} names its texture
     * directly, because a texture entry cannot refer to another slot.
     */
    private void crateState(Block b, CrateLayout layout, ResourceLocation facing, ResourceLocation sides, ResourceLocation facingColumns) {
        ModelFile horizontal = crateChild(name(b), layout, false, facing, sides, facingColumns);
        ModelFile vertical = crateChild(name(b) + "_vertical", layout, true, facing, sides, facingColumns);

        getVariantBuilder(b).forAllStatesExcept(state -> switch (state.getValue(CrateBlock.FACING)) {
            case DOWN -> ConfiguredModel.builder().modelFile(vertical).rotationX(180).build();
            case UP -> ConfiguredModel.builder().modelFile(vertical).build();
            case NORTH -> ConfiguredModel.builder().modelFile(horizontal).build();
            case SOUTH -> ConfiguredModel.builder().modelFile(horizontal).rotationY(180).build();
            case WEST -> ConfiguredModel.builder().modelFile(horizontal).rotationY(270).build();
            case EAST -> ConfiguredModel.builder().modelFile(horizontal).rotationY(90).build();
        }, CrateBlock.WATERLOGGED);

        simpleBlockItem(b, horizontal);
    }

    private ModelFile crateChild(String name, CrateLayout layout, boolean vertical, ResourceLocation facing, ResourceLocation sides, ResourceLocation facingColumns) {
        BlockModelBuilder builder = models().withExistingParent(name, resource("block/base_crate_" + layout.getName() + (vertical ? "_vertical" : "")))
                .texture("facing", facing)
                .texture("sides", sides);
        if (layout != CrateLayout.SINGLE) {
            builder.texture("facing_columns", facingColumns);
        }
        return builder;
    }

    private void crateController() {
        CrateControllerBlock b = CratesBlocks.CRATE_CONTROLLER.get();
        ResourceLocation top = resource("block/crates/crate_top");
        ResourceLocation side = resource("block/crates/crate_controller_side");
        ResourceLocation front = resource("block/crates/crate_controller_front");

        ModelFile unlocked = models().orientable(name(b) + "_unlocked", side, front, top);
        ModelFile locked = models().orientable(name(b) + "_locked", side, resource("block/crates/crate_controller_front_locked"), top);

        // A block item reads its transforms from the block model's parent chain; block/block supplies
        // the standard display block and gui_light, and no geometry. The loader replaces the geometry
        // outright, so the model declares no elements.
        ModelFile model = models().withExistingParent(name(b), ResourceLocation.withDefaultNamespace("block/block"))
                .texture("particle", front)
                .customLoader(LockedModelBuilder.<BlockModelBuilder>begin(LockedModel.LOADER_NAME))
                .unlockedModel(unlocked.getLocation()).lockedModel(locked.getLocation())
                .end();

        // x = up ? 270 : horizontal ? 0 : 90, y = vertical ? 180 : (facing.toYRot() + 180) % 360.
        getVariantBuilder(b).forAllStates(state -> {
            Direction dir = state.getValue(CrateControllerBlock.FACING);
            return switch (dir) {
                case DOWN -> ConfiguredModel.builder().modelFile(model).rotationX(90).rotationY(180).build();
                case UP -> ConfiguredModel.builder().modelFile(model).rotationX(270).rotationY(180).build();
                case NORTH -> ConfiguredModel.builder().modelFile(model).build();
                case SOUTH -> ConfiguredModel.builder().modelFile(model).rotationY(180).build();
                case WEST -> ConfiguredModel.builder().modelFile(model).rotationY(270).build();
                case EAST -> ConfiguredModel.builder().modelFile(model).rotationY(90).build();
            };
        });

        // The item shows the unlocked face rather than the loader model, as it always has.
        simpleBlockItem(b, unlocked);
    }

    // ------------------------------------------------------------------ base crate geometry

    /**
     * The eight parent models the crates share. Each layout adds a column to the one before it, so
     * the horizontal and vertical sets are built cumulatively.
     */
    private void baseCrateModels() {
        for (CrateLayout layout : CrateLayout.values()) {
            BlockModelBuilder flat = baseCrate("block/base_crate_" + layout.getName());
            BlockModelBuilder upright = baseCrateVertical("block/base_crate_" + layout.getName() + "_vertical");

            if (layout != CrateLayout.SINGLE) {
                crateColumnWide(flat);
                crateColumnWideVertical(upright);
            }
            if (layout == CrateLayout.TRIPLE || layout == CrateLayout.QUADRUPLE) {
                crateColumnNarrow(flat, 2, 7);
                crateColumnNarrowVertical(upright, 9, 14);
            }
            if (layout == CrateLayout.QUADRUPLE) {
                crateColumnNarrow(flat, 9, 14);
                crateColumnNarrowVertical(upright, 2, 7);
            }
        }
    }

    private BlockModelBuilder baseModel(String name) {
        return models().withExistingParent(name, ResourceLocation.withDefaultNamespace("block/block"))
                .texture("particle", resource("block/crates/crate_top"))
                .texture("top", resource("block/crates/crate_top"))
                .texture("edges", resource("block/crates/crate_edges"));
    }

    private static void face(ModelBuilder<BlockModelBuilder>.ElementBuilder e, Direction dir, float u1, float v1, float u2, float v2, String texture, Direction cull, FaceRotation rotation) {
        ModelBuilder<BlockModelBuilder>.ElementBuilder.FaceBuilder face = e.face(dir).uvs(u1, v1, u2, v2).texture(texture);
        if (rotation != FaceRotation.ZERO) {
            face.rotation(rotation);
        }
        if (cull != null) {
            face.cullface(cull);
        }
    }

    private static void face(ModelBuilder<BlockModelBuilder>.ElementBuilder e, Direction dir, float u1, float v1, float u2, float v2, String texture, Direction cull) {
        face(e, dir, u1, v1, u2, v2, texture, cull, FaceRotation.ZERO);
    }

    private static void face(ModelBuilder<BlockModelBuilder>.ElementBuilder e, Direction dir, float u1, float v1, float u2, float v2, String texture) {
        face(e, dir, u1, v1, u2, v2, texture, null, FaceRotation.ZERO);
    }

    private static void face(ModelBuilder<BlockModelBuilder>.ElementBuilder e, Direction dir, float u1, float v1, float u2, float v2, FaceRotation rotation, String texture, Direction cull) {
        face(e, dir, u1, v1, u2, v2, texture, cull, rotation);
    }

    private static void face(ModelBuilder<BlockModelBuilder>.ElementBuilder e, Direction dir, float u1, float v1, float u2, float v2, FaceRotation rotation, String texture) {
        face(e, dir, u1, v1, u2, v2, texture, null, rotation);
    }

    private BlockModelBuilder baseCrate(String name) {
        BlockModelBuilder builder = baseModel(name);

        var e = builder.element().from(1, 1, 1).to(15, 15, 15);
        face(e, Direction.NORTH, 1, 1, 15, 15, FACING);
        face(e, Direction.EAST, 1, 1, 15, 15, SIDES);
        face(e, Direction.SOUTH, 1, 1, 15, 15, SIDES);
        face(e, Direction.WEST, 1, 1, 15, 15, SIDES);

        e = builder.element().from(0, 0, 0).to(16, 2, 16);
        face(e, Direction.NORTH, 0, 0, 16, 2, EDGES, Direction.NORTH);
        face(e, Direction.EAST, 0, 0, 16, 2, EDGES, Direction.EAST);
        face(e, Direction.SOUTH, 0, 0, 16, 2, EDGES, Direction.SOUTH);
        face(e, Direction.WEST, 0, 0, 16, 2, EDGES, Direction.WEST);
        face(e, Direction.UP, 0, 0, 16, 2, TOP);
        face(e, Direction.DOWN, 0, 0, 16, 2, TOP, Direction.DOWN);

        e = builder.element().from(0, 14, 0).to(16, 16, 16);
        face(e, Direction.NORTH, 0, 2, 16, 0, EDGES, Direction.NORTH);
        face(e, Direction.EAST, 0, 2, 16, 0, EDGES, Direction.EAST);
        face(e, Direction.SOUTH, 0, 2, 16, 0, EDGES, Direction.SOUTH);
        face(e, Direction.WEST, 0, 2, 16, 0, EDGES, Direction.WEST);
        face(e, Direction.UP, 16, 16, 0, 0, TOP, Direction.UP);
        face(e, Direction.DOWN, 16, 16, 0, 0, TOP);

        e = builder.element().from(0, 2, 14).to(2, 14, 16);
        face(e, Direction.NORTH, 10, 2, 8, 14, SIDES);
        face(e, Direction.EAST, 12, 2, 10, 14, SIDES);
        face(e, Direction.SOUTH, 14, 2, 12, 14, SIDES, Direction.SOUTH);
        face(e, Direction.WEST, 8, 2, 6, 14, SIDES, Direction.WEST);

        e = builder.element().from(0, 2, 0).to(2, 14, 2);
        face(e, Direction.NORTH, 4, 2, 6, 14, SIDES, Direction.NORTH);
        face(e, Direction.EAST, 2, 2, 4, 14, SIDES);
        face(e, Direction.SOUTH, 8, 2, 10, 14, SIDES);
        face(e, Direction.WEST, 6, 2, 8, 14, SIDES, Direction.WEST);

        e = builder.element().from(14, 2, 14).to(16, 14, 16);
        face(e, Direction.NORTH, 8, 2, 10, 14, SIDES);
        face(e, Direction.EAST, 6, 2, 8, 14, SIDES, Direction.EAST);
        face(e, Direction.SOUTH, 12, 2, 14, 14, SIDES, Direction.SOUTH);
        face(e, Direction.WEST, 10, 2, 12, 14, SIDES);

        e = builder.element().from(14, 2, 0).to(16, 14, 2);
        face(e, Direction.NORTH, 6, 2, 4, 14, SIDES, Direction.NORTH);
        face(e, Direction.EAST, 8, 2, 6, 14, SIDES, Direction.EAST);
        face(e, Direction.SOUTH, 10, 2, 8, 14, SIDES);
        face(e, Direction.WEST, 4, 2, 2, 14, SIDES);

        return builder;
    }

    private BlockModelBuilder baseCrateVertical(String name) {
        BlockModelBuilder builder = baseModel(name);

        var e = builder.element().from(1, 1, 1).to(15, 15, 15);
        face(e, Direction.EAST, 1, 1, 15, 15, FaceRotation.COUNTERCLOCKWISE_90, SIDES);
        face(e, Direction.WEST, 1, 1, 15, 15, FaceRotation.CLOCKWISE_90, SIDES);
        face(e, Direction.UP, 1, 1, 15, 15, FaceRotation.UPSIDE_DOWN, FACING);
        face(e, Direction.DOWN, 1, 1, 15, 15, SIDES);

        e = builder.element().from(0, 0, 0).to(16, 16, 2);
        face(e, Direction.NORTH, 16, 0, 0, 16, FaceRotation.UPSIDE_DOWN, TOP, Direction.NORTH);
        face(e, Direction.EAST, 0, 0, 16, 2, FaceRotation.COUNTERCLOCKWISE_90, EDGES, Direction.EAST);
        face(e, Direction.SOUTH, 16, 16, 0, 0, TOP);
        face(e, Direction.WEST, 0, 0, 16, 2, FaceRotation.CLOCKWISE_90, EDGES, Direction.WEST);
        face(e, Direction.UP, 0, 0, 16, 2, FaceRotation.UPSIDE_DOWN, EDGES, Direction.UP);
        face(e, Direction.DOWN, 0, 0, 16, 2, EDGES, Direction.DOWN);

        e = builder.element().from(0, 0, 14).to(16, 16, 16);
        face(e, Direction.NORTH, 16, 16, 0, 0, FaceRotation.UPSIDE_DOWN, TOP);
        face(e, Direction.EAST, 0, 2, 16, 0, FaceRotation.COUNTERCLOCKWISE_90, EDGES, Direction.EAST);
        face(e, Direction.SOUTH, 16, 16, 0, 0, TOP, Direction.SOUTH);
        face(e, Direction.WEST, 0, 2, 16, 0, FaceRotation.CLOCKWISE_90, EDGES, Direction.WEST);
        face(e, Direction.UP, 0, 2, 16, 0, FaceRotation.UPSIDE_DOWN, EDGES, Direction.UP);
        face(e, Direction.DOWN, 0, 2, 16, 0, EDGES, Direction.DOWN);

        e = builder.element().from(0, 0, 2).to(2, 2, 14);
        face(e, Direction.EAST, 12, 2, 10, 14, FaceRotation.COUNTERCLOCKWISE_90, SIDES);
        face(e, Direction.WEST, 8, 2, 6, 14, FaceRotation.CLOCKWISE_90, SIDES, Direction.WEST);
        face(e, Direction.UP, 10, 2, 8, 14, FaceRotation.UPSIDE_DOWN, SIDES);
        face(e, Direction.DOWN, 14, 2, 12, 14, SIDES, Direction.DOWN);

        e = builder.element().from(0, 14, 2).to(2, 16, 14);
        face(e, Direction.EAST, 2, 2, 4, 14, FaceRotation.COUNTERCLOCKWISE_90, SIDES);
        face(e, Direction.WEST, 6, 2, 8, 14, FaceRotation.CLOCKWISE_90, SIDES, Direction.WEST);
        face(e, Direction.UP, 4, 2, 6, 14, FaceRotation.UPSIDE_DOWN, SIDES, Direction.UP);
        face(e, Direction.DOWN, 8, 2, 10, 14, SIDES);

        e = builder.element().from(14, 0, 2).to(16, 2, 14);
        face(e, Direction.EAST, 6, 2, 8, 14, FaceRotation.COUNTERCLOCKWISE_90, SIDES, Direction.EAST);
        face(e, Direction.WEST, 10, 2, 12, 14, FaceRotation.CLOCKWISE_90, SIDES);
        face(e, Direction.UP, 8, 2, 10, 14, FaceRotation.UPSIDE_DOWN, SIDES);
        face(e, Direction.DOWN, 12, 2, 14, 14, SIDES, Direction.DOWN);

        e = builder.element().from(14, 14, 2).to(16, 16, 14);
        face(e, Direction.EAST, 8, 2, 6, 14, FaceRotation.COUNTERCLOCKWISE_90, SIDES, Direction.EAST);
        face(e, Direction.WEST, 4, 2, 2, 14, FaceRotation.CLOCKWISE_90, SIDES);
        face(e, Direction.UP, 6, 2, 4, 14, FaceRotation.UPSIDE_DOWN, SIDES, Direction.UP);
        face(e, Direction.DOWN, 10, 2, 8, 14, SIDES);

        return builder;
    }

    private static void crateColumnWide(BlockModelBuilder builder) {
        var e = builder.element().from(2, 7, 0).to(14, 9, 1);
        face(e, Direction.NORTH, 0, 0, 12, 2, FACING_COLUMNS, Direction.NORTH);
        face(e, Direction.UP, 0, 0, 12, 1, FACING_COLUMNS);
        face(e, Direction.DOWN, 0, 0, 12, 1, FACING_COLUMNS);
    }

    private static void crateColumnWideVertical(BlockModelBuilder builder) {
        var e = builder.element().from(2, 15, 7).to(14, 16, 9);
        face(e, Direction.NORTH, 0, 0, 12, 1, FACING_COLUMNS);
        face(e, Direction.SOUTH, 0, 0, 12, 1, FACING_COLUMNS);
        face(e, Direction.UP, 0, 0, 12, 2, FACING_COLUMNS, Direction.UP);
    }

    private static void crateColumnNarrow(BlockModelBuilder builder, int fromY, int toY) {
        var e = builder.element().from(7, fromY, 0).to(9, toY, 1);
        face(e, Direction.NORTH, 4, 0, 2, 5, FACING_COLUMNS, Direction.NORTH);
        face(e, Direction.EAST, 4, 0, 1, 5, FACING_COLUMNS);
        face(e, Direction.WEST, 4, 0, 1, 5, FACING_COLUMNS);
    }

    private static void crateColumnNarrowVertical(BlockModelBuilder builder, int fromZ, int toZ) {
        var e = builder.element().from(7, 15, fromZ).to(9, 16, toZ);
        face(e, Direction.EAST, 4, 0, 5, 1, FACING_COLUMNS);
        face(e, Direction.WEST, 4, 0, 5, 1, FACING_COLUMNS);
        face(e, Direction.UP, 4, 0, 2, 5, FACING_COLUMNS, Direction.UP);
    }

    // ------------------------------------------------------------------ helpers

    private static String name(Block b) {
        return BuiltInRegistries.BLOCK.getKey(b).getPath();
    }

    private static ResourceLocation resource(String path) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path);
    }
}
