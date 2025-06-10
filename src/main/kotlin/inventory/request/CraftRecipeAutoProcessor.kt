package org.chorus_oss.chorus.inventory.request

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.Server
import org.chorus_oss.chorus.event.inventory.CraftItemEvent
import org.chorus_oss.chorus.item.Item
import org.chorus_oss.chorus.network.protocol.types.itemstack.request.action.AutoCraftRecipeAction
import org.chorus_oss.chorus.network.protocol.types.itemstack.request.action.ConsumeAction
import org.chorus_oss.chorus.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import org.chorus_oss.chorus.recipe.descriptor.DefaultDescriptor
import org.chorus_oss.chorus.recipe.descriptor.ItemTagDescriptor
import org.chorus_oss.chorus.registry.Registries
import org.chorus_oss.chorus.utils.Loggable


class CraftRecipeAutoProcessor : ItemStackRequestActionProcessor<AutoCraftRecipeAction> {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.CRAFT_RECIPE_AUTO

    override fun handle(
        action: AutoCraftRecipeAction,
        player: Player,
        context: ItemStackRequestContext
    ): ActionResponse? {
        val recipe = Registries.RECIPE.getRecipeByNetworkId(action.recipeNetworkId)

        val eventItems = action.ingredients.map { it.toItem() }.toTypedArray()

        val craftItemEvent = CraftItemEvent(player, eventItems, recipe, 1)
        Server.instance.pluginManager.callEvent(craftItemEvent)
        if (craftItemEvent.cancelled) {
            return context.error()
        }

        var success = 0
        for (clientInputItem in eventItems) {
            for (serverExpect in action.ingredients) {
                var match = false
                if (serverExpect is ItemTagDescriptor) {
                    match = serverExpect.match(clientInputItem)
                } else if (serverExpect is DefaultDescriptor) {
                    match = serverExpect.match(clientInputItem)
                }
                if (match) {
                    success++
                    break
                }
            }
        }

        val matched = success == action.ingredients.size
        if (!matched) {
            log.warn(
                "Mismatched recipe! Network id: {},Recipe name: {},Recipe type: {}",
                action.recipeNetworkId,
                recipe.recipeId,
                recipe.type
            )
            return context.error()
        } else {
            context.put(CraftRecipeActionProcessor.Companion.RECIPE_DATA_KEY, recipe)
            val consumeActions: List<ConsumeAction> = CraftRecipeActionProcessor.Companion.findAllConsumeActions(
                context.itemStackRequest.actions,
                context.currentActionIndex + 1
            )

            var consumeActionCountNeeded = 0
            for (item in eventItems) {
                if (!item.isNothing) {
                    consumeActionCountNeeded++
                }
            }
            if (consumeActions.size < consumeActionCountNeeded) {
                log.warn("Mismatched consume action count! Expected: " + consumeActionCountNeeded + ", Actual: " + consumeActions.size)
                return context.error()
            }
            if (recipe.results.size == 1) {
                val output: Item = recipe.results.first().clone()
                output.setCount(output.getCount() * action.timesCrafted)
                val createdOutput = player.creativeOutputInventory
                createdOutput.setItem(0, output.clone().autoAssignStackNetworkId(), false)
            }
        }
        return null
    }

    companion object : Loggable
}
