package com.grim3212.assorted.crates.api;

import com.google.common.collect.Maps;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.ILoaderRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Finds compacting recipes, following the approach of StorageDrawers and FunctionStorage. Needs a
 * {@link ServerLevel}, the only side with a recipe manager.
 */

public class CompactingHelper {

    private final ServerLevel level;

    public CompactingHelper(ServerLevel level) {
        this.level = level;
    }

    public List<Match> findMatches(ItemStack stack, int numTiers) {
        List<Match> matches = new ArrayList<>();
        // Add initial match for the stack itself
        matches.add(new Match(stack, 1));
        // Try to find an upperTier
        Match match = findUpperTier(stack);
        if (!match.getItem().isEmpty()) {
            // We found an upperTier add to front of list
            matches.add(0, match);
            // Are we at the topMost tier
            if (matches.size() < numTiers) {
                // If we are not, search again
                match = findUpperTier(match.getItem());
                if (!match.getItem().isEmpty()) {
                    // Set the current result number of required items to be multiplied by the
                    // number of the previous in the tier
                    // as there is 1 tier difference between the two
                    match.setNumRequired(match.getNumRequired() * matches.get(0).getNumRequired());
                    // Found higher tier and to front of list
                    matches.add(0, match);
                }
            }
        }
        boolean keepSearching = true;
        // Until we cannot find a lower tier anymore keep searching
        while (keepSearching && matches.size() < numTiers) {
            // Look for a lower tier for the first element in the list of matches
            match = findLowerTier(matches.get(matches.size() - 1).getItem());
            if (!match.getItem().isEmpty()) {
                // If we found a match we need to adjust all the other results
                // This is because this lower tier will now be what all the amounts are now
                // based off of
                for (Match prevMatch : matches) {
                    prevMatch.setNumRequired(prevMatch.getNumRequired() * match.getNumRequired());
                }
                match.setNumRequired(1);
                // Add the new lower tier to the first index
                matches.add(match);
            } else {
                keepSearching = false;
            }
        }
        // If we do not have a full amount of tiers then fill the remaining spots with
        // an empty match
        while (matches.size() < numTiers) {
            matches.add(new Match(ItemStack.EMPTY, 1));
        }
        // Modify each match to be the real number of requirements
        matches.stream().filter(x -> x.getItem().getCount() > 0).forEach(x -> x.setNumRequired(x.getNumRequired() / x.getItem().getCount()));
        return matches;
    }

    private Match findUpperTier(ItemStack stack) {
        List<ItemStack> outputs = new ArrayList<>();
        // Try to check for 3x3 recipes first
        List<ItemStack> matchingStacks = findMatchingStacks(filledInput(3, stack));
        int sizeCheck = matchingStacks.isEmpty() ? 4 : 9;
        if (matchingStacks.isEmpty()) {
            // If we did not find a match at 3x3 then we search at 2x2
            matchingStacks = findMatchingStacks(filledInput(2, stack));
        }

        if (stack.is(CratesTags.Items.CRAFTING_OVERRIDE)) {
            outputs = matchingStacks;
        } else if (!matchingStacks.isEmpty()) {
            // If we were able to find a matchingStack iterate
            for (ItemStack match : matchingStacks) {
                // Fill a single slot input with the current item we are checking
                for (ItemStack reverseMatch : findMatchingStacks(filledInput(1, match))) {
                    if (reverseMatch.getCount() != sizeCheck || !ItemStack.isSameItemSameComponents(reverseMatch, stack)) {
                        continue;
                    }
                    outputs.add(match);
                }
            }
        }

        ItemStack same = findSameItems(stack, outputs);
        if (!same.isEmpty()) {
            return new Match(same, sizeCheck);
        }
        if (!outputs.isEmpty()) {
            return new Match(outputs.get(0), sizeCheck);
        }

        // Fallback
        return new Match(ItemStack.EMPTY, 0);
    }

    /** Finds the recipe one tier down, e.g. an iron ingot into nine iron nuggets. */
    private Match findLowerTier(ItemStack stack) {
        // For each option we keep track of the amount it takes to craft the stack given
        Map<ItemStack, Integer> itemOptions = Maps.newLinkedHashMap();

        for (CraftingRecipe craftingRecipe : craftingRecipes().toList()) {
            ItemStack output = resultOf(craftingRecipe);
            // If the output is not this item check the next recipe
            if (!ItemStack.isSameItemSameComponents(stack, output))
                continue;

            // Look for a match
            // A shaped recipe lists its blank cells as empty ingredients; only the filled ones count.
            List<Ingredient> ingredients = craftingRecipe.getIngredients().stream().filter(x -> !x.isEmpty()).toList();
            ItemStack match = tryMatch(stack, ingredients);
            if (!match.isEmpty()) {
                int recipeSize = ingredients.size();
                if (stack.is(CratesTags.Items.CRAFTING_OVERRIDE)) {
                    itemOptions.put(match, recipeSize);
                }

                if (stack.is(CratesTags.Items.ONE_TO_ONE_CRAFTING_OVERRIDE)) {
                    itemOptions.put(match, 1);
                }

                // Look through each recipe to return a list of stacks that match the output
                // item
                List<ItemStack> matchStacks = findMatchingStacks(filledInput(1, output));
                for (ItemStack matchStack : matchStacks) {
                    if (ItemStack.isSameItemSameComponents(match, matchStack) && matchStack.getCount() == recipeSize) {
                        itemOptions.put(match, recipeSize);
                        break;
                    }
                }
            }
        }
        // Look for same items given the list of options we have generated
        ItemStack same = findSameItems(stack, new ArrayList<>(itemOptions.keySet()));
        if (!same.isEmpty()) {
            return new Match(same, itemOptions.get(same));
        }
        if (!itemOptions.isEmpty()) {
            // If we could not find any similar items then lets return the first option
            Entry<ItemStack, Integer> firstOption = itemOptions.entrySet().iterator().next();
            return new Match(firstOption.getKey(), firstOption.getValue());
        }

        // Fallback
        return new Match(ItemStack.EMPTY, 0);
    }

    /**
     * A square crafting grid of the given size with every slot holding a copy of the given stack.
     */
    private static CraftingInput filledInput(int size, ItemStack stack) {
        return CraftingInput.of(size, size, Collections.nCopies(size * size, stack.copy()));
    }

    private Stream<CraftingRecipe> craftingRecipes() {
        return this.level.getRecipeManager().getRecipes().stream().map(RecipeHolder::value).filter(CraftingRecipe.class::isInstance).map(CraftingRecipe.class::cast);
    }

    private ItemStack resultOf(CraftingRecipe recipe) {
        return recipe.getResultItem(this.level.registryAccess());
    }

    private List<ItemStack> findMatchingStacks(CraftingInput crafting) {
        if (crafting.isEmpty()) {
            return List.of();
        }

        return craftingRecipes().filter(x -> x.matches(crafting, this.level)).map(r -> r.assemble(crafting, this.level.registryAccess())).filter(i -> !i.isEmpty()).collect(Collectors.toList());
    }

    private ItemStack findSameItems(ItemStack stack, List<ItemStack> ingredientItems) {
        ILoaderRegistry<Item> itemRegistry = Services.PLATFORM.getRegistry(Registries.ITEM);
        ResourceLocation stackKey = itemRegistry.getRegistryName(stack.getItem());
        if (stackKey != null) {
            ItemStack firstMatch = ingredientItems.stream().filter(x -> {
                ResourceLocation optionKey = itemRegistry.getRegistryName(x.getItem());
                return optionKey != null && stackKey.getNamespace().equals(optionKey.getNamespace());
            }).findFirst().orElse(ItemStack.EMPTY);

            if (firstMatch != ItemStack.EMPTY) {
                return firstMatch;
            }
        }
        return ingredientItems.isEmpty() ? ItemStack.EMPTY : ingredientItems.get(0);
    }

    private ItemStack tryMatch(ItemStack stack, List<Ingredient> ingredients) {
        if (ingredients.size() != 9 && ingredients.size() != 4)
            return ItemStack.EMPTY;

        Ingredient refIngredient = ingredients.get(0);
        List<ItemStack> refMatchingStacks = Arrays.asList(refIngredient.getItems());
        if (refMatchingStacks.isEmpty())
            return ItemStack.EMPTY;

        for (int i = 1; i < ingredients.size(); i++) {
            Ingredient curIngredient = ingredients.get(i);

            boolean hasMatch = refMatchingStacks.stream().anyMatch(x -> !x.isEmpty() && curIngredient.test(x));

            if (!hasMatch) {
                return ItemStack.EMPTY;
            }
        }

        ItemStack match = findSameItems(stack, refMatchingStacks);
        return match.isEmpty() ? refMatchingStacks.get(0) : match;
    }

    public static class Match {
        private final ItemStack item;
        // This is the number of required items based off of the lowest tier
        private int numRequired;

        public Match(ItemStack item, int numRequired) {
            this.item = item;
            this.numRequired = numRequired;
        }

        public ItemStack getItem() {
            return item;
        }

        public int getNumRequired() {
            return numRequired;
        }

        public void setNumRequired(int numRequired) {
            this.numRequired = numRequired;
        }

        @Override
        public String toString() {
            return this.item.getItem().getDescriptionId() + ", " + this.numRequired;
        }
    }

}
