package org.chorus.network.protocol.types.itemstack.request.action

import org.chorus.network.protocol.types.itemstack.request.ItemStackRequestSlotData


/**
 * DropStackRequestActionData is sent by the client when it drops an item out of the inventory when it has its
 * inventory opened. This action is not sent when a player drops an item out of the hotbar using the Q button
 * (or the equivalent on mobile). The InventoryTransaction packet is still used for that action, regardless of
 * whether the item stack network IDs are used or not.
 */

class DropAction : ItemStackRequestAction {
    var count: Int = 0
    var source: ItemStackRequestSlotData? = null
    var randomly: Boolean = false // ?? Perhaps deals with order of items being dropped? Normally false.

    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.DROP
}
