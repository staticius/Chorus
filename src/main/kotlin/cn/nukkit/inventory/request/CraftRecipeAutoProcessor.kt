package cn.nukkit.inventory.request

import cn.nukkit.Player
import cn.nukkit.entity.Entity.getServer
import cn.nukkit.event.inventory.CraftItemEvent
import cn.nukkit.item.*
import cn.nukkit.network.protocol.types.itemstack.request.action.AutoCraftRecipeAction
import cn.nukkit.network.protocol.types.itemstack.request.action.ConsumeAction
import cn.nukkit.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import cn.nukkit.recipe.descriptor.DefaultDescriptor
import cn.nukkit.recipe.descriptor.ItemDescriptor
import cn.nukkit.recipe.descriptor.ItemTagDescriptor
import cn.nukkit.registry.Registries
import lombok.extern.slf4j.Slf4j

@Slf4j
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
        player.getServer().getPluginManager().callEvent(craftItemEvent)
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
