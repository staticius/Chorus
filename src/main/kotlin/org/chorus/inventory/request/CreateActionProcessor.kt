package org.chorus.inventory.request

import org.chorus.Player
import org.chorus.network.protocol.types.itemstack.request.action.CraftRecipeAction
import org.chorus.network.protocol.types.itemstack.request.action.CreateAction
import org.chorus.network.protocol.types.itemstack.request.action.ItemStackRequestAction
import org.chorus.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import org.chorus.registry.Registries
import org.chorus.utils.Loggable

import java.util.*

class CreateActionProcessor : ItemStackRequestActionProcessor<CreateAction> {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.CREATE

    override fun handle(action: CreateAction, player: Player, context: ItemStackRequestContext): ActionResponse? {
        val itemStackRequestAction = Arrays.stream(context.itemStackRequest.actions)
            .filter { action1: ItemStackRequestAction? -> action1 is CraftRecipeAction }.findFirst()
        if (itemStackRequestAction.isEmpty) {
            CreateActionProcessor.log.warn("Recipe not found in ItemStackRequest Context! Context: $context")
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
