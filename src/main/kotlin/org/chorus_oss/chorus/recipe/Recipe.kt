package org.chorus_oss.chorus.recipe

import org.chorus_oss.chorus.item.Item
import org.chorus_oss.chorus.recipe.descriptor.ItemDescriptor


interface Recipe {
    val recipeId: String

    val results: List<Item>

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

    val type: RecipeType
}
