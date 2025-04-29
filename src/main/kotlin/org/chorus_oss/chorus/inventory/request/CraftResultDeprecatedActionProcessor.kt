package org.chorus_oss.chorus.inventory.request

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.network.protocol.types.itemstack.request.action.CraftResultsDeprecatedAction
import org.chorus_oss.chorus.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import org.chorus_oss.chorus.recipe.Recipe
import org.chorus_oss.chorus.recipe.RecipeType

class CraftResultDeprecatedActionProcessor : ItemStackRequestActionProcessor<CraftResultsDeprecatedAction> {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.CRAFT_RESULTS_DEPRECATED

    override fun handle(
        action: CraftResultsDeprecatedAction,
        player: Player,
        context: ItemStackRequestContext
    ): ActionResponse? {
        if (context.has(CraftRecipeActionProcessor.Companion.RECIPE_DATA_KEY) && (context.get<Any>(
                CraftRecipeActionProcessor.Companion.RECIPE_DATA_KEY
            ) as Recipe).type == RecipeType.MULTI
        ) {
            val createdOutput = player.creativeOutputInventory
            val resultItem = action.resultItems[0]
            resultItem.autoAssignStackNetworkId()
            createdOutput.setItem(0, resultItem, false)
            return null
        }
        context.put(NO_RESPONSE_DESTROY_KEY, true)
        return null
    }

    companion object {
        const val NO_RESPONSE_DESTROY_KEY: String = "noResponseForDestroyAction"
    }
}
