package org.chorus.recipe

import org.chorus.item.Item
import org.chorus.recipe.descriptor.DefaultDescriptor

abstract class MixRecipe(recipeId: String?, input: Item, ingredient: Item, output: Item?) :
    BaseRecipe(recipeId!!) {
    constructor(input: Item, ingredient: Item, output: Item?) : this(null, input, ingredient, output)

    init {
        ingredients.add(DefaultDescriptor(input))
        ingredients.add(DefaultDescriptor(ingredient))
        results.add(output)
    }

    val ingredient: Item
        get() = ingredients[1].toItem()

    val input: Item
        get() = ingredients[0].toItem()

    val result: Item
        get() = results[0]

    override fun match(input: Input): Boolean {
        return true
    }
}
