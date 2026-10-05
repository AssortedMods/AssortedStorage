package com.grim3212.assorted.hoppers.client.data;

import com.grim3212.assorted.hoppers.Constants;
import com.grim3212.assorted.hoppers.common.block.HoppersBlocks;
import com.grim3212.assorted.hoppers.client.HoppersClient;
import com.grim3212.assorted.lib.client.data.LockedModelBuilder;
import com.grim3212.assorted.lib.core.storage.hopper.LockedHopperBlock;
import com.grim3212.assorted.lib.client.data.SpecificationBlockStateModelBuilder;
import com.grim3212.assorted.lib.registry.IRegistryObject;
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
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;

import java.util.function.BiConsumer;

/**
 * Block states, block models and the hopper items' models. Every hopper switches between its
 * unlocked and locked model through the {@code assortedhoppers:locked} loader.
 */
public class HoppersBlockstateProvider extends ModelProvider {

    // Vanilla has no constant for this slot, and TextureSlot has no equals, so it is made once and shared.
    private static final TextureSlot TOPSIDES = TextureSlot.create("topsides");

    private static final Identifier VANILLA_HOPPER = Identifier.withDefaultNamespace("block/hopper");
    private static final Identifier VANILLA_HOPPER_SIDE = Identifier.withDefaultNamespace("block/hopper_side");
    private static final Identifier TEMPLATE_HOPPER = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "block/template_hopper");
    private static final Identifier TEMPLATE_HOPPER_SIDE = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "block/template_hopper_side");

    public HoppersBlockstateProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    public String getName() {
        return "Assorted Hoppers block states";
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        for (IRegistryObject<LockedHopperBlock> b : HoppersBlocks.HOPPERS.values()) {
            materialHopper(blockModels, b.get());
        }
    }

    private void materialHopper(BlockModelGenerators blockModels, LockedHopperBlock b) {
        String material = b.getStorageMaterial().toString();
        Material outside = texture("block/hoppers/" + material + "/hopper_outside");
        Material top = texture("block/hoppers/" + material + "/hopper_top");
        Material inside = texture("block/hoppers/" + material + "/hopper_inside");
        Material lockedOutside = texture("block/hoppers/" + material + "/locked_hopper_outside");

        TextureMapping unlockedTextures = new TextureMapping()
                .put(TextureSlot.PARTICLE, outside)
                .put(TextureSlot.TOP, top)
                .put(TextureSlot.SIDE, outside)
                .put(TextureSlot.INSIDE, inside);
        TextureMapping lockedTextures = unlockedTextures.copyAndUpdate(TOPSIDES, lockedOutside);

        Identifier unlocked = HOPPER_TEXTURED.create(ModelLocationUtils.getModelLocation(b, "_unlocked"), unlockedTextures, blockModels.modelOutput);
        Identifier locked = LOCKED_HOPPER_TEXTURED.create(ModelLocationUtils.getModelLocation(b, "_locked"), lockedTextures, blockModels.modelOutput);
        Identifier unlockedSide = HOPPER_SIDE_TEXTURED.create(ModelLocationUtils.getModelLocation(b, "_unlocked_side"), unlockedTextures, blockModels.modelOutput);
        Identifier lockedSide = LOCKED_HOPPER_SIDE_TEXTURED.create(ModelLocationUtils.getModelLocation(b, "_locked_side"), lockedTextures, blockModels.modelOutput);

        hopperState(blockModels, b, unlocked, locked, unlockedSide, lockedSide, outside);
    }

    private static final ModelTemplate HOPPER_TEXTURED = ExtendedModelTemplateBuilder.builder().parent(VANILLA_HOPPER)
            .requiredTextureSlot(TextureSlot.PARTICLE).requiredTextureSlot(TextureSlot.TOP).requiredTextureSlot(TextureSlot.SIDE).requiredTextureSlot(TextureSlot.INSIDE).build();
    private static final ModelTemplate HOPPER_SIDE_TEXTURED = ExtendedModelTemplateBuilder.builder().parent(VANILLA_HOPPER_SIDE)
            .requiredTextureSlot(TextureSlot.PARTICLE).requiredTextureSlot(TextureSlot.TOP).requiredTextureSlot(TextureSlot.SIDE).requiredTextureSlot(TextureSlot.INSIDE).build();
    private static final ModelTemplate LOCKED_HOPPER_TEXTURED = ExtendedModelTemplateBuilder.builder().parent(TEMPLATE_HOPPER)
            .requiredTextureSlot(TextureSlot.PARTICLE).requiredTextureSlot(TextureSlot.TOP).requiredTextureSlot(TextureSlot.SIDE).requiredTextureSlot(TextureSlot.INSIDE).requiredTextureSlot(TOPSIDES).build();
    private static final ModelTemplate LOCKED_HOPPER_SIDE_TEXTURED = ExtendedModelTemplateBuilder.builder().parent(TEMPLATE_HOPPER_SIDE)
            .requiredTextureSlot(TextureSlot.PARTICLE).requiredTextureSlot(TextureSlot.TOP).requiredTextureSlot(TextureSlot.SIDE).requiredTextureSlot(TextureSlot.INSIDE).requiredTextureSlot(TOPSIDES).build();

    /**
     * {@code ENABLED} is not dispatched on, which is how the 1.20.1 {@code forAllStatesExcept} call
     * expressed the same thing: a property nothing selects on simply does not appear in the
     * blockstate key. The rotations are vanilla's own hopper dispatch.
     */
    private void hopperState(BlockModelGenerators blockModels, LockedHopperBlock b, Identifier unlocked, Identifier locked, Identifier unlockedSide, Identifier lockedSide, Material particle) {
        MultiVariant down = SpecificationBlockStateModelBuilder.specificationVariant(lockedModel(blockModels.modelOutput, ModelLocationUtils.getModelLocation(b), unlocked, locked, particle));
        MultiVariant side = SpecificationBlockStateModelBuilder.specificationVariant(lockedModel(blockModels.modelOutput, ModelLocationUtils.getModelLocation(b, "_side"), unlockedSide, lockedSide, particle));

        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(b)
                .with(PropertyDispatch.initial(BlockStateProperties.FACING_HOPPER)
                        .select(Direction.DOWN, down)
                        .select(Direction.NORTH, side)
                        .select(Direction.EAST, side.with(BlockModelGenerators.Y_ROT_90))
                        .select(Direction.SOUTH, side.with(BlockModelGenerators.Y_ROT_180))
                        .select(Direction.WEST, side.with(BlockModelGenerators.Y_ROT_270))));

        // A hopper's item is a flat sprite, not its block model - the default BlockItem fallback
        // would point it at the locked-loader model instead, which is not what 1.20.1 shipped.
        Item item = b.asItem();
        Identifier itemModel = ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(item), blockModels.modelOutput);
        blockModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(itemModel));
    }

    /**
     * Writes a model whose whole body is the {@code assortedhoppers:locked} loader block. The loader
     * replaces the geometry outright, so the template declares no slots and no elements.
     */
    private static Identifier lockedModel(BiConsumer<Identifier, ModelInstance> output, Identifier target, Identifier unlocked, Identifier locked, Material particle) {
        return ExtendedModelTemplateBuilder.builder()
                // A block item reads its transforms from the block model's parent chain
                // (ResolvedModel#getTopTransforms); parenting to nothing yields ItemTransforms.NO_TRANSFORMS.
                // block/block supplies the standard display block and gui_light, and no geometry.
                .parent(Identifier.withDefaultNamespace("block/block"))
                .customLoader(() -> LockedModelBuilder.begin(HoppersClient.LOCKED_MODEL_LOADER), b -> b.unlockedModel(unlocked).lockedModel(locked))
                .requiredTextureSlot(TextureSlot.PARTICLE)
                .build()
                .create(target, new TextureMapping().put(TextureSlot.PARTICLE, particle), output);
    }

    /** A model that is nothing but a parent reference, which is what {@code withExistingParent} was. */
    private static Identifier parented(BiConsumer<Identifier, ModelInstance> output, Identifier target, Identifier parent) {
        return ExtendedModelTemplateBuilder.builder().parent(parent).build().create(target, new TextureMapping(), output);
    }

    private static Material texture(String path) {
        return new Material(Identifier.fromNamespaceAndPath(Constants.MOD_ID, path));
    }
}
