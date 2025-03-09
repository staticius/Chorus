package org.chorus.recipe

import cn.nukkit.item.Item
import cn.nukkit.item.Item.clone
import cn.nukkit.network.protocol.types.RecipeUnlockingRequirement
import cn.nukkit.recipe.descriptor.DefaultDescriptor
import cn.nukkit.registry.RecipeRegistry
import java.util.*
import java.util.List

class StonecutterRecipe(
    recipeId: String?,
    uuid: UUID?,
    priority: Int,
    result: Item,
    ingredient: Item,
    recipeUnlockingRequirement: RecipeUnlockingRequirement?
) :
    CraftingRecipe(
        recipeId
            ?: RecipeRegistry.computeRecipeIdWithItem(
                List.of<Item>(result),
                List.of<Item>(ingredient),
                RecipeType.STONECUTTER
            ), priority, recipeUnlockingRequirement
    ) {
    constructor(result: Item, ingredient: Item) : this(null, 10, result, ingredient)

    constructor(recipeId: String?, priority: Int, result: Item, ingredient: Item) : this(
        recipeId,
        null,
        priority,
        result,
        ingredient
    )

    constructor(recipeId: String?, uuid: UUID?, priority: Int, result: Item, ingredient: Item) : this(
        recipeId,
        null,
        priority,
        result,
        ingredient,
        null
    )

    init {
        this.uuid = uuid
        results.add(result.clone())
        require(ingredient.getCount() >= 1) { "Recipe '" + recipeId + "' Ingredient amount was not 1 (value: " + ingredient.getCount() + ")" }
        ingredients.add(DefaultDescriptor(ingredient.clone()))
    }

    val result: Item
        get() = results.getFirst().clone()

    val ingredient: Item
        get() = ingredients.getFirst().toItem().clone()

    override fun match(input: Input): Boolean {
        return true
    }

    override val type: RecipeType
        get() = RecipeType.STONECUTTER
}
