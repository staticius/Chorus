package org.chorus_oss.chorus.inventory.request

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.network.protocol.types.itemstack.request.action.CraftRecipeAction
import org.chorus_oss.chorus.network.protocol.types.itemstack.request.action.CreateAction
import org.chorus_oss.chorus.network.protocol.types.itemstack.request.action.ItemStackRequestAction
import org.chorus_oss.chorus.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import org.chorus_oss.chorus.registry.Registries
import org.chorus_oss.chorus.utils.Loggable

import java.util.*

class CreateActionProcessor : ItemStackRequestActionProcessor<CreateAction> {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.CREATE

    override fun handle(action: CreateAction, player: Player, context: ItemStackRequestContext): ActionResponse? {
        val itemStackRequestAction = Arrays.stream(context.itemStackRequest.actions)
            .filter { action1: ItemStackRequestAction? -> action1 is CraftRecipeAction }.findFirst()
        if (itemStackRequestAction.isEmpty) {
            log.warn("Recipe not found in ItemStackRequest Context! Context: $context")
            return context.error()
        }
        val recipe =
            Registries.RECIPE.getRecipeByNetworkId((itemStackRequestAction.get() as CraftRecipeAction).recipeNetworkId)
        val output = recipe.results[action.slot]
        val createdOutput = player.creativeOutputInventory
        createdOutput.setItem(0, output, false)
        return null
    }

    companion object : Loggable
}
