package org.chorus.network.protocol.types.itemstack.request.action

import org.chorus.network.protocol.types.itemstack.request.ItemStackRequestSlotData

/**
 * DestroyStackRequestActionData is sent by the client when it destroys an item in creative mode by moving it
 * back into the creative inventory.
 */
class DestroyAction(
    var count: Int,
    var source: ItemStackRequestSlotData,
) : ItemStackRequestAction {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.DESTROY
}
