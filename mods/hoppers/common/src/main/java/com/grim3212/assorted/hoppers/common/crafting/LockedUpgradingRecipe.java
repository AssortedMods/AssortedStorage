package com.grim3212.assorted.hoppers.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.grim3212.assorted.lib.core.storage.IStorageMaterial;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.block.Block;

/**
 * A shaped recipe whose result keeps the data components of the storage item being upgraded; the
 * shape matching is {@link ShapedRecipe}'s.
 */
public class LockedUpgradingRecipe extends ShapedRecipe {

    /** The fields of vanilla's shaped recipe, which keeps them to itself. */
    public static final MapCodec<LockedUpgradingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(ShapedRecipe::getGroup),
                    CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(ShapedRecipe::category),
                    ShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.shape),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                    Codec.BOOL.optionalFieldOf("show_notification", true).forGetter(ShapedRecipe::showNotification)
            ).apply(instance, LockedUpgradingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, LockedUpgradingRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ShapedRecipe::getGroup,
            CraftingBookCategory.STREAM_CODEC, ShapedRecipe::category,
            ShapedRecipePattern.STREAM_CODEC, recipe -> recipe.shape,
            ItemStack.STREAM_CODEC, recipe -> recipe.result,
            ByteBufCodecs.BOOL, ShapedRecipe::showNotification,
            LockedUpgradingRecipe::new);

    public static final RecipeSerializer<LockedUpgradingRecipe> SERIALIZER = new RecipeSerializer<>() {
        @Override
        public MapCodec<LockedUpgradingRecipe> codec() {
            return MAP_CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, LockedUpgradingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    };

    private final ShapedRecipePattern shape;
    private final ItemStack result;

    public LockedUpgradingRecipe(String group, CraftingBookCategory category, ShapedRecipePattern shape, ItemStack result, boolean showNotification) {
        super(group, category, shape, result, showNotification);
        this.shape = shape;
        this.result = result;
    }

    @Override
    public RecipeSerializer<LockedUpgradingRecipe> getSerializer() {
        return HoppersRecipeSerializers.LOCKED_UPGRADING.get();
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack output = this.result.copy();

        ItemStack itemstack = ItemStack.EMPTY;

        for (int i = 0; i < input.size(); ++i) {
            ItemStack stack = input.getItem(i);

            if (!stack.isEmpty()) {
                // By interface, as the chest a hopper can be made from is Assorted Chests' block
                if (Block.byItem(stack.getItem()) instanceof IStorageMaterial) {
                    itemstack = stack;
                }
            }
        }

        // Carrying the upgraded item's stored contents, name and lock over is a matter of applying
        // its data component patch to the fresh result.
        if (!itemstack.isEmpty()) {
            output.applyComponents(itemstack.getComponentsPatch());
        }

        return output;
    }
}
