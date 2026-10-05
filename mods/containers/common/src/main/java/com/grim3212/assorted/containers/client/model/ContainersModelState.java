package com.grim3212.assorted.containers.client.model;

/**
 * Per-submission state for the storage entity models. A model is set up later, once per submission,
 * so anything that varies per block has to travel here.
 *
 * @param doorAngle    The door/lid opening angle, in degrees.
 * @param renderHandle Whether to draw the handle ({@code true}) or the padlock ({@code false}).
 */
public record ContainersModelState(float doorAngle, boolean renderHandle) {

    /** A closed, unlocked model - what the item renderers draw. */
    public static final ContainersModelState CLOSED_UNLOCKED = new ContainersModelState(0.0F, true);

    /** A closed, locked model - for the storage blocks that are always locked. */
    public static final ContainersModelState CLOSED_LOCKED = new ContainersModelState(0.0F, false);
}
