package com.grim3212.assorted.barrels.client.data;

import com.grim3212.assorted.barrels.Constants;
import com.grim3212.assorted.barrels.client.BarrelsClient;
import com.grim3212.assorted.barrels.common.block.BarrelsBlocks;
import com.grim3212.assorted.lib.client.data.LockedModelBuilder;
import com.grim3212.assorted.lib.core.storage.barrel.LockedBarrelBlock;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/** Block states, block models and block item models for the barrels. */
public class BarrelsBlockstateProvider extends BlockStateProvider {

    public BarrelsBlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Constants.MOD_ID, exFileHelper);
    }

    @Override
    public String getName() {
        return "Assorted Barrels block states";
    }

    @Override
    protected void registerStatesAndModels() {
        for (IRegistryObject<LockedBarrelBlock> b : BarrelsBlocks.BARRELS.values()) {
            materialBarrel(b.get());
        }
    }

    private void materialBarrel(LockedBarrelBlock b) {
        String name = BuiltInRegistries.BLOCK.getKey(b).getPath();
        String material = b.getStorageMaterial().toString();
        ResourceLocation side = modLoc("block/barrels/" + material + "/barrel_side");
        ResourceLocation bottom = modLoc("block/barrels/" + material + "/barrel_bottom");

        ModelFile unlocked = models().cubeBottomTop(name + "_unlocked", side, bottom, modLoc("block/barrels/" + material + "/barrel_top"));
        ModelFile locked = models().cubeBottomTop(name + "_locked", side, bottom, modLoc("block/barrels/" + material + "/locked_barrel_top"));
        ModelFile open = models().cubeBottomTop(name + "_open", side, bottom, modLoc("block/barrels/" + material + "/barrel_top_open"));

        // The closed barrel is the assortedbarrels:locked loader model, which picks its locked half
        // from the block entity's model data. block/block supplies the display transforms and no geometry.
        BlockModelBuilder closed = models().withExistingParent(name, mcLoc("block/block"))
                .texture("particle", side)
                .customLoader(LockedModelBuilder.begin(BarrelsClient.LOCKED_MODEL_LOADER))
                .unlockedModel(unlocked.getLocation())
                .lockedModel(locked.getLocation())
                .end();

        // The barrel's own rotation: down is x 180, up is x 0, a side is x 90 turned to face the way it points.
        getVariantBuilder(b).forAllStates(state -> {
            Direction dir = state.getValue(LockedBarrelBlock.FACING);
            return ConfiguredModel.builder()
                    .modelFile(state.getValue(LockedBarrelBlock.OPEN) ? open : closed)
                    .rotationX(dir == Direction.DOWN ? 180 : dir == Direction.UP ? 0 : 90)
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .build();
        });

        // An item has no block entity for the loader to read, so the item model picks the locked
        // child itself, on the stack's lock.
        itemModels().withExistingParent(name, unlocked.getLocation())
                .override().predicate(BarrelsClient.LOCKED_PROPERTY_ID, 1.0F).model(locked).end();
    }
}
