package com.grim3212.assorted.chests.client.data;

import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.chests.Constants;
import com.grim3212.assorted.lib.core.storage.StorageMaterial;
import com.grim3212.assorted.chests.client.blockentity.item.ChestsSpecialRenderer;
import com.grim3212.assorted.chests.client.blockentity.item.LockedChestSpecialRenderer;
import com.grim3212.assorted.chests.client.model.ChestsModelType;
import com.grim3212.assorted.chests.client.model.ChestsModels;
import com.grim3212.assorted.chests.common.block.LockedChestBlock;
import com.grim3212.assorted.chests.common.block.ChestsBlocks;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Block states, block models and block item models. Every chest is drawn by a block entity
 * renderer, so the blocks only name a particle and the items a special renderer.
 */
public class ChestsBlockstateProvider extends ModelProvider {

    /**
     * The models with no geometry of their own: the block is drawn by a block entity renderer and
     * the json exists only to name the break/step particle.
     */
    private final Map<Block, Identifier> particleOnly = new LinkedHashMap<>();

    /**
     * Block items drawn by a {@code minecraft:special} renderer rather than a model, on the display
     * transforms of vanilla's {@code minecraft:item/template_chest}.
     */
    private final Map<Block, SpecialModelRenderer.Unbaked<?>> specialItems = new LinkedHashMap<>();

    public ChestsBlockstateProvider(PackOutput output) {
        super(output, Constants.MOD_ID);

        particle(ChestsBlocks.LOCKED_ENDER_CHEST.get(), Identifier.parse("block/obsidian"));
        particle(ChestsBlocks.LOCKED_CHEST.get(), Identifier.parse("block/oak_planks"));

        for (IRegistryObject<LockedChestBlock> b : ChestsBlocks.CHESTS.values()) {
            particle(b.get(), b.get().getStorageMaterial().getParticle(Constants.MOD_ID));
        }

        // Which renderer each of those items uses. The textures are the ones the matching block
        // entity renderer resolves, so the item and the placed block look the same.
        this.specialItems.put(ChestsBlocks.LOCKED_ENDER_CHEST.get(), new ChestsSpecialRenderer.Unbaked(ChestsModelType.CHEST, modelTexture("locked_ender_chest")));

        // The locked chests are textured from the chest atlas, so the renderer is handed a sprite
        // path rather than a standalone png.
        this.specialItems.put(ChestsBlocks.LOCKED_CHEST.get(), new LockedChestSpecialRenderer.Unbaked(ChestsModels.CHEST_LOCATIONS.get(null)));

        for (Map.Entry<StorageMaterial, IRegistryObject<LockedChestBlock>> e : ChestsBlocks.CHESTS.entrySet()) {
            this.specialItems.put(e.getValue().get(), new LockedChestSpecialRenderer.Unbaked(ChestsModels.CHEST_LOCATIONS.get(e.getKey())));
        }
    }

    private void particle(Block block, Identifier texture) {
        this.particleOnly.put(block, texture);
    }

    @Override
    public String getName() {
        return "Assorted Chests block states";
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.particleOnly.forEach((block, texture) -> particleOnlyBlock(blockModels, block, texture));
        this.specialItems.forEach((block, renderer) -> specialItem(blockModels, block, renderer));
    }

    private void particleOnlyBlock(BlockModelGenerators blockModels, Block block, Identifier texture) {
        MultiVariant model = BlockModelGenerators.plainVariant(ModelTemplates.PARTICLE_ONLY.create(block, TextureMapping.particle(new Material(texture)), blockModels.modelOutput));
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, model));
    }

    private void specialItem(BlockModelGenerators blockModels, Block block, SpecialModelRenderer.Unbaked<?> renderer) {
        Item item = block.asItem();
        Identifier base = ModelTemplates.CHEST_INVENTORY.create(item, TextureMapping.particle(new Material(this.particleOnly.get(block))), blockModels.modelOutput);
        blockModels.itemModelOutput.accept(item, ItemModelUtils.specialModel(base, Optional.empty(), renderer));
    }

    private static Identifier modelTexture(String name) {
        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/model/" + name + ".png");
    }
}
