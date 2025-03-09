package org.chorus.network.protocol.types.itemstack.request.action

import cn.nukkit.network.protocol.types.itemstack.request.ItemStackRequestSlotData
import lombok.Value

/**
 * SwapStackRequestActionData is sent by the client to swap the item in its cursor with an item present in another
 * container. The two item stacks swap places.
 */
@Value
class SwapAction : ItemStackRequestAction {
    var source: ItemStackRequestSlotData? = null
    var destination: ItemStackRequestSlotData? = null

    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.SWAP
}
