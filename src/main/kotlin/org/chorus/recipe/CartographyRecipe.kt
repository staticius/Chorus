package org.chorus.recipe

import cn.nukkit.item.Item
import cn.nukkit.network.protocol.types.RecipeUnlockingRequirement
import cn.nukkit.recipe.descriptor.DefaultDescriptor
import cn.nukkit.recipe.descriptor.ItemDescriptor
import cn.nukkit.registry.RecipeRegistry
import java.util.*

class CartographyRecipe(
    recipeId: String?,
    uuid: UUID?,
    priority: Int,
    result: Item,
    ingredients: List<ItemDescriptor>,
    recipeUnlockingRequirement: RecipeUnlockingRequirement?
) :
    ShapelessRecipe(
        recipeId
            ?: RecipeRegistry.computeRecipeId(
                java.util.List.of(result),
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
        null,
        priority,
        result,
        ingredients
    )

    constructor(recipeId: String?, uuid: UUID?, priority: Int, result: Item, ingredients: List<ItemDescriptor>) : this(
        recipeId,
        null,
        priority,
        result,
        ingredients,
        null
    )

    override val type: RecipeType
        get() = RecipeType.CARTOGRAPHY
}
