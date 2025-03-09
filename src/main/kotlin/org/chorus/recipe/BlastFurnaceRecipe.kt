package org.chorus.recipe

import cn.nukkit.item.Item
import cn.nukkit.recipe.descriptor.DefaultDescriptor
import cn.nukkit.registry.RecipeRegistry
import java.util.List

class BlastFurnaceRecipe(recipeId: String?, result: Item, ingredient: Item) :
    SmeltingRecipe(
        recipeId
            ?: RecipeRegistry.computeRecipeId(
                List.of(result),
                List.of(DefaultDescriptor(ingredient)),
                RecipeType.BLAST_FURNACE
            )
    ) {
    constructor(result: Item, ingredient: Item) : this(null, result, ingredient)


    init {
        ingredients.add(DefaultDescriptor(ingredient.clone()))
        results.add(result.clone())
    }

    override fun match(input: Input): Boolean {
        return false
    }

    override val type: RecipeType
        get() = RecipeType.BLAST_FURNACE
}
