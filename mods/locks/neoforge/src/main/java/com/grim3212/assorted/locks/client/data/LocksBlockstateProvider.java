package com.grim3212.assorted.locks.client.data;

import com.grim3212.assorted.lib.client.data.LockedModelBuilder;
import com.grim3212.assorted.lib.core.storage.barrel.LockedBarrelBlock;
import com.grim3212.assorted.locks.Constants;
import com.grim3212.assorted.locks.client.model.LocksStorageModels;
import com.grim3212.assorted.locks.common.block.LocksBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Block states, block models and the block items' models. {@link LocksItemModelProvider} models
 * the other items, so the two never write the same file.
 */
public class LocksBlockstateProvider extends BlockStateProvider {

    public LocksBlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Constants.MOD_ID, exFileHelper);
    }

    @Override
    public String getName() {
        return "Assorted Locks block states";
    }

    @Override
    protected void registerStatesAndModels() {
        locksmithWorkbench();

        for (Block door : LocksBlocks.lockedDoors()) {
            door((DoorBlock) door);
        }

        lockedChest(LocksBlocks.LOCKED_ENDER_CHEST.get(), mcLoc("block/obsidian"));
        lockedChest(LocksBlocks.LOCKED_CHEST.get(), mcLoc("block/oak_planks"));
        lockedShulkerBox();
        lockedBarrel();
        lockedHopper();
    }

    private static String name(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    /**
     * Drawn by its block entity renderer, so the block only names a particle and the item is a
     * builtin/entity model on vanilla's chest display transforms, drawn by the item renderer
     * {@code LocksClient} registers for it.
     */
    private void lockedChest(Block block, ResourceLocation particle) {
        particleOnlyBlock(block, particle);
        this.itemModels().withExistingParent(name(block), mcLoc("item/chest")).texture("particle", particle);
    }

    /** As {@link #lockedChest}, on vanilla's shulker box display transforms, which its item renderer is laid out for. */
    private void lockedShulkerBox() {
        Block block = LocksBlocks.LOCKED_SHULKER_BOX.get();
        ResourceLocation particle = mcLoc("block/shulker_box");
        particleOnlyBlock(block, particle);
        this.itemModels().withExistingParent(name(block), mcLoc("item/template_shulker_box")).texture("particle", particle);
    }

    private void particleOnlyBlock(Block block, ResourceLocation texture) {
        this.simpleBlock(block, this.models().getBuilder(name(block)).texture("particle", texture));
    }

    /**
     * The closed barrel is the locked loader model and the open one is plain. The item picks between the locked and
     * unlocked models itself, through an override on the stack's lock, as it has no block entity for the loader to read.
     */
    private void lockedBarrel() {
        Block b = LocksBlocks.LOCKED_BARREL.get();
        String name = name(b);
        ResourceLocation side = mcLoc("block/barrel_side");
        ResourceLocation bottom = mcLoc("block/barrel_bottom");

        ModelFile unlocked = this.models().cubeBottomTop(name + "_unlocked", side, bottom, mcLoc("block/barrel_top"));
        ModelFile locked = this.models().cubeBottomTop(name + "_locked", side, bottom, modLoc("block/barrels/locked_barrel_top"));
        ModelFile open = this.models().cubeBottomTop(name + "_open", side, bottom, mcLoc("block/barrel_top_open"));
        ModelFile closed = lockedModel(name, unlocked.getLocation(), locked.getLocation(), side);

        // Vanilla's barrel rotations: the top faces the block's FACING.
        this.getVariantBuilder(b).forAllStates(state -> {
            Direction facing = state.getValue(LockedBarrelBlock.FACING);
            int x = facing == Direction.UP ? 0 : facing == Direction.DOWN ? 180 : 90;
            int y = facing.getAxis().isVertical() ? 0 : ((int) facing.toYRot() + 180) % 360;
            return ConfiguredModel.builder().modelFile(state.getValue(LockedBarrelBlock.OPEN) ? open : closed).rotationX(x).rotationY(y).build();
        });

        this.itemModels().getBuilder(name).parent(unlocked)
                .override().predicate(LocksStorageModels.LOCKED_PROPERTY_ID, 1.0F).model(locked).end();
    }

    /**
     * The unlocked halves are vanilla's own hopper models, and the item is a flat sprite with the padlock drawn on.
     * Each child names the cutout layer vanilla draws its hopper in, which the locked model reads from the child.
     */
    private void lockedHopper() {
        Block b = LocksBlocks.LOCKED_HOPPER.get();
        String name = name(b);
        ResourceLocation particle = mcLoc("block/hopper_outside");

        ModelFile unlocked = this.models().withExistingParent(name + "_unlocked", mcLoc("block/hopper")).renderType("cutout_mipped");
        ModelFile unlockedSide = this.models().withExistingParent(name + "_unlocked_side", mcLoc("block/hopper_side")).renderType("cutout_mipped");
        ModelFile locked = this.models().withExistingParent(name + "_locked", modLoc("block/template_hopper")).renderType("cutout_mipped");
        ModelFile lockedSide = this.models().withExistingParent(name + "_locked_side", modLoc("block/template_hopper_side")).renderType("cutout_mipped");

        ModelFile down = lockedModel(name, unlocked.getLocation(), locked.getLocation(), particle);
        ModelFile side = lockedModel(name + "_side", unlockedSide.getLocation(), lockedSide.getLocation(), particle);

        this.getVariantBuilder(b).forAllStates(state -> {
            Direction facing = state.getValue(BlockStateProperties.FACING_HOPPER);
            if (facing == Direction.DOWN) {
                return ConfiguredModel.builder().modelFile(down).build();
            }
            return ConfiguredModel.builder().modelFile(side).rotationY(((int) facing.toYRot() + 180) % 360).build();
        });

        this.itemModels().basicItem(b.asItem());
    }

    /** A model whose whole body is the locked loader block, which replaces the geometry outright. */
    private BlockModelBuilder lockedModel(String name, ResourceLocation unlocked, ResourceLocation locked, ResourceLocation particle) {
        // block/block gives a block item its display transforms and no geometry of its own.
        return this.models().withExistingParent(name, mcLoc("block/block"))
                .customLoader(LockedModelBuilder.begin(LocksStorageModels.LOCKED_MODEL_LOADER)).unlockedModel(unlocked).lockedModel(locked).end()
                .texture("particle", particle);
    }

    private void locksmithWorkbench() {
        Block b = LocksBlocks.LOCKSMITH_WORKBENCH.get();
        ResourceLocation front = modLoc("block/locksmith_front");
        ResourceLocation side = modLoc("block/locksmith_side");
        ModelFile model = this.models().cube(name(b), mcLoc("block/oak_planks"), modLoc("block/locksmith_top"), front, side, side, front).texture("particle", front);
        this.simpleBlockWithItem(b, model);
    }

    /**
     * Which other locked door a door takes its textures from. A waxed copper door looks exactly like
     * the unwaxed one of the same oxidation stage - vanilla shares those textures too - so only the
     * four unwaxed locked copper doors ship a png.
     */
    private static Map<Block, Block> doorTextureSources() {
        Map<Block, Block> sources = new HashMap<>();
        for (int i = 0; i < LocksBlocks.COPPER_DOORS.size(); i++) {
            sources.put(LocksBlocks.VANILLA_DOORS.get(LocksBlocks.WAXED_COPPER_DOORS.get(i)).get(), LocksBlocks.VANILLA_DOORS.get(LocksBlocks.COPPER_DOORS.get(i)).get());
        }
        return sources;
    }

    /**
     * A locked door's block state and models, in the cutout layer vanilla draws its doors in. The
     * doors have no item, so no item model is written.
     */
    private void door(DoorBlock door) {
        String texture = "block/" + name(doorTextureSources().getOrDefault(door, door));
        this.doorBlockWithRenderType(door, modLoc(texture + "_bottom"), modLoc(texture + "_top"), "cutout");
    }
}
