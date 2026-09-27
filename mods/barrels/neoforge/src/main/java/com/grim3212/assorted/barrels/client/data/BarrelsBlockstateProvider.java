package com.grim3212.assorted.barrels.client.data;

import com.grim3212.assorted.lib.client.data.SpecificationBlockStateModelBuilder;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.barrels.Constants;
import com.grim3212.assorted.barrels.client.BarrelsClient;
import com.grim3212.assorted.lib.client.data.LockedModelBuilder;
import com.grim3212.assorted.lib.core.storage.barrel.LockedBarrelBlock;
import com.grim3212.assorted.barrels.common.block.BarrelsBlocks;
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
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;

import java.util.function.BiConsumer;

/** Block states, block models and block item models for the barrels. */
public class BarrelsBlockstateProvider extends ModelProvider {

    /**
     * The barrel's own rotation was {@code x = down ? 180 : up ? 0 : 90} and
     * {@code y = vertical ? 0 : (facing.toYRot() + 180) % 360}, which is exactly what
     * {@link BlockModelGenerators#ROTATIONS_COLUMN_WITH_FACING} spells out.
     */
    private static final PropertyDispatch<VariantMutator> BARREL_ROTATION = BlockModelGenerators.ROTATIONS_COLUMN_WITH_FACING;

    public BarrelsBlockstateProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    public String getName() {
        return "Assorted Barrels block states";
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        for (IRegistryObject<LockedBarrelBlock> b : BarrelsBlocks.BARRELS.values()) {
            materialBarrel(blockModels, b.get());
        }
    }

    private void materialBarrel(BlockModelGenerators blockModels, LockedBarrelBlock b) {
        String material = b.getStorageMaterial().toString();
        Material side = texture("block/barrels/" + material + "/barrel_side");
        Material bottom = texture("block/barrels/" + material + "/barrel_bottom");

        Identifier unlocked = ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(b, "_unlocked", barrelTextures(side, bottom, texture("block/barrels/" + material + "/barrel_top")), blockModels.modelOutput);
        Identifier locked = ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(b, "_locked", barrelTextures(side, bottom, texture("block/barrels/" + material + "/locked_barrel_top")), blockModels.modelOutput);
        Identifier open = ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(b, "_open", barrelTextures(side, bottom, texture("block/barrels/" + material + "/barrel_top_open")), blockModels.modelOutput);

        barrelState(blockModels, b, unlocked, locked, open, side);
    }

    private static TextureMapping barrelTextures(Material side, Material bottom, Material top) {
        return new TextureMapping().put(TextureSlot.SIDE, side).put(TextureSlot.BOTTOM, bottom).put(TextureSlot.TOP, top);
    }

    /**
     * The closed barrel is the {@code assortedbarrels:locked} loader model; the open one is plain.
     * The item picks between the locked and unlocked children itself, on the stack's lock, because
     * an item has no block entity for the loader to read.
     */
    private void barrelState(BlockModelGenerators blockModels, LockedBarrelBlock b, Identifier unlocked, Identifier locked, Identifier open, Material particle) {
        MultiVariant closedModel = SpecificationBlockStateModelBuilder.specificationVariant(lockedModel(blockModels.modelOutput, ModelLocationUtils.getModelLocation(b), unlocked, locked, particle));
        MultiVariant openModel = BlockModelGenerators.plainVariant(open);

        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(b)
                .with(BlockModelGenerators.createBooleanModelDispatch(LockedBarrelBlock.OPEN, openModel, closedModel))
                .with(BARREL_ROTATION));

        blockModels.itemModelOutput.accept(b.asItem(), ItemModelUtils.conditional(BarrelsClient.LOCKED_PROPERTY,
                ItemModelUtils.plainModel(locked), ItemModelUtils.plainModel(unlocked)));
    }

    /**
     * Writes a model whose whole body is the {@code assortedbarrels:locked} loader block. The loader
     * replaces the geometry outright, so the template declares no slots and no elements.
     */
    private static Identifier lockedModel(BiConsumer<Identifier, ModelInstance> output, Identifier target, Identifier unlocked, Identifier locked, Material particle) {
        return ExtendedModelTemplateBuilder.builder()
                // A block item reads its transforms from the block model's parent chain
                // (ResolvedModel#getTopTransforms); parenting to nothing yields ItemTransforms.NO_TRANSFORMS.
                // block/block supplies the standard display block and gui_light, and no geometry.
                .parent(Identifier.withDefaultNamespace("block/block"))
                .customLoader(() -> LockedModelBuilder.begin(BarrelsClient.LOCKED_MODEL_LOADER), b -> b.unlockedModel(unlocked).lockedModel(locked))
                .requiredTextureSlot(TextureSlot.PARTICLE)
                .build()
                .create(target, new TextureMapping().put(TextureSlot.PARTICLE, particle), output);
    }

    private static Material texture(String path) {
        return new Material(Identifier.fromNamespaceAndPath(Constants.MOD_ID, path));
    }
}
