package org.chorus.network.protocol.types.itemstack.request.action

import cn.nukkit.network.protocol.types.itemstack.request.ItemStackRequestSlotData
import lombok.Value

/**
 * ConsumeStackRequestAction is sent by the client when it uses an item to craft another item. The original
 * item is 'consumed'.
 */
@Value
class ConsumeAction : ItemStackRequestAction {
    var count: Int = 0
    var source: ItemStackRequestSlotData? = null

    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.CONSUME
}
