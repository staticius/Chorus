package org.chorus.network.protocol.types.itemstack.request.action

import org.chorus.network.protocol.types.itemstack.request.ItemStackRequestSlotData


/**
 * TakeStackRequestActionData is sent by the client to the server to take x amount of items from one slot in a
 * container to the cursor.
 */

class TakeAction : TransferItemStackRequestAction {
    override var count: Int = 0
    override var source: ItemStackRequestSlotData? = null
    override var destination: ItemStackRequestSlotData? = null

    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.TAKE
}
