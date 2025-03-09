package org.chorus.network.protocol.types.itemstack.request.action

import org.chorus.network.protocol.types.itemstack.request.ItemStackRequestSlotData


/**
 * TransferStackRequestActionData is the structure shared by StackRequestActions that transfer items from one
 * slot into another
 */
interface TransferItemStackRequestAction : ItemStackRequestAction {
    val count: Int

    val source: ItemStackRequestSlotData?

    val destination: ItemStackRequestSlotData?
}
