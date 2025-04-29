package org.chorus_oss.chorus.network.protocol.types.itemstack.request.action

import org.chorus_oss.chorus.network.protocol.types.itemstack.request.ItemStackRequestSlotData

interface TransferItemStackRequestAction : ItemStackRequestAction {
    val count: Int
    val source: ItemStackRequestSlotData
    val destination: ItemStackRequestSlotData
}
