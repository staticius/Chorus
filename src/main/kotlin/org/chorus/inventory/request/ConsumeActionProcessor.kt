package org.chorus.inventory.request

import org.chorus.Player
import org.chorus.inventory.*
import org.chorus.network.protocol.types.itemstack.ContainerSlotType
import org.chorus.network.protocol.types.itemstack.request.action.ConsumeAction
import org.chorus.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import org.chorus.network.protocol.types.itemstack.response.ItemStackResponseContainer
import org.chorus.network.protocol.types.itemstack.response.ItemStackResponseSlot
import org.chorus.utils.Loggable

class ConsumeActionProcessor : ItemStackRequestActionProcessor<ConsumeAction> {
    override fun handle(action: ConsumeAction, player: Player, context: ItemStackRequestContext): ActionResponse? {
        // We have validated the recipe in CraftRecipeActionProcessor, so here we can believe the client directly
        val count = action.count
        if (count == 0) {
            ConsumeActionProcessor.log.warn("cannot consume 0 items!")

            return context.error()
        }

        val sourceContainer: Inventory = NetworkMapping.getInventory(player, action.source.containerName.container)
        val slot = sourceContainer.fromNetworkSlot(action.source.slot)
        var item = sourceContainer.getItem(slot)
        if (validateStackNetworkId(item.getNetId(), action.source.stackNetworkId)) {
            ConsumeActionProcessor.log.warn("mismatch stack network id!")

            return context.error()
        }

        if (item.isNothing) {
            ConsumeActionProcessor.log.warn("cannot consume an air!")

            return context.error()
        }

        if (item.getCount() < count) {
            ConsumeActionProcessor.log.warn("cannot consume more items than the current amount!")

            return context.error()
        }

        if (item.getCount() > count) {
            item.setCount(item.getCount() - count)
            sourceContainer.setItem(slot, item, false)
        } else {
            sourceContainer.clear(slot, false)
            item = sourceContainer.getItem(slot)
        }

        val isEnchRecipe = context.get<Boolean>(CraftRecipeActionProcessor.ENCH_RECIPE_KEY)
        if (isEnchRecipe != null && isEnchRecipe && action.source.containerName.container == ContainerSlotType.ENCHANTING_INPUT) {
            return null
        }

        val containerSlotType = sourceContainer.getSlotType(slot)

        return context.success(
            listOf(
                ItemStackResponseContainer(
                    containerSlotType,
                    mutableListOf(
                        ItemStackResponseSlot(
                            sourceContainer.toNetworkSlot(slot),
                            sourceContainer.toNetworkSlot(slot),
                            item.getCount(),
                            item.getNetId(),
                            item.customName,
                            item.damage
                        )
                    ),
                    action.source.containerName
                )
            )
        )
    }

    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.CONSUME

    companion object : Loggable
}
