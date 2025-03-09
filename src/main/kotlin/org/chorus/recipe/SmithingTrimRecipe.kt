package org.chorus.recipe

import cn.nukkit.recipe.descriptor.ItemDescriptor

/**
 * The type Smithing recipe for trim equipment.
 *
 * @author CoolLoong
 */
class SmithingTrimRecipe(
    id: String,
    base: ItemDescriptor?,
    addition: ItemDescriptor?,
    template: ItemDescriptor?,
    tag: String
) :
    BaseRecipe(id) {
    val tag: String

    init {
        results.clear()
        ingredients.add(template)
        ingredients.add(base)
        ingredients.add(addition)
        this.tag = tag
    }

    override fun match(input: Input): Boolean {
        return false
    }

    override val type: RecipeType
        get() = RecipeType.SMITHING_TRIM
}
