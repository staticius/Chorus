package org.chorus.network.protocol.types.itemstack.request.action

import org.chorus.recipe.descriptor.ItemDescriptor
import lombok.Value

/**
 * AutoCraftRecipeStackRequestActionData is sent by the client similarly to the CraftRecipeStackRequestActionData. The
 * only difference is that the recipe is automatically created and crafted by shift clicking the recipe book.
 */
@Value
class AutoCraftRecipeAction : RecipeItemStackRequestAction {
    override var recipeNetworkId: Int = 0
    override var numberOfRequestedCrafts: Int = 0
    var timesCrafted: Int = 0
    var ingredients: List<ItemDescriptor>? = null

    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.CRAFT_RECIPE_AUTO
}
