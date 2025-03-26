package org.chorus.network.protocol.types.itemstack.request.action

import org.chorus.network.protocol.types.itemstack.request.ItemStackRequestSlotData

/**
 * TakeStackRequestActionData is sent by the client to the server to take x amount of items from one slot in a
 * container to the cursor.
 */
class TakeAction(
    override var count: Int,
    override var source: ItemStackRequestSlotData,
    override var destination: ItemStackRequestSlotData,
) : TransferItemStackRequestAction {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.TAKE
}
