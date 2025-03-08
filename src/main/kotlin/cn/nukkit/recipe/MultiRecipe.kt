package cn.nukkit.recipe

import cn.nukkit.item.Item
import cn.nukkit.recipe.descriptor.ItemDescriptor
import lombok.ToString
import java.util.*

@ToString
class MultiRecipe(val id: UUID) : Recipe {
    override val recipeId: String
        get() = id.toString()

    override val results: List<Item>
        get() = listOf()

    override val ingredients: List<ItemDescriptor>
        get() = listOf()

    override fun match(input: Input): Boolean {
        return true
    }

    override val type: RecipeType
        get() = RecipeType.MULTI
}
