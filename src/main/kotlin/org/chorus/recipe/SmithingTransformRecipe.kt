package org.chorus.recipe

import org.chorus.item.Item
import org.chorus.recipe.descriptor.ItemDescriptor

/**
 * The type Smithing recipe for upgrade equipment.
 *
 * @author joserobjr
 * @since 2020 -09-28
 */
class SmithingTransformRecipe(
    recipeId: String,
    result: Item?,
    base: ItemDescriptor?,
    addition: ItemDescriptor?,
    template: ItemDescriptor?
) :
    BaseRecipe(recipeId) {
    init {
        ingredients.add(base)
        ingredients.add(addition)
        ingredients.add(template)
        results.add(result)
    }

    override fun match(input: Input): Boolean {
        return false
    }

    val result: Item
        get() = results.first()

    override val type: RecipeType
        get() = RecipeType.SMITHING_TRANSFORM

    val base: ItemDescriptor
        get() = ingredients.first()

    val addition: ItemDescriptor
        get() = ingredients[1]

    val template: ItemDescriptor
        get() = ingredients[2]
}
