package org.chorus.network.protocol.types.itemstack.request.action

import org.chorus.network.protocol.types.itemstack.request.ItemStackRequestSlotData


/**
 * PlaceStackRequestAction is sent by the client to the server to place x amount of items from one slot into
 * another slot, such as when shift clicking an item in the inventory to move it around or when moving an item
 * in the cursor into a slot.
 */

class PlaceAction(
    override var count: Int,
    override var source: ItemStackRequestSlotData,
    override var destination: ItemStackRequestSlotData,
) : TransferItemStackRequestAction {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.PLACE
}
