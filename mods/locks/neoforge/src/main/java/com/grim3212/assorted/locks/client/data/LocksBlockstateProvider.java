package com.grim3212.assorted.locks.client.data;

import com.grim3212.assorted.lib.client.data.LockedModelBuilder;
import com.grim3212.assorted.lib.client.data.SpecificationBlockStateModelBuilder;
import com.grim3212.assorted.lib.client.storage.LockedChestSpecialRenderer;
import com.grim3212.assorted.lib.client.storage.LockedShulkerBoxSpecialRenderer;
import com.grim3212.assorted.lib.core.storage.barrel.LockedBarrelBlock;
import com.grim3212.assorted.locks.Constants;
import com.grim3212.assorted.locks.client.model.LocksStorageModels;
import com.grim3212.assorted.locks.common.block.LocksBlocks;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.blockentity.ShulkerBoxRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

/**
 * Block states and block models. This owns every block and block item, and
 * {@link LocksItemModelProvider} owns the rest, so the two never write the same file.
 */
public class LocksBlockstateProvider extends ModelProvider {

    public LocksBlockstateProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    public String getName() {
        return "Assorted Locks block states";
    }

    /**
     * Only the block items belong here; every other item is {@link LocksItemModelProvider}'s, so
     * the two providers never write the same file.
     */
    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return super.getKnownItems().filter(holder -> holder.value() instanceof BlockItem);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        locksmithWorkbench(blockModels);

        for (Block door : LocksBlocks.lockedDoors()) {
            door(blockModels, door);
        }

        lockedChest(blockModels, LocksBlocks.LOCKED_ENDER_CHEST.get(), Identifier.parse("block/obsidian"), LocksStorageModels.ENDER_CHEST_SPRITE);
        lockedChest(blockModels, LocksBlocks.LOCKED_CHEST.get(), Identifier.parse("block/oak_planks"), LocksStorageModels.CHEST_SPRITE);
        lockedShulkerBox(blockModels);
        lockedBarrel(blockModels);
        lockedHopper(blockModels);
    }

    /** Drawn by its block entity renderer, so the block only names a particle and the item a special renderer. */
    private void lockedChest(BlockModelGenerators blockModels, Block block, Identifier particle, Identifier sprite) {
        particleOnlyBlock(blockModels, block, particle);
        Identifier base = ModelTemplates.CHEST_INVENTORY.create(block.asItem(), TextureMapping.particle(new Material(particle)), blockModels.modelOutput);
        SpecialModelRenderer.Unbaked<?> renderer = new LockedChestSpecialRenderer.Unbaked(sprite, LocksStorageModels.CHEST_LAYER);
        blockModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.specialModel(base, Optional.empty(), renderer));
    }

    /** The item renderer is authored in block entity space, so the block transform comes from here as vanilla's does. */
    private void lockedShulkerBox(BlockModelGenerators blockModels) {
        Block block = LocksBlocks.LOCKED_SHULKER_BOX.get();
        Identifier particle = Identifier.parse("block/shulker_box");
        particleOnlyBlock(blockModels, block, particle);
        Identifier base = ModelTemplates.CHEST_INVENTORY.create(block.asItem(), TextureMapping.particle(new Material(particle)), blockModels.modelOutput);
        SpecialModelRenderer.Unbaked<?> renderer = new LockedShulkerBoxSpecialRenderer.Unbaked(LocksStorageModels.SHULKER_BOX_SPRITE, LocksStorageModels.SHULKER_BOX_LAYER);
        blockModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.specialModel(base, Optional.of(ShulkerBoxRenderer.modelTransform(Direction.UP)), renderer));
    }

    private void particleOnlyBlock(BlockModelGenerators blockModels, Block block, Identifier texture) {
        MultiVariant model = BlockModelGenerators.plainVariant(ModelTemplates.PARTICLE_ONLY.create(block, TextureMapping.particle(new Material(texture)), blockModels.modelOutput));
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, model));
    }

    /**
     * The closed barrel is the locked loader model and the open one is plain. The item picks between the locked and
     * unlocked models itself, on the stack's lock, as it has no block entity for the loader to read.
     */
    private void lockedBarrel(BlockModelGenerators blockModels) {
        Block b = LocksBlocks.LOCKED_BARREL.get();
        Material side = new Material(Identifier.parse("block/barrel_side"));
        Material bottom = new Material(Identifier.parse("block/barrel_bottom"));

        Identifier unlocked = ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(b, "_unlocked", barrelTextures(side, bottom, new Material(Identifier.parse("block/barrel_top"))), blockModels.modelOutput);
        Identifier locked = ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(b, "_locked", barrelTextures(side, bottom, texture("block/barrels/locked_barrel_top")), blockModels.modelOutput);
        Identifier open = ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(b, "_open", barrelTextures(side, bottom, new Material(Identifier.parse("block/barrel_top_open"))), blockModels.modelOutput);

        MultiVariant closedModel = SpecificationBlockStateModelBuilder.specificationVariant(lockedModel(blockModels.modelOutput, ModelLocationUtils.getModelLocation(b), unlocked, locked, side));
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(b)
                .with(BlockModelGenerators.createBooleanModelDispatch(LockedBarrelBlock.OPEN, BlockModelGenerators.plainVariant(open), closedModel))
                .with(BlockModelGenerators.ROTATIONS_COLUMN_WITH_FACING));

        blockModels.itemModelOutput.accept(b.asItem(), ItemModelUtils.conditional(LocksStorageModels.LOCKED_PROPERTY,
                ItemModelUtils.plainModel(locked), ItemModelUtils.plainModel(unlocked)));
    }

    private static TextureMapping barrelTextures(Material side, Material bottom, Material top) {
        return new TextureMapping().put(TextureSlot.SIDE, side).put(TextureSlot.BOTTOM, bottom).put(TextureSlot.TOP, top);
    }

    /** The unlocked halves are vanilla's own hopper models, and the item is a flat sprite with the padlock drawn on. */
    private void lockedHopper(BlockModelGenerators blockModels) {
        Block b = LocksBlocks.LOCKED_HOPPER.get();
        Material particle = new Material(Identifier.parse("block/hopper_outside"));
        Identifier locked = parented(blockModels.modelOutput, ModelLocationUtils.getModelLocation(b, "_locked"), Identifier.fromNamespaceAndPath(Constants.MOD_ID, "block/template_hopper"));
        Identifier lockedSide = parented(blockModels.modelOutput, ModelLocationUtils.getModelLocation(b, "_locked_side"), Identifier.fromNamespaceAndPath(Constants.MOD_ID, "block/template_hopper_side"));

        MultiVariant down = SpecificationBlockStateModelBuilder.specificationVariant(lockedModel(blockModels.modelOutput, ModelLocationUtils.getModelLocation(b), Identifier.withDefaultNamespace("block/hopper"), locked, particle));
        MultiVariant side = SpecificationBlockStateModelBuilder.specificationVariant(lockedModel(blockModels.modelOutput, ModelLocationUtils.getModelLocation(b, "_side"), Identifier.withDefaultNamespace("block/hopper_side"), lockedSide, particle));

        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(b)
                .with(PropertyDispatch.initial(BlockStateProperties.FACING_HOPPER)
                        .select(Direction.DOWN, down)
                        .select(Direction.NORTH, side)
                        .select(Direction.EAST, side.with(BlockModelGenerators.Y_ROT_90))
                        .select(Direction.SOUTH, side.with(BlockModelGenerators.Y_ROT_180))
                        .select(Direction.WEST, side.with(BlockModelGenerators.Y_ROT_270))));

        Item item = b.asItem();
        Identifier itemModel = ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(item), blockModels.modelOutput);
        blockModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(itemModel));
    }

    /** A model whose whole body is the locked loader block, which replaces the geometry outright. */
    private static Identifier lockedModel(BiConsumer<Identifier, ModelInstance> output, Identifier target, Identifier unlocked, Identifier locked, Material particle) {
        return ExtendedModelTemplateBuilder.builder()
                // block/block gives a block item its display transforms and no geometry of its own.
                .parent(Identifier.withDefaultNamespace("block/block"))
                .customLoader(() -> LockedModelBuilder.begin(LocksStorageModels.LOCKED_MODEL_LOADER), b -> b.unlockedModel(unlocked).lockedModel(locked))
                .requiredTextureSlot(TextureSlot.PARTICLE)
                .build()
                .create(target, new TextureMapping().put(TextureSlot.PARTICLE, particle), output);
    }

    private static Identifier parented(BiConsumer<Identifier, ModelInstance> output, Identifier target, Identifier parent) {
        return ExtendedModelTemplateBuilder.builder().parent(parent).build().create(target, new TextureMapping(), output);
    }

    private void locksmithWorkbench(BlockModelGenerators blockModels) {
        Block b = LocksBlocks.LOCKSMITH_WORKBENCH.get();
        TextureMapping textures = new TextureMapping()
                .put(TextureSlot.PARTICLE, texture("block/locksmith_front"))
                .put(TextureSlot.DOWN, new Material(Identifier.parse("block/oak_planks")))
                .put(TextureSlot.UP, texture("block/locksmith_top"))
                .put(TextureSlot.NORTH, texture("block/locksmith_front"))
                .put(TextureSlot.SOUTH, texture("block/locksmith_side"))
                .put(TextureSlot.EAST, texture("block/locksmith_side"))
                .put(TextureSlot.WEST, texture("block/locksmith_front"));

        MultiVariant model = BlockModelGenerators.plainVariant(ModelTemplates.CUBE.create(b, textures, blockModels.modelOutput));
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(b, model));
    }

    /**
     * Which other locked door a door takes its textures from. A waxed copper door looks exactly like
     * the unwaxed one of the same oxidation stage - vanilla shares those textures too - so only the
     * four unwaxed locked copper doors ship a png.
     */
    private static final Map<Block, Block> DOOR_TEXTURE_SOURCE = new HashMap<>();

    static {
        Blocks.COPPER_DOOR.zipUnwaxedWaxed((unwaxed, waxed) ->
                DOOR_TEXTURE_SOURCE.put(LocksBlocks.VANILLA_DOORS.get(waxed).get(), LocksBlocks.VANILLA_DOORS.get(unwaxed).get()));
    }

    /**
     * A locked door's block state and models. The doors have no item, so vanilla's {@code
     * createDoor}, which also writes an item model, cannot be used.
     */
    private void door(BlockModelGenerators blockModels, Block door) {
        TextureMapping mapping = TextureMapping.door(DOOR_TEXTURE_SOURCE.getOrDefault(door, door));
        MultiVariant bottomLeft = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_BOTTOM_LEFT.create(door, mapping, blockModels.modelOutput));
        MultiVariant bottomLeftOpen = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_BOTTOM_LEFT_OPEN.create(door, mapping, blockModels.modelOutput));
        MultiVariant bottomRight = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_BOTTOM_RIGHT.create(door, mapping, blockModels.modelOutput));
        MultiVariant bottomRightOpen = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_BOTTOM_RIGHT_OPEN.create(door, mapping, blockModels.modelOutput));
        MultiVariant topLeft = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_TOP_LEFT.create(door, mapping, blockModels.modelOutput));
        MultiVariant topLeftOpen = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_TOP_LEFT_OPEN.create(door, mapping, blockModels.modelOutput));
        MultiVariant topRight = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_TOP_RIGHT.create(door, mapping, blockModels.modelOutput));
        MultiVariant topRightOpen = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_TOP_RIGHT_OPEN.create(door, mapping, blockModels.modelOutput));

        blockModels.blockStateOutput.accept(BlockModelGenerators.createDoor(door, bottomLeft, bottomLeftOpen, bottomRight, bottomRightOpen, topLeft, topLeftOpen, topRight, topRightOpen));
    }

    private static Material texture(String path) {
        return new Material(Identifier.fromNamespaceAndPath(Constants.MOD_ID, path));
    }
}
