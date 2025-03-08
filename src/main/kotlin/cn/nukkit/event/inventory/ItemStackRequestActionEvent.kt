package cn.nukkit.event.inventory

import cn.nukkit.Player
import cn.nukkit.event.Cancellable
import cn.nukkit.event.Event
import cn.nukkit.event.HandlerList
import cn.nukkit.inventory.request.ActionResponse
import cn.nukkit.inventory.request.ItemStackRequestContext
import cn.nukkit.network.protocol.types.itemstack.request.action.ItemStackRequestAction

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
