package org.chorus.inventory.request

import org.chorus.Player
import org.chorus.network.protocol.types.itemstack.request.action.CraftResultsDeprecatedAction
import org.chorus.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import org.chorus.recipe.Recipe
import org.chorus.recipe.RecipeType
import lombok.extern.slf4j.Slf4j


/**
 * Allay Project 2023/12/2
 *
 * @author daoge_cmd | CoolLoong
 */

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
