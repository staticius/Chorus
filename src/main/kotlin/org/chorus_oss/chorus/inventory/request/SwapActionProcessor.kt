package org.chorus_oss.chorus.inventory.request

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.inventory.Inventory
import org.chorus_oss.chorus.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import org.chorus_oss.chorus.network.protocol.types.itemstack.request.action.SwapAction
import org.chorus_oss.chorus.network.protocol.types.itemstack.response.ItemStackResponseContainer
import org.chorus_oss.chorus.network.protocol.types.itemstack.response.ItemStackResponseSlot
import org.chorus_oss.chorus.utils.Loggable

class SwapActionProcessor : ItemStackRequestActionProcessor<SwapAction> {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.SWAP

    override fun handle(action: SwapAction, player: Player, context: ItemStackRequestContext): ActionResponse {
        val sourceSlotType = action.source.containerName.container
        val destinationSlotType = action.destination.containerName.container
        val source: Inventory = NetworkMapping.getInventory(player, sourceSlotType)
        val destination: Inventory = NetworkMapping.getInventory(player, destinationSlotType)

        val sourceSlot = source.fromNetworkSlot(action.source.slot)
        val destinationSlot = destination.fromNetworkSlot(action.destination.slot)
        val sourceItem = source.getItem(sourceSlot)
        val destinationItem = destination.getItem(destinationSlot)
        if (validateStackNetworkId(sourceItem.getNetId(), action.source.stackNetworkId)) {
            SwapActionProcessor.log.warn("mismatch stack network id!")
            return context.error()
        }
        if (validateStackNetworkId(destinationItem.getNetId(), action.destination.stackNetworkId)) {
            SwapActionProcessor.log.warn("mismatch stack network id!")
            return context.error()
        }
        source.setItem(sourceSlot, destinationItem, false)
        destination.setItem(destinationSlot, sourceItem, false)
        return context.success(
            listOf(
                ItemStackResponseContainer(
                    source.getSlotType(sourceSlot),
                    mutableListOf(
                        ItemStackResponseSlot(
                            source.toNetworkSlot(sourceSlot),
                            source.toNetworkSlot(sourceSlot),
                            destinationItem.getCount(),
                            destinationItem.getNetId(),
                            destinationItem.customName,
                            destinationItem.damage
                        )
                    ),
                    action.source.containerName
                ),
                ItemStackResponseContainer(
                    destination.getSlotType(destinationSlot),
                    mutableListOf(
                        ItemStackResponseSlot(
                            destination.toNetworkSlot(destinationSlot),
                            destination.toNetworkSlot(destinationSlot),
                            sourceItem.getCount(),
                            sourceItem.getNetId(),
                            sourceItem.customName,
                            sourceItem.damage
                        )
                    ),
                    action.destination.containerName
                )
            )
        )
    }

    companion object : Loggable
}
