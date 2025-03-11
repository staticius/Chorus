package org.chorus.network.protocol.types.itemstack.request.action

import org.chorus.network.protocol.types.itemstack.request.ItemStackRequestSlotData


/**
 * SwapStackRequestActionData is sent by the client to swap the item in its cursor with an item present in another
 * container. The two item stacks swap places.
 */

class SwapAction : ItemStackRequestAction {
    var source: ItemStackRequestSlotData? = null
    var destination: ItemStackRequestSlotData? = null

    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.SWAP
}
