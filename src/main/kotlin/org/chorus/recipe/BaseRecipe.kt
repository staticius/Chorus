package org.chorus.recipe

import com.google.common.base.Objects
import org.chorus.item.Item
import org.chorus.recipe.descriptor.ItemDescriptor

abstract class BaseRecipe protected constructor(id: String) : Recipe {
    override val recipeId: String = id
    override val results: MutableList<Item> = ArrayList()
    override val ingredients: MutableList<ItemDescriptor> = ArrayList()

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is BaseRecipe) return false
        return Objects.equal(recipeId, o.recipeId)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(recipeId)
    }

    override fun toString(): String {
        return javaClass.simpleName + "{" +
                "id='" + this.recipeId + '\'' +
                '}'
    }
}
