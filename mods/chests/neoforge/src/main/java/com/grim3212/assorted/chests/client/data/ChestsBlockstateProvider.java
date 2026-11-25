package com.grim3212.assorted.chests.client.data;

import com.grim3212.assorted.chests.Constants;
import com.grim3212.assorted.chests.common.block.ChestsBlocks;
import com.grim3212.assorted.lib.core.storage.chest.LockedChestBlock;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * Block states, block models and block item models. Every chest is drawn by a block entity
 * renderer, so the blocks only name a particle, and the items are {@code builtin/entity} models on
 * vanilla's chest display transforms, drawn by the item renderer {@code ChestsClient} registers.
 */
public class ChestsBlockstateProvider extends BlockStateProvider {

    public ChestsBlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Constants.MOD_ID, exFileHelper);
    }

    @Override
    public String getName() {
        return "Assorted Chests block states";
    }

    @Override
    protected void registerStatesAndModels() {
        for (IRegistryObject<LockedChestBlock> b : ChestsBlocks.CHESTS.values()) {
            chest(b.get(), b.get().getStorageMaterial().getParticle(Constants.MOD_ID));
        }
    }

    private void chest(Block block, ResourceLocation particle) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        ModelFile model = this.models().getBuilder(name).texture("particle", particle);
        this.simpleBlock(block, model);
        // Vanilla's chest item is builtin/entity with the chest's display transforms; only the particle differs.
        this.itemModels().withExistingParent(name, this.mcLoc("item/chest")).texture("particle", particle);
    }
}
