package cn.nukkit.recipe

import cn.nukkit.item.Item
import cn.nukkit.registry.RecipeRegistry
import java.util.List


class BrewingRecipe(recipeId: String?, input: Item, ingredient: Item, output: Item) :
    MixRecipe(
        recipeId
            ?: RecipeRegistry.computeRecipeIdWithItem(
                List.of(output),
                List.of(input, ingredient),
                RecipeType.BREWING
            ), input, ingredient, output
    ) {
    constructor(input: Item, ingredient: Item, output: Item) : this(null, input, ingredient, output)

    override val type: RecipeType
        get() = RecipeType.BREWING
}