package com.grim3212.assorted.crates.client.model.baked;

import com.grim3212.assorted.lib.client.model.baked.IDataAwareBakedModel;
import com.grim3212.assorted.lib.client.model.data.IBlockModelData;
import com.grim3212.assorted.crates.common.properties.CratesModelProperties;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Picks the locked or unlocked geometry from the model data the block entity publishes for its
 * position.
 */
public class LockedBakedModel implements IDataAwareBakedModel {

    private final BlockStateModelPart unlockedModel;
    private final BlockStateModelPart lockedModel;

    // An item has no block entity to read the lock from, so the controller's item names its unlocked model itself.

    public LockedBakedModel(BlockStateModelPart unlockedModel, BlockStateModelPart lockedModel) {
        this.unlockedModel = unlockedModel;
        this.lockedModel = lockedModel;
    }

    @Override
    public void collectParts(@NotNull RandomSource random, @NotNull IBlockModelData extraData, @NotNull List<BlockStateModelPart> output) {
        Boolean locked = extraData.getData(CratesModelProperties.IS_LOCKED);
        output.add(Boolean.TRUE.equals(locked) ? this.lockedModel : this.unlockedModel);
    }

    // Deprecated by NeoForge in favour of level/pos aware overloads that only exist in its patched
    // jar; vanilla still declares these abstract, so they have to be implemented here.
    @SuppressWarnings("deprecation")
    @Override
    public Material.Baked particleMaterial() {
        return this.unlockedModel.particleMaterial();
    }

    @SuppressWarnings("deprecation")
    @Override
    public @BakedQuad.MaterialFlags int materialFlags() {
        return this.unlockedModel.materialFlags() | this.lockedModel.materialFlags();
    }
}
