package com.grim3212.assorted.containers.client.data;

import com.grim3212.assorted.containers.Constants;
import com.grim3212.assorted.containers.common.block.ContainersBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Block states, block models and item models. Every block here is drawn by a block entity renderer,
 * so its model only names the particle, and its item is a {@code builtin/entity} model on vanilla's
 * chest display transforms, drawn by the item renderer {@code ContainersClient} registers for it.
 */
public class ContainersBlockstateProvider extends BlockStateProvider {

    /**
     * The models with no geometry of their own: the block is drawn by a block entity renderer and
     * the json exists only to name the break/step particle.
     */
    private final Map<Block, ResourceLocation> particleOnly = new LinkedHashMap<>();

    public ContainersBlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Constants.MOD_ID, exFileHelper);

        particle(ContainersBlocks.WOOD_CABINET.get(), resource("block/cabinet_break"));
        particle(ContainersBlocks.GLASS_CABINET.get(), resource("block/cabinet_break"));
        particle(ContainersBlocks.GOLD_SAFE.get(), ResourceLocation.withDefaultNamespace("block/gold_block"));
        particle(ContainersBlocks.OBSIDIAN_SAFE.get(), ResourceLocation.withDefaultNamespace("block/obsidian"));
        particle(ContainersBlocks.LOCKER.get(), ResourceLocation.withDefaultNamespace("block/iron_block"));
        particle(ContainersBlocks.ITEM_TOWER.get(), ResourceLocation.withDefaultNamespace("block/iron_block"));
        ContainersBlocks.WAREHOUSE_CRATES.forEach((wood, crate) -> particle(crate.get(), ResourceLocation.withDefaultNamespace("block/" + wood.getLogTopTextureName())));
    }

    private void particle(Block block, ResourceLocation texture) {
        this.particleOnly.put(block, texture);
    }

    @Override
    public String getName() {
        return "Assorted Containers block states";
    }

    @Override
    protected void registerStatesAndModels() {
        this.particleOnly.forEach(this::particleOnlyBlock);
    }

    private void particleOnlyBlock(Block block, ResourceLocation texture) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        ModelFile model = this.models().getBuilder(name).texture("particle", texture);
        this.simpleBlock(block, model);
        this.itemModels().withExistingParent(name, mcLoc("item/chest")).texture("particle", texture);
    }

    private static ResourceLocation resource(String path) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path);
    }
}
