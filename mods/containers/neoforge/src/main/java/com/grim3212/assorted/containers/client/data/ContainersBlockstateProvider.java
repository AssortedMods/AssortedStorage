package com.grim3212.assorted.containers.client.data;

import com.grim3212.assorted.containers.Constants;
import com.grim3212.assorted.containers.client.blockentity.WarehouseCrateBlockEntityRenderer;
import com.grim3212.assorted.containers.client.blockentity.item.ContainersSpecialRenderer;
import com.grim3212.assorted.containers.client.blockentity.item.ItemTowerSpecialRenderer;
import com.grim3212.assorted.containers.client.model.ContainersModelType;
import com.grim3212.assorted.containers.common.block.ContainersBlocks;
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
 * Block states, block models and item models. Every block here is drawn by a block entity renderer, so its
 * model only names the particle and its item is a {@code minecraft:special} renderer.
 */
public class ContainersBlockstateProvider extends ModelProvider {

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

    public ContainersBlockstateProvider(PackOutput output) {
        super(output, Constants.MOD_ID);

        particle(ContainersBlocks.WOOD_CABINET.get(), resource("block/cabinet_break"));
        particle(ContainersBlocks.GLASS_CABINET.get(), resource("block/cabinet_break"));
        particle(ContainersBlocks.GOLD_SAFE.get(), Identifier.parse("block/gold_block"));
        particle(ContainersBlocks.OBSIDIAN_SAFE.get(), Identifier.parse("block/obsidian"));
        particle(ContainersBlocks.LOCKER.get(), Identifier.parse("block/iron_block"));
        particle(ContainersBlocks.ITEM_TOWER.get(), Identifier.parse("block/iron_block"));
        ContainersBlocks.WAREHOUSE_CRATES.forEach((wood, crate) -> particle(crate.get(), Identifier.parse("block/" + wood.getLogTopTextureName())));

        // Which renderer each of those items uses. The textures are the ones the matching block
        // entity renderer resolves, so the item and the placed block look the same.
        special(ContainersBlocks.WOOD_CABINET.get(), ContainersModelType.CABINET, modelTexture("cabinet"));
        special(ContainersBlocks.GLASS_CABINET.get(), ContainersModelType.GLASS_CABINET, modelTexture("cabinet"));
        special(ContainersBlocks.GOLD_SAFE.get(), ContainersModelType.SAFE, modelTexture("gold_safe"));
        special(ContainersBlocks.OBSIDIAN_SAFE.get(), ContainersModelType.SAFE, modelTexture("obsidian_safe"));
        special(ContainersBlocks.LOCKER.get(), ContainersModelType.LOCKER, modelTexture("locker"));
        ContainersBlocks.WAREHOUSE_CRATES.forEach((wood, crate) -> special(crate.get(), ContainersModelType.WAREHOUSE_CRATE, WarehouseCrateBlockEntityRenderer.texture(wood)));

        this.specialItems.put(ContainersBlocks.ITEM_TOWER.get(), new ItemTowerSpecialRenderer.Unbaked());
    }

    private void particle(Block block, Identifier texture) {
        this.particleOnly.put(block, texture);
    }

    private void special(Block block, ContainersModelType model, Identifier texture) {
        this.specialItems.put(block, new ContainersSpecialRenderer.Unbaked(model, texture));
    }

    @Override
    public String getName() {
        return "Assorted Containers block states";
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

    // Every renderer here draws in item space already, so none needs a block transform.
    private void specialItem(BlockModelGenerators blockModels, Block block, SpecialModelRenderer.Unbaked<?> renderer) {
        Item item = block.asItem();
        Identifier base = ModelTemplates.CHEST_INVENTORY.create(item, TextureMapping.particle(new Material(this.particleOnly.get(block))), blockModels.modelOutput);
        blockModels.itemModelOutput.accept(item, ItemModelUtils.specialModel(base, Optional.empty(), renderer));
    }

    private static Identifier resource(String path) {
        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, path);
    }

    private static Identifier modelTexture(String name) {
        return resource("textures/model/" + name + ".png");
    }
}
