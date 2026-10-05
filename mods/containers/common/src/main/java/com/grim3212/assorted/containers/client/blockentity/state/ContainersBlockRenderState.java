package com.grim3212.assorted.containers.client.blockentity.state;

import com.grim3212.assorted.containers.client.model.ContainersModelState;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;

/**
 * Render state for the storage blocks drawn with a {@code BaseStorageModel}. Everything the submit
 * pass needs is copied from the block entity during extraction, including the facing.
 */
public class ContainersBlockRenderState extends BlockEntityRenderState {

    /** False when the block entity's block is not a storage block, in which case nothing is drawn. */
    public boolean renderModel;
    public Direction facing = Direction.SOUTH;
    public ContainersModelState model = ContainersModelState.CLOSED_UNLOCKED;
}
