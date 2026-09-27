package com.grim3212.assorted.locks.client.data;

import com.grim3212.assorted.locks.Constants;
import com.grim3212.assorted.locks.common.block.LocksBlocks;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Block states and block models. This owns every block and block item, and
 * {@link LocksItemModelProvider} owns the rest, so the two never write the same file.
 */
public class LocksBlockstateProvider extends ModelProvider {

    public LocksBlockstateProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    public String getName() {
        return "Assorted Locks block states";
    }

    /**
     * Only the block items belong here; every other item is {@link LocksItemModelProvider}'s, so
     * the two providers never write the same file.
     */
    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return super.getKnownItems().filter(holder -> holder.value() instanceof BlockItem);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        locksmithWorkbench(blockModels);

        for (Block door : LocksBlocks.lockedDoors()) {
            door(blockModels, door);
        }
    }

    private void locksmithWorkbench(BlockModelGenerators blockModels) {
        Block b = LocksBlocks.LOCKSMITH_WORKBENCH.get();
        TextureMapping textures = new TextureMapping()
                .put(TextureSlot.PARTICLE, texture("block/locksmith_front"))
                .put(TextureSlot.DOWN, new Material(Identifier.parse("block/oak_planks")))
                .put(TextureSlot.UP, texture("block/locksmith_top"))
                .put(TextureSlot.NORTH, texture("block/locksmith_front"))
                .put(TextureSlot.SOUTH, texture("block/locksmith_side"))
                .put(TextureSlot.EAST, texture("block/locksmith_side"))
                .put(TextureSlot.WEST, texture("block/locksmith_front"));

        MultiVariant model = BlockModelGenerators.plainVariant(ModelTemplates.CUBE.create(b, textures, blockModels.modelOutput));
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(b, model));
    }

    /**
     * Which other locked door a door takes its textures from. A waxed copper door looks exactly like
     * the unwaxed one of the same oxidation stage - vanilla shares those textures too - so only the
     * four unwaxed locked copper doors ship a png.
     */
    private static final Map<Block, Block> DOOR_TEXTURE_SOURCE = new HashMap<>();

    static {
        Blocks.COPPER_DOOR.zipUnwaxedWaxed((unwaxed, waxed) ->
                DOOR_TEXTURE_SOURCE.put(LocksBlocks.VANILLA_DOORS.get(waxed).get(), LocksBlocks.VANILLA_DOORS.get(unwaxed).get()));
    }

    /**
     * A locked door's block state and models. The doors have no item, so vanilla's {@code
     * createDoor}, which also writes an item model, cannot be used.
     */
    private void door(BlockModelGenerators blockModels, Block door) {
        TextureMapping mapping = TextureMapping.door(DOOR_TEXTURE_SOURCE.getOrDefault(door, door));
        MultiVariant bottomLeft = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_BOTTOM_LEFT.create(door, mapping, blockModels.modelOutput));
        MultiVariant bottomLeftOpen = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_BOTTOM_LEFT_OPEN.create(door, mapping, blockModels.modelOutput));
        MultiVariant bottomRight = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_BOTTOM_RIGHT.create(door, mapping, blockModels.modelOutput));
        MultiVariant bottomRightOpen = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_BOTTOM_RIGHT_OPEN.create(door, mapping, blockModels.modelOutput));
        MultiVariant topLeft = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_TOP_LEFT.create(door, mapping, blockModels.modelOutput));
        MultiVariant topLeftOpen = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_TOP_LEFT_OPEN.create(door, mapping, blockModels.modelOutput));
        MultiVariant topRight = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_TOP_RIGHT.create(door, mapping, blockModels.modelOutput));
        MultiVariant topRightOpen = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_TOP_RIGHT_OPEN.create(door, mapping, blockModels.modelOutput));

        blockModels.blockStateOutput.accept(BlockModelGenerators.createDoor(door, bottomLeft, bottomLeftOpen, bottomRight, bottomRightOpen, topLeft, topLeftOpen, topRight, topRightOpen));
    }

    private static Material texture(String path) {
        return new Material(Identifier.fromNamespaceAndPath(Constants.MOD_ID, path));
    }
}
