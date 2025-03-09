package org.chorus.recipe

import cn.nukkit.item.Item
import cn.nukkit.recipe.descriptor.DefaultDescriptor
import cn.nukkit.registry.RecipeRegistry
import java.util.List

open class CampfireRecipe(recipeId: String?, result: Item, ingredient: Item) :
    SmeltingRecipe(
        recipeId
            ?: RecipeRegistry.computeRecipeId(
                List.of(result),
                List.of(DefaultDescriptor(ingredient)),
                RecipeType.CAMPFIRE
            )
    ) {
    constructor(result: Item, ingredient: Item) : this(null, result, ingredient)

    init {
        results.add(result)
        ingredients.add(DefaultDescriptor(ingredient))
    }

    override fun match(input: Input): Boolean {
        return false
    }

    override val type: RecipeType
        get() = RecipeType.CAMPFIRE
}
