package com.grim3212.assorted.hoppers.client.data;

import com.grim3212.assorted.hoppers.Constants;
import com.grim3212.assorted.hoppers.client.HoppersClient;
import com.grim3212.assorted.hoppers.common.block.HoppersBlocks;
import com.grim3212.assorted.lib.client.data.LockedModelBuilder;
import com.grim3212.assorted.lib.core.storage.hopper.LockedHopperBlock;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * Block states, block models and the hopper items' models. Every hopper switches between its
 * unlocked and locked model through the {@code assortedhoppers:locked} loader.
 */
public class HoppersBlockstateProvider extends BlockStateProvider {

    private static final ResourceLocation VANILLA_HOPPER = ResourceLocation.withDefaultNamespace("block/hopper");
    private static final ResourceLocation VANILLA_HOPPER_SIDE = ResourceLocation.withDefaultNamespace("block/hopper_side");
    private static final ModelFile TEMPLATE_HOPPER = new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "block/template_hopper"));
    private static final ModelFile TEMPLATE_HOPPER_SIDE = new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "block/template_hopper_side"));
    private static final ModelFile BLOCK = new ModelFile.UncheckedModelFile(ResourceLocation.withDefaultNamespace("block/block"));

    /**
     * A hopper's rim has see-through pixels, which 1.21.1 only cuts out when the model names the layer; vanilla's
     * own hopper draws in this one.
     */
    private static final String RENDER_TYPE = "cutout_mipped";

    public HoppersBlockstateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Assorted Hoppers block states";
    }

    @Override
    protected void registerStatesAndModels() {
        for (IRegistryObject<LockedHopperBlock> b : HoppersBlocks.HOPPERS.values()) {
            materialHopper(b.get());
        }
    }

    private void materialHopper(LockedHopperBlock b) {
        String name = BuiltInRegistries.BLOCK.getKey(b).getPath();
        String material = b.getStorageMaterial().toString();
        ResourceLocation outside = texture("block/hoppers/" + material + "/hopper_outside");
        ResourceLocation top = texture("block/hoppers/" + material + "/hopper_top");
        ResourceLocation inside = texture("block/hoppers/" + material + "/hopper_inside");
        ResourceLocation lockedOutside = texture("block/hoppers/" + material + "/locked_hopper_outside");

        BlockModelBuilder unlocked = textured(this.models().withExistingParent(name + "_unlocked", VANILLA_HOPPER), outside, top, inside);
        BlockModelBuilder locked = textured(this.models().getBuilder(name + "_locked").parent(TEMPLATE_HOPPER), outside, top, inside).texture("topsides", lockedOutside);
        BlockModelBuilder unlockedSide = textured(this.models().withExistingParent(name + "_unlocked_side", VANILLA_HOPPER_SIDE), outside, top, inside);
        BlockModelBuilder lockedSide = textured(this.models().getBuilder(name + "_locked_side").parent(TEMPLATE_HOPPER_SIDE), outside, top, inside).texture("topsides", lockedOutside);

        ModelFile down = lockedModel(name, unlocked, locked, outside);
        ModelFile side = lockedModel(name + "_side", unlockedSide, lockedSide, outside);

        // ENABLED is not dispatched on; the rotations are vanilla's own hopper dispatch.
        this.getVariantBuilder(b).forAllStatesExcept(state -> {
            Direction facing = state.getValue(BlockStateProperties.FACING_HOPPER);
            if (facing == Direction.DOWN) {
                return ConfiguredModel.builder().modelFile(down).build();
            }
            return ConfiguredModel.builder().modelFile(side).rotationY(((int) facing.toYRot() + 180) % 360).build();
        }, LockedHopperBlock.ENABLED);

        // A hopper's item is a flat sprite, not its block model, as it always was.
        this.itemModels().basicItem(b.asItem());
    }

    private static BlockModelBuilder textured(BlockModelBuilder model, ResourceLocation outside, ResourceLocation top, ResourceLocation inside) {
        return model.texture("particle", outside).texture("top", top).texture("side", outside).texture("inside", inside).renderType(RENDER_TYPE);
    }

    /**
     * A model whose whole body is the {@code assortedhoppers:locked} loader block, which picks one of the two
     * children. {@code block/block} gives it the standard block display transforms and no geometry.
     */
    private ModelFile lockedModel(String name, ModelFile unlocked, ModelFile locked, ResourceLocation particle) {
        return this.models().getBuilder(name).parent(BLOCK).texture("particle", particle)
                .customLoader(LockedModelBuilder.begin(HoppersClient.LOCKED_MODEL_LOADER)).unlockedModel(unlocked.getLocation()).lockedModel(locked.getLocation()).end();
    }

    private static ResourceLocation texture(String path) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path);
    }
}
