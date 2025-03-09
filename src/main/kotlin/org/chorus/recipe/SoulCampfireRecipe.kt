package org.chorus.recipe

import org.chorus.item.Item
import org.chorus.recipe.descriptor.DefaultDescriptor
import org.chorus.registry.RecipeRegistry
import java.util.List

class SoulCampfireRecipe(recipeId: String?, result: Item, ingredient: Item) :
    CampfireRecipe(
        recipeId
            ?: RecipeRegistry.computeRecipeId(
                List.of(result),
                List.of(DefaultDescriptor(ingredient)),
                RecipeType.SOUL_CAMPFIRE
            ), result, ingredient
    ) {
    constructor(result: Item, ingredient: Item) : this(null, result, ingredient)

    override val type: RecipeType
        get() = RecipeType.SOUL_CAMPFIRE
}
