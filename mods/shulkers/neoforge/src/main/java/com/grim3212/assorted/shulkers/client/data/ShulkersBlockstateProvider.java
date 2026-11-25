package com.grim3212.assorted.shulkers.client.data;

import com.grim3212.assorted.lib.core.storage.shulker.LockedShulkerBoxBlock;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.shulkers.Constants;
import com.grim3212.assorted.shulkers.common.block.ShulkersBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * Block states, block models and block item models. Every shulker box is drawn by a block entity
 * renderer, so the blocks only name a particle, and the items are {@code builtin/entity} models on
 * vanilla's shulker box display transforms, drawn by the item renderer {@code ShulkersClient} registers.
 */
public class ShulkersBlockstateProvider extends BlockStateProvider {

    public ShulkersBlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Constants.MOD_ID, exFileHelper);
    }

    @Override
    public String getName() {
        return "Assorted Shulkers block states";
    }

    @Override
    protected void registerStatesAndModels() {
        for (IRegistryObject<LockedShulkerBoxBlock> b : ShulkersBlocks.SHULKERS.values()) {
            shulkerBox(b.get(), b.get().getStorageMaterial().getParticle(Constants.MOD_ID));
        }
    }

    private void shulkerBox(Block block, ResourceLocation particle) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        ModelFile model = this.models().getBuilder(name).texture("particle", particle);
        this.simpleBlock(block, model);
        this.itemModels().withExistingParent(name, this.mcLoc("item/template_shulker_box")).texture("particle", particle);
    }
}
