package org.chorus.recipe

import org.chorus.item.Item
import org.chorus.recipe.descriptor.ItemDescriptor

/**
 * @author MagicDroidX (Nukkit Project)
 */
interface Recipe {
    @JvmField
    val recipeId: String

    @JvmField
    val results: List<Item>

    @JvmField
    val ingredients: List<ItemDescriptor>

    fun match(input: Input): Boolean

    fun fastCheck(vararg items: Item): Boolean {
        if (ingredients.size != items.size) return false
        for (item in items) {
            val b = ingredients.stream().anyMatch { i: ItemDescriptor -> i.match(item) }
            if (!b) return false
        }
        return true
    }

    @JvmField
    val type: RecipeType
}
