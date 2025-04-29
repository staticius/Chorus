package org.chorus_oss.chorus.event.inventory

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.event.Cancellable
import org.chorus_oss.chorus.event.Event
import org.chorus_oss.chorus.event.HandlerList
import org.chorus_oss.chorus.inventory.request.ActionResponse
import org.chorus_oss.chorus.inventory.request.ItemStackRequestContext
import org.chorus_oss.chorus.network.protocol.types.itemstack.request.action.ItemStackRequestAction

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
