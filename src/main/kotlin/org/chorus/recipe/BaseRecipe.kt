package org.chorus.recipe

import cn.nukkit.item.Item
import cn.nukkit.recipe.descriptor.ItemDescriptor
import com.google.common.base.Objects
import com.google.common.base.Preconditions

abstract class BaseRecipe protected constructor(id: String) : Recipe {
    override val recipeId: String
    override val results: List<Item> = ArrayList()
    override val ingredients: List<ItemDescriptor> = ArrayList()

    init {
        Preconditions.checkNotNull(id)
        this.recipeId = id
    }

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
