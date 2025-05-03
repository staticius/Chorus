package org.chorus_oss.chorus.inventory.request

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.Server
import org.chorus_oss.chorus.event.player.PlayerDropItemEvent
import org.chorus_oss.chorus.inventory.Inventory
import org.chorus_oss.chorus.network.protocol.types.itemstack.request.action.DropAction
import org.chorus_oss.chorus.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import org.chorus_oss.chorus.network.protocol.types.itemstack.response.ItemStackResponseContainer
import org.chorus_oss.chorus.network.protocol.types.itemstack.response.ItemStackResponseSlot
import org.chorus_oss.chorus.utils.Loggable

class DropActionProcessor : ItemStackRequestActionProcessor<DropAction> {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.DROP

    override fun handle(action: DropAction, player: Player, context: ItemStackRequestContext): ActionResponse {
        val inventory: Inventory = NetworkMapping.getInventory(player, action.source.containerName.container)
        val count = action.count
        val slot = inventory.fromNetworkSlot(action.source.slot)
        var item = inventory.getItem(slot)

        val ev: PlayerDropItemEvent
        Server.instance.pluginManager.callEvent(PlayerDropItemEvent(player, item).also { ev = it })
        if (ev.isCancelled) {
            return context.error()
        }

        if (validateStackNetworkId(item.getNetId(), action.source.stackNetworkId)) {
            DropActionProcessor.log.warn("mismatch stack network id!")
            return context.error()
        }
        if (item.isNothing) {
            DropActionProcessor.log.warn("cannot throw an air!")
            return context.error()
        }
        if (item.getCount() < count) {
            DropActionProcessor.log.warn("cannot throw more items than the current amount!")
            return context.error()
        }
        val drop = item.clone()
        drop.setCount(count)
        player.dropItem(drop)

        val c = item.getCount() - count
        if (c <= 0) {
            inventory.clear(slot, false)
            item = inventory.getItem(slot)
        } else {
            item.setCount(c)
            inventory.setItem(slot, item, false)
        }
        return context.success(
            listOf(
                ItemStackResponseContainer(
                    inventory.getSlotType(slot),
                    mutableListOf(
                        ItemStackResponseSlot(
                            inventory.toNetworkSlot(slot),
                            inventory.toNetworkSlot(slot),
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
