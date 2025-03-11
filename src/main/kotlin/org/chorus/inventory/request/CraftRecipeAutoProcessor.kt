package org.chorus.inventory.request

import org.chorus.Player
import org.chorus.Server
import org.chorus.event.inventory.CraftItemEvent
import org.chorus.item.*
import org.chorus.network.protocol.types.itemstack.request.action.AutoCraftRecipeAction
import org.chorus.network.protocol.types.itemstack.request.action.ConsumeAction
import org.chorus.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import org.chorus.recipe.descriptor.DefaultDescriptor
import org.chorus.recipe.descriptor.ItemDescriptor
import org.chorus.recipe.descriptor.ItemTagDescriptor
import org.chorus.registry.Registries



class CraftRecipeAutoProcessor : ItemStackRequestActionProcessor<AutoCraftRecipeAction> {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.CRAFT_RECIPE_AUTO

    override fun handle(
        action: AutoCraftRecipeAction,
        player: Player,
        context: ItemStackRequestContext
    ): ActionResponse? {
        val recipe = Registries.RECIPE.getRecipeByNetworkId(action.recipeNetworkId)

        val eventItems = action.ingredients.stream().map<Item> { obj: ItemDescriptor -> obj.toItem() }
            .toArray<Item> { _Dummy_.__Array__() }

        val craftItemEvent = CraftItemEvent(player, eventItems, recipe, 1)
        Server.instance.pluginManager.callEvent(craftItemEvent)
        if (craftItemEvent.isCancelled) {
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
            CraftRecipeAutoProcessor.log.warn(
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
                if (!item.isNull) {
                    consumeActionCountNeeded++
                }
            }
            if (consumeActions.size < consumeActionCountNeeded) {
                CraftRecipeAutoProcessor.log.warn("Mismatched consume action count! Expected: " + consumeActionCountNeeded + ", Actual: " + consumeActions.size)
                return context.error()
            }
            if (recipe.results.size == 1) {
                val output: Item = recipe.results.getFirst().clone()
                output.setCount(output.getCount() * action.timesCrafted)
                val createdOutput = player.creativeOutputInventory
                createdOutput.setItem(0, output.clone().autoAssignStackNetworkId(), false)
            }
        }
        return null
    }
}
