package org.chorus_oss.chorus.inventory.request

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.inventory.Inventory
import org.chorus_oss.chorus.network.protocol.types.itemstack.request.action.DestroyAction
import org.chorus_oss.chorus.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import org.chorus_oss.chorus.network.protocol.types.itemstack.response.ItemStackResponseContainer
import org.chorus_oss.chorus.network.protocol.types.itemstack.response.ItemStackResponseSlot
import org.chorus_oss.chorus.utils.Loggable

class DestroyActionProcessor : ItemStackRequestActionProcessor<DestroyAction> {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.DESTROY

    override fun handle(action: DestroyAction, player: Player, context: ItemStackRequestContext): ActionResponse? {
        val noResponseForDestroyAction =
            context.get<Boolean>(CraftResultDeprecatedActionProcessor.Companion.NO_RESPONSE_DESTROY_KEY)
        if (noResponseForDestroyAction != null && noResponseForDestroyAction) {
            return null
        }
        if (player.gamemode != 1) {
            log.warn("only creative mode can destroy item")
            return context.error()
        }
        val container = action.source.containerName.container
        val sourceInventory: Inventory = NetworkMapping.getInventory(player, container)
        val count = action.count
        val slot = sourceInventory.fromNetworkSlot(action.source.slot)
        var item = sourceInventory.getItem(slot)
        if (validateStackNetworkId(item.getNetId(), action.source.stackNetworkId)) {
            log.warn("mismatch stack network id!")
            return context.error()
        }
        if (item.isNothing) {
            log.warn("cannot destroy an air!")
            return context.error()
        }
        if (item.getCount() < count) {
            log.warn("cannot destroy more items than the current amount!")
            return context.error()
        }
        if (item.getCount() > count) {
            item.setCount(item.getCount() - count)
            sourceInventory.setItem(slot, item, false)
        } else {
            sourceInventory.clear(slot, false)
            item = sourceInventory.getItem(slot)
        }
        return context.success(
            listOf(
                ItemStackResponseContainer(
                    sourceInventory.getSlotType(slot),
                    mutableListOf(
                        ItemStackResponseSlot(
                            sourceInventory.toNetworkSlot(slot),
                            sourceInventory.toNetworkSlot(slot),
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

    companion object : Loggable
}
