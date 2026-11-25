package com.grim3212.assorted.crates.client.blockentity.state;

import com.grim3212.assorted.crates.api.crates.CrateLayout;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;

/**
 * What one frame of a crate draws, read off the crate before anything is drawn so the item, upgrade
 * and lock passes all see the same thing and none of them reach back into the crate.
 */
public class CrateRenderState {

    public Direction facing = Direction.NORTH;
    public CrateLayout layout = CrateLayout.SINGLE;
    /** The stored item of each displayed slot, in slot order. */
    public List<ItemStack> items = Collections.emptyList();
    /** The stored item's display rotation, in slot order. */
    public float[] itemRotations = new float[0];
    /** Whether each slot has its item lock set, in slot order. */
    public boolean[] slotLocked = new boolean[0];
    /** How many items each slot holds, in slot order. */
    public int[] slotAmounts = new int[0];
    /** How many items each slot can hold, in slot order. */
    public int[] slotCapacities = new int[0];
    public boolean anySlotLocked;
    /** The light at the crate. */
    public int lightCoords;
    /** The light the stored items and their amounts draw with: full bright with a glow upgrade. */
    public int itemLightCoords;
    public int overlayCoords;
    /** Seeds the stored items' models, as the item frame and the crate's own position used to. */
    public int seed;

    /**
     * Copies of the unique upgrade stacks whose renderers should run.
     */
    public List<ItemStack> upgrades = Collections.emptyList();
}
