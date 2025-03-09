package org.chorus.event.inventory

import org.chorus.Player
import org.chorus.event.Cancellable
import org.chorus.event.Event
import org.chorus.event.HandlerList
import org.chorus.inventory.request.ActionResponse
import org.chorus.inventory.request.ItemStackRequestContext
import org.chorus.network.protocol.types.itemstack.request.action.ItemStackRequestAction

class ItemStackRequestActionEvent(
    @JvmField val player: Player,
    @JvmField val action: ItemStackRequestAction,
    val context: ItemStackRequestContext
) :
    Event(), Cancellable {
    @JvmField
    var response: ActionResponse? = null

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
