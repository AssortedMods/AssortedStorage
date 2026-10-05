package com.grim3212.assorted.shulkers.client.data;

import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.shulkers.Constants;
import com.grim3212.assorted.lib.client.storage.LockedShulkerBoxSpecialRenderer;
import com.grim3212.assorted.shulkers.client.model.ShulkersModelLayers;
import com.grim3212.assorted.shulkers.client.model.ShulkersModels;
import com.grim3212.assorted.lib.core.storage.shulker.LockedShulkerBoxBlock;
import com.grim3212.assorted.shulkers.common.block.ShulkersBlocks;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.blockentity.ShulkerBoxRenderer;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Block states, block models and the block items' models. Every shulker box is drawn by its block
 * entity renderer, so its block model only names the break particle.
 */
public class ShulkersBlockstateProvider extends ModelProvider {

    /** The break/step particle of each shulker box. */
    private final Map<Block, Identifier> particles = new LinkedHashMap<>();

    /**
     * Block items drawn by a {@code minecraft:special} renderer rather than a model, on the display
     * transforms of vanilla's {@code minecraft:item/template_chest}.
     */
    private final Map<Block, LockedShulkerBoxSpecialRenderer.Unbaked> specialItems = new LinkedHashMap<>();

    public ShulkersBlockstateProvider(PackOutput output) {
        super(output, Constants.MOD_ID);

        for (IRegistryObject<LockedShulkerBoxBlock> b : ShulkersBlocks.SHULKERS.values()) {
            this.particles.put(b.get(), b.get().getStorageMaterial().getParticle(Constants.MOD_ID));
        }

        // Textured from the shulker box atlas, so the renderer is handed a sprite path, not a png.
        for (Map.Entry<StorageMaterial, IRegistryObject<LockedShulkerBoxBlock>> e : ShulkersBlocks.SHULKERS.entrySet()) {
            this.specialItems.put(e.getValue().get(), new LockedShulkerBoxSpecialRenderer.Unbaked(ShulkersModels.SHULKER_LOCATIONS.get(e.getKey()), ShulkersModelLayers.LOCKED_SHULKER_BOX));
        }
    }

    @Override
    public String getName() {
        return "Assorted Shulkers block states";
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.particles.forEach((block, texture) -> particleOnlyBlock(blockModels, block, texture));
        this.specialItems.forEach((block, renderer) -> specialItem(blockModels, block, renderer));
    }

    private void particleOnlyBlock(BlockModelGenerators blockModels, Block block, Identifier texture) {
        MultiVariant model = BlockModelGenerators.plainVariant(ModelTemplates.PARTICLE_ONLY.create(block, TextureMapping.particle(new Material(texture)), blockModels.modelOutput));
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, model));
    }

    /**
     * {@code SpecialModelRenderer#submit} applies no transform, and this renderer is authored in
     * block-entity space, so the block transform is supplied here as vanilla does in {@code createShulkerBox}.
     */
    private void specialItem(BlockModelGenerators blockModels, Block block, LockedShulkerBoxSpecialRenderer.Unbaked renderer) {
        Item item = block.asItem();
        Identifier base = ModelTemplates.CHEST_INVENTORY.create(item, TextureMapping.particle(new Material(this.particles.get(block))), blockModels.modelOutput);
        blockModels.itemModelOutput.accept(item, ItemModelUtils.specialModel(base, Optional.of(ShulkerBoxRenderer.modelTransform(Direction.UP)), renderer));
    }
}
