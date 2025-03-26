package org.chorus.network.protocol.types.itemstack.request.action

import org.chorus.network.protocol.types.itemstack.request.ItemStackRequestSlotData

/**
 * ConsumeStackRequestAction is sent by the client when it uses an item to craft another item. The original
 * item is 'consumed'.
 */
class ConsumeAction(
    var count: Int,
    var source: ItemStackRequestSlotData,
) : ItemStackRequestAction {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.CONSUME
}
