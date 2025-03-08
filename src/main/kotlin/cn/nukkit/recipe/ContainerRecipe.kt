package cn.nukkit.recipe

import cn.nukkit.item.Item
import cn.nukkit.item.ItemPotion
import cn.nukkit.registry.RecipeRegistry
import java.util.List

class ContainerRecipe(recipeId: String?, input: Item, ingredient: Item, output: Item) :
    MixRecipe(
        recipeId
            ?: RecipeRegistry.computeRecipeIdWithItem(
                List.of(output),
                List.of(input, ingredient),
                RecipeType.CONTAINER
            ),
        input, ingredient, output
    ) {
    constructor(input: Item, ingredient: Item, output: Item) : this(null, input, ingredient, output)

    override fun fastCheck(vararg items: Item): Boolean {
        if (items.size == 2) {
            if (items[1] is ItemPotion) {
                return items[0].equals(ingredient)
            }
        }
        return false
    }

    override val type: RecipeType
        get() = RecipeType.CONTAINER
}
