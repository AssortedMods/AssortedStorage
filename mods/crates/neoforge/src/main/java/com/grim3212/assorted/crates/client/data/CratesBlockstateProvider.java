package com.grim3212.assorted.crates.client.data;

import com.grim3212.assorted.lib.client.data.SpecificationBlockStateModelBuilder;
import com.grim3212.assorted.crates.Constants;
import com.grim3212.assorted.crates.api.crates.CrateLayout;
import com.grim3212.assorted.crates.common.block.CrateBlock;
import com.grim3212.assorted.crates.common.block.CrateCompactingBlock;
import com.grim3212.assorted.crates.common.block.CrateControllerBlock;
import com.grim3212.assorted.crates.common.block.CratesBlocks;
import com.grim3212.assorted.crates.common.block.CratesBlocks.CrateGroup;
import com.mojang.math.Quadrant;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.template.ElementBuilder;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

/**
 * Block states and block models. This owns every block and block item, and
 * {@link CratesItemModelProvider} owns the rest, so the two never write the same file.
 */
public class CratesBlockstateProvider extends ModelProvider {

    // Slots the crate templates read that vanilla has no constant for. TextureSlot has no equals, so
    // each one has to be created exactly once and shared.
    private static final TextureSlot FACING = TextureSlot.create("facing");
    private static final TextureSlot SIDES = TextureSlot.create("sides");
    private static final TextureSlot EDGES = TextureSlot.create("edges");
    private static final TextureSlot FACING_COLUMNS = TextureSlot.create("facing_columns");

    private static final Identifier VANILLA_BLOCK = Identifier.withDefaultNamespace("block/block");

    public CratesBlockstateProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    public String getName() {
        return "Assorted Crates block states";
    }

    /**
     * Only the block items belong here; every other item is {@link CratesItemModelProvider}'s, so
     * the two providers never write the same file.
     */
    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return super.getKnownItems().filter(holder -> holder.value() instanceof BlockItem);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        baseCrateModels(blockModels.modelOutput);

        for (CrateGroup group : CratesBlocks.CRATES) {
            storageCrate(blockModels, group.SINGLE.get());
            storageCrate(blockModels, group.DOUBLE.get());
            storageCrate(blockModels, group.TRIPLE.get());
            storageCrate(blockModels, group.QUADRUPLE.get());
        }

        compactingStorageCrate(blockModels, CratesBlocks.CRATE_COMPACTING.get());
        crateController(blockModels);

        Block bridge = CratesBlocks.CRATE_BRIDGE.get();
        MultiVariant bridgeModel = BlockModelGenerators.plainVariant(ModelTemplates.CUBE_ALL.create(bridge, TextureMapping.cube(new Material(resource("block/crates/crate_bridge"))), blockModels.modelOutput));
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(bridge, bridgeModel));
    }

    // ------------------------------------------------------------------ crates

    private void storageCrate(BlockModelGenerators blockModels, CrateBlock b) {
        String name = b.getWoodType() != null ? b.getWoodType().toString() : name(b);
        Material sides = b.getWoodType() != null ? new Material(Identifier.parse("block/" + b.getWoodType().getLogTextureName())) : texture("block/crates/" + name + "_sides");
        TextureMapping textures = crateTextures(texture("block/crates/" + name + "_facing"), sides, sides);

        crateState(blockModels, b, b.getLayout(), textures);
    }

    private void compactingStorageCrate(BlockModelGenerators blockModels, CrateCompactingBlock b) {
        TextureMapping textures = crateTextures(
                texture("block/crates/" + name(b) + "_facing"),
                texture("block/crates/crate_bridge"),
                texture("block/crates/crate_compacting_columns"));

        crateState(blockModels, b, b.getLayout(), textures);
    }

    /**
     * The crate texture mapping. {@code facing_columns} names its texture directly, because a
     * texture entry cannot refer to another slot.
     */
    private static TextureMapping crateTextures(Material facing, Material sides, Material facingColumns) {
        return new TextureMapping().put(FACING, facing).put(SIDES, sides).put(FACING_COLUMNS, facingColumns);
    }

    /**
     * A crate's block state: rotated to its facing, with separate models for the two vertical
     * faces. {@code WATERLOGGED} is not dispatched on.
     */
    private void crateState(BlockModelGenerators blockModels, Block b, CrateLayout layout, TextureMapping textures) {
        Identifier horizontal = crateChild(layout, false).create(ModelLocationUtils.getModelLocation(b), textures, blockModels.modelOutput);
        Identifier vertical = crateChild(layout, true).create(ModelLocationUtils.getModelLocation(b, "_vertical"), textures, blockModels.modelOutput);

        MultiVariant flat = BlockModelGenerators.plainVariant(horizontal);
        MultiVariant upright = BlockModelGenerators.plainVariant(vertical);

        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(b)
                .with(PropertyDispatch.initial(CrateBlock.FACING)
                        .select(Direction.DOWN, upright.with(BlockModelGenerators.X_ROT_180))
                        .select(Direction.UP, upright)
                        .select(Direction.NORTH, flat)
                        .select(Direction.SOUTH, flat.with(BlockModelGenerators.Y_ROT_180))
                        .select(Direction.WEST, flat.with(BlockModelGenerators.Y_ROT_270))
                        .select(Direction.EAST, flat.with(BlockModelGenerators.Y_ROT_90))));
    }

    private static final Map<String, ModelTemplate> CRATE_CHILDREN = new HashMap<>();

    private static ModelTemplate crateChild(CrateLayout layout, boolean vertical) {
        return CRATE_CHILDREN.computeIfAbsent(layout.getName() + (vertical ? "_vertical" : ""), key -> {
            ExtendedModelTemplateBuilder builder = ExtendedModelTemplateBuilder.builder()
                    .parent(resource("block/base_crate_" + key))
                    .requiredTextureSlot(FACING)
                    .requiredTextureSlot(SIDES);
            if (layout != CrateLayout.SINGLE) {
                builder.requiredTextureSlot(FACING_COLUMNS);
            }
            return builder.build();
        });
    }

    private void crateController(BlockModelGenerators blockModels) {
        Block b = CratesBlocks.CRATE_CONTROLLER.get();
        Material top = texture("block/crates/crate_top");
        Material side = texture("block/crates/crate_controller_side");

        Identifier unlocked = ModelTemplates.CUBE_ORIENTABLE.createWithSuffix(b, "_unlocked", orientable(side, texture("block/crates/crate_controller_front"), top), blockModels.modelOutput);
        Identifier locked = ModelTemplates.CUBE_ORIENTABLE.createWithSuffix(b, "_locked", orientable(side, texture("block/crates/crate_controller_front_locked"), top), blockModels.modelOutput);

        MultiVariant model = SpecificationBlockStateModelBuilder.specificationVariant(lockedModel(blockModels.modelOutput, ModelLocationUtils.getModelLocation(b), unlocked, locked, texture("block/crates/crate_controller_front")));

        // x = up ? 270 : horizontal ? 0 : 90, y = vertical ? 180 : (facing.toYRot() + 180) % 360.
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(b)
                .with(PropertyDispatch.initial(CrateControllerBlock.FACING)
                        .select(Direction.DOWN, model.with(BlockModelGenerators.X_ROT_90).with(BlockModelGenerators.Y_ROT_180))
                        .select(Direction.UP, model.with(BlockModelGenerators.X_ROT_270).with(BlockModelGenerators.Y_ROT_180))
                        .select(Direction.NORTH, model)
                        .select(Direction.SOUTH, model.with(BlockModelGenerators.Y_ROT_180))
                        .select(Direction.WEST, model.with(BlockModelGenerators.Y_ROT_270))
                        .select(Direction.EAST, model.with(BlockModelGenerators.Y_ROT_90))));

        // The item shows the unlocked face rather than the loader model, as it always has.
        blockModels.itemModelOutput.accept(b.asItem(), ItemModelUtils.plainModel(unlocked));
    }

    private static TextureMapping orientable(Material side, Material front, Material top) {
        return new TextureMapping().put(TextureSlot.SIDE, side).put(TextureSlot.FRONT, front).put(TextureSlot.TOP, top);
    }

    // ------------------------------------------------------------------ base crate geometry

    /**
     * The eight parent models the crates share. Each layout adds a column to the one before it, so
     * the horizontal and vertical sets are built cumulatively.
     */
    private void baseCrateModels(BiConsumer<Identifier, ModelInstance> output) {
        for (CrateLayout layout : CrateLayout.values()) {
            ExtendedModelTemplateBuilder flat = baseCrateBuilder();
            ExtendedModelTemplateBuilder upright = baseCrateVerticalBuilder();

            if (layout != CrateLayout.SINGLE) {
                crateColumnWide(flat);
                crateColumnWideVertical(upright);
            }
            if (layout == CrateLayout.TRIPLE || layout == CrateLayout.QUADRUPLE) {
                flat.element(e -> crateColumnNarrow(e, 2, 7));
                upright.element(e -> crateColumnNarrowVertical(e, 9, 14));
            }
            if (layout == CrateLayout.QUADRUPLE) {
                flat.element(e -> crateColumnNarrow(e, 9, 14));
                upright.element(e -> crateColumnNarrowVertical(e, 2, 7));
            }

            TextureMapping textures = new TextureMapping()
                    .put(TextureSlot.PARTICLE, texture("block/crates/crate_top"))
                    .put(TextureSlot.TOP, texture("block/crates/crate_top"))
                    .put(EDGES, texture("block/crates/crate_edges"));

            flat.build().create(resource("block/base_crate_" + layout.getName()), textures, output);
            upright.build().create(resource("block/base_crate_" + layout.getName() + "_vertical"), textures, output);
        }
    }

    private static ExtendedModelTemplateBuilder baseCrateBuilder() {
        return ExtendedModelTemplateBuilder.builder()
                .parent(VANILLA_BLOCK)
                .requiredTextureSlot(TextureSlot.PARTICLE)
                .requiredTextureSlot(TextureSlot.TOP)
                .requiredTextureSlot(EDGES)
                .element(e -> e.from(1, 1, 1).to(15, 15, 15)
                        .face(Direction.NORTH, f -> f.uvs(1, 1, 15, 15).texture(FACING))
                        .face(Direction.EAST, f -> f.uvs(1, 1, 15, 15).texture(SIDES))
                        .face(Direction.SOUTH, f -> f.uvs(1, 1, 15, 15).texture(SIDES))
                        .face(Direction.WEST, f -> f.uvs(1, 1, 15, 15).texture(SIDES)))
                .element(e -> e.from(0, 0, 0).to(16, 2, 16)
                        .face(Direction.NORTH, f -> f.uvs(0, 0, 16, 2).texture(EDGES).cullface(Direction.NORTH))
                        .face(Direction.EAST, f -> f.uvs(0, 0, 16, 2).texture(EDGES).cullface(Direction.EAST))
                        .face(Direction.SOUTH, f -> f.uvs(0, 0, 16, 2).texture(EDGES).cullface(Direction.SOUTH))
                        .face(Direction.WEST, f -> f.uvs(0, 0, 16, 2).texture(EDGES).cullface(Direction.WEST))
                        .face(Direction.UP, f -> f.uvs(0, 0, 16, 2).texture(TextureSlot.TOP))
                        .face(Direction.DOWN, f -> f.uvs(0, 0, 16, 2).texture(TextureSlot.TOP).cullface(Direction.DOWN)))
                .element(e -> e.from(0, 14, 0).to(16, 16, 16)
                        .face(Direction.NORTH, f -> f.uvs(0, 2, 16, 0).texture(EDGES).cullface(Direction.NORTH))
                        .face(Direction.EAST, f -> f.uvs(0, 2, 16, 0).texture(EDGES).cullface(Direction.EAST))
                        .face(Direction.SOUTH, f -> f.uvs(0, 2, 16, 0).texture(EDGES).cullface(Direction.SOUTH))
                        .face(Direction.WEST, f -> f.uvs(0, 2, 16, 0).texture(EDGES).cullface(Direction.WEST))
                        .face(Direction.UP, f -> f.uvs(16, 16, 0, 0).texture(TextureSlot.TOP).cullface(Direction.UP))
                        .face(Direction.DOWN, f -> f.uvs(16, 16, 0, 0).texture(TextureSlot.TOP)))
                .element(e -> e.from(0, 2, 14).to(2, 14, 16)
                        .face(Direction.NORTH, f -> f.uvs(10, 2, 8, 14).texture(SIDES))
                        .face(Direction.EAST, f -> f.uvs(12, 2, 10, 14).texture(SIDES))
                        .face(Direction.SOUTH, f -> f.uvs(14, 2, 12, 14).texture(SIDES).cullface(Direction.SOUTH))
                        .face(Direction.WEST, f -> f.uvs(8, 2, 6, 14).texture(SIDES).cullface(Direction.WEST)))
                .element(e -> e.from(0, 2, 0).to(2, 14, 2)
                        .face(Direction.NORTH, f -> f.uvs(4, 2, 6, 14).texture(SIDES).cullface(Direction.NORTH))
                        .face(Direction.EAST, f -> f.uvs(2, 2, 4, 14).texture(SIDES))
                        .face(Direction.SOUTH, f -> f.uvs(8, 2, 10, 14).texture(SIDES))
                        .face(Direction.WEST, f -> f.uvs(6, 2, 8, 14).texture(SIDES).cullface(Direction.WEST)))
                .element(e -> e.from(14, 2, 14).to(16, 14, 16)
                        .face(Direction.NORTH, f -> f.uvs(8, 2, 10, 14).texture(SIDES))
                        .face(Direction.EAST, f -> f.uvs(6, 2, 8, 14).texture(SIDES).cullface(Direction.EAST))
                        .face(Direction.SOUTH, f -> f.uvs(12, 2, 14, 14).texture(SIDES).cullface(Direction.SOUTH))
                        .face(Direction.WEST, f -> f.uvs(10, 2, 12, 14).texture(SIDES)))
                .element(e -> e.from(14, 2, 0).to(16, 14, 2)
                        .face(Direction.NORTH, f -> f.uvs(6, 2, 4, 14).texture(SIDES).cullface(Direction.NORTH))
                        .face(Direction.EAST, f -> f.uvs(8, 2, 6, 14).texture(SIDES).cullface(Direction.EAST))
                        .face(Direction.SOUTH, f -> f.uvs(10, 2, 8, 14).texture(SIDES))
                        .face(Direction.WEST, f -> f.uvs(4, 2, 2, 14).texture(SIDES)));
    }

    private static ExtendedModelTemplateBuilder baseCrateVerticalBuilder() {
        return ExtendedModelTemplateBuilder.builder()
                .parent(VANILLA_BLOCK)
                .requiredTextureSlot(TextureSlot.PARTICLE)
                .requiredTextureSlot(TextureSlot.TOP)
                .requiredTextureSlot(EDGES)
                .element(e -> e.from(1, 1, 1).to(15, 15, 15)
                        .face(Direction.EAST, f -> f.uvs(1, 1, 15, 15).rotation(Quadrant.R270).texture(SIDES))
                        .face(Direction.WEST, f -> f.uvs(1, 1, 15, 15).rotation(Quadrant.R90).texture(SIDES))
                        .face(Direction.UP, f -> f.uvs(1, 1, 15, 15).rotation(Quadrant.R180).texture(FACING))
                        .face(Direction.DOWN, f -> f.uvs(1, 1, 15, 15).texture(SIDES)))
                .element(e -> e.from(0, 0, 0).to(16, 16, 2)
                        .face(Direction.NORTH, f -> f.uvs(16, 0, 0, 16).rotation(Quadrant.R180).texture(TextureSlot.TOP).cullface(Direction.NORTH))
                        .face(Direction.EAST, f -> f.uvs(0, 0, 16, 2).rotation(Quadrant.R270).texture(EDGES).cullface(Direction.EAST))
                        .face(Direction.SOUTH, f -> f.uvs(16, 16, 0, 0).texture(TextureSlot.TOP))
                        .face(Direction.WEST, f -> f.uvs(0, 0, 16, 2).rotation(Quadrant.R90).texture(EDGES).cullface(Direction.WEST))
                        .face(Direction.UP, f -> f.uvs(0, 0, 16, 2).rotation(Quadrant.R180).texture(EDGES).cullface(Direction.UP))
                        .face(Direction.DOWN, f -> f.uvs(0, 0, 16, 2).texture(EDGES).cullface(Direction.DOWN)))
                .element(e -> e.from(0, 0, 14).to(16, 16, 16)
                        .face(Direction.NORTH, f -> f.uvs(16, 16, 0, 0).rotation(Quadrant.R180).texture(TextureSlot.TOP))
                        .face(Direction.EAST, f -> f.uvs(0, 2, 16, 0).rotation(Quadrant.R270).texture(EDGES).cullface(Direction.EAST))
                        .face(Direction.SOUTH, f -> f.uvs(16, 16, 0, 0).texture(TextureSlot.TOP).cullface(Direction.SOUTH))
                        .face(Direction.WEST, f -> f.uvs(0, 2, 16, 0).rotation(Quadrant.R90).texture(EDGES).cullface(Direction.WEST))
                        .face(Direction.UP, f -> f.uvs(0, 2, 16, 0).rotation(Quadrant.R180).texture(EDGES).cullface(Direction.UP))
                        .face(Direction.DOWN, f -> f.uvs(0, 2, 16, 0).texture(EDGES).cullface(Direction.DOWN)))
                .element(e -> e.from(0, 0, 2).to(2, 2, 14)
                        .face(Direction.EAST, f -> f.uvs(12, 2, 10, 14).rotation(Quadrant.R270).texture(SIDES))
                        .face(Direction.WEST, f -> f.uvs(8, 2, 6, 14).rotation(Quadrant.R90).texture(SIDES).cullface(Direction.WEST))
                        .face(Direction.UP, f -> f.uvs(10, 2, 8, 14).rotation(Quadrant.R180).texture(SIDES))
                        .face(Direction.DOWN, f -> f.uvs(14, 2, 12, 14).texture(SIDES).cullface(Direction.DOWN)))
                .element(e -> e.from(0, 14, 2).to(2, 16, 14)
                        .face(Direction.EAST, f -> f.uvs(2, 2, 4, 14).rotation(Quadrant.R270).texture(SIDES))
                        .face(Direction.WEST, f -> f.uvs(6, 2, 8, 14).rotation(Quadrant.R90).texture(SIDES).cullface(Direction.WEST))
                        .face(Direction.UP, f -> f.uvs(4, 2, 6, 14).rotation(Quadrant.R180).texture(SIDES).cullface(Direction.UP))
                        .face(Direction.DOWN, f -> f.uvs(8, 2, 10, 14).texture(SIDES)))
                .element(e -> e.from(14, 0, 2).to(16, 2, 14)
                        .face(Direction.EAST, f -> f.uvs(6, 2, 8, 14).rotation(Quadrant.R270).texture(SIDES).cullface(Direction.EAST))
                        .face(Direction.WEST, f -> f.uvs(10, 2, 12, 14).rotation(Quadrant.R90).texture(SIDES))
                        .face(Direction.UP, f -> f.uvs(8, 2, 10, 14).rotation(Quadrant.R180).texture(SIDES))
                        .face(Direction.DOWN, f -> f.uvs(12, 2, 14, 14).texture(SIDES).cullface(Direction.DOWN)))
                .element(e -> e.from(14, 14, 2).to(16, 16, 14)
                        .face(Direction.EAST, f -> f.uvs(8, 2, 6, 14).rotation(Quadrant.R270).texture(SIDES).cullface(Direction.EAST))
                        .face(Direction.WEST, f -> f.uvs(4, 2, 2, 14).rotation(Quadrant.R90).texture(SIDES))
                        .face(Direction.UP, f -> f.uvs(6, 2, 4, 14).rotation(Quadrant.R180).texture(SIDES).cullface(Direction.UP))
                        .face(Direction.DOWN, f -> f.uvs(10, 2, 8, 14).texture(SIDES)));
    }

    private static void crateColumnWide(ExtendedModelTemplateBuilder builder) {
        builder.element(e -> e.from(2, 7, 0).to(14, 9, 1)
                .face(Direction.NORTH, f -> f.uvs(0, 0, 12, 2).texture(FACING_COLUMNS).cullface(Direction.NORTH))
                .face(Direction.UP, f -> f.uvs(0, 0, 12, 1).texture(FACING_COLUMNS))
                .face(Direction.DOWN, f -> f.uvs(0, 0, 12, 1).texture(FACING_COLUMNS)));
    }

    private static void crateColumnWideVertical(ExtendedModelTemplateBuilder builder) {
        builder.element(e -> e.from(2, 15, 7).to(14, 16, 9)
                .face(Direction.NORTH, f -> f.uvs(0, 0, 12, 1).texture(FACING_COLUMNS))
                .face(Direction.SOUTH, f -> f.uvs(0, 0, 12, 1).texture(FACING_COLUMNS))
                .face(Direction.UP, f -> f.uvs(0, 0, 12, 2).texture(FACING_COLUMNS).cullface(Direction.UP)));
    }

    private static void crateColumnNarrow(ElementBuilder e, int fromY, int toY) {
        e.from(7, fromY, 0).to(9, toY, 1)
                .face(Direction.NORTH, f -> f.uvs(4, 0, 2, 5).texture(FACING_COLUMNS).cullface(Direction.NORTH))
                .face(Direction.EAST, f -> f.uvs(4, 0, 1, 5).texture(FACING_COLUMNS))
                .face(Direction.WEST, f -> f.uvs(4, 0, 1, 5).texture(FACING_COLUMNS));
    }

    private static void crateColumnNarrowVertical(ElementBuilder e, int fromZ, int toZ) {
        e.from(7, 15, fromZ).to(9, 16, toZ)
                .face(Direction.EAST, f -> f.uvs(4, 0, 5, 1).texture(FACING_COLUMNS))
                .face(Direction.WEST, f -> f.uvs(4, 0, 5, 1).texture(FACING_COLUMNS))
                .face(Direction.UP, f -> f.uvs(4, 0, 2, 5).texture(FACING_COLUMNS).cullface(Direction.UP));
    }

    // ------------------------------------------------------------------ helpers

    /**
     * Writes a model whose whole body is the {@code assortedcrates:locked} loader block. The loader
     * replaces the geometry outright, so the template declares no slots and no elements.
     */
    private static Identifier lockedModel(BiConsumer<Identifier, ModelInstance> output, Identifier target, Identifier unlocked, Identifier locked, Material particle) {
        return ExtendedModelTemplateBuilder.builder()
                // A block item reads its transforms from the block model's parent chain
                // (ResolvedModel#getTopTransforms); parenting to nothing yields ItemTransforms.NO_TRANSFORMS.
                // block/block supplies the standard display block and gui_light, and no geometry.
                .parent(Identifier.withDefaultNamespace("block/block"))
                .customLoader(LockedModelBuilder::begin, b -> b.unlockedModel(unlocked).lockedModel(locked))
                .requiredTextureSlot(TextureSlot.PARTICLE)
                .build()
                .create(target, new TextureMapping().put(TextureSlot.PARTICLE, particle), output);
    }

    private static String name(Block b) {
        return BuiltInRegistries.BLOCK.getKey(b).getPath();
    }

    private static Identifier resource(String path) {
        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, path);
    }

    private static Material texture(String path) {
        return new Material(resource(path));
    }
}
