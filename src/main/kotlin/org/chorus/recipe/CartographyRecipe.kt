package org.chorus.recipe

import org.chorus.item.Item
import org.chorus.network.protocol.types.RecipeUnlockingRequirement
import org.chorus.recipe.descriptor.DefaultDescriptor
import org.chorus.recipe.descriptor.ItemDescriptor
import org.chorus.registry.RecipeRegistry
import java.util.*

class CartographyRecipe(
    recipeId: String?,
    uuid: UUID,
    priority: Int,
    result: Item,
    ingredients: List<ItemDescriptor>,
    recipeUnlockingRequirement: RecipeUnlockingRequirement?
) :
    ShapelessRecipe(
        recipeId
            ?: RecipeRegistry.computeRecipeId(
                listOf(result),
                ingredients,
                RecipeType.CARTOGRAPHY
            ), uuid, priority, result, ingredients, recipeUnlockingRequirement
    ) {
    constructor(result: Item, ingredients: Collection<Item>) : this(null, 10, result, ingredients)

    constructor(recipeId: String?, priority: Int, result: Item, ingredients: Collection<Item>) : this(
        recipeId,
        priority,
        result,
        ingredients.stream().map<ItemDescriptor> { item: Item ->
            DefaultDescriptor(item)
        }.toList()
    )

    constructor(recipeId: String?, priority: Int, result: Item, ingredients: List<ItemDescriptor>) : this(
        recipeId,
        UUID.randomUUID(),
        priority,
        result,
        ingredients
    )

    constructor(recipeId: String?, uuid: UUID, priority: Int, result: Item, ingredients: List<ItemDescriptor>) : this(
        recipeId,
        uuid,
        priority,
        result,
        ingredients,
        null
    )

    override val type: RecipeType
        get() = RecipeType.CARTOGRAPHY
}
