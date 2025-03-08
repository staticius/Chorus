package cn.nukkit.network.protocol.types.itemstack.request.action

import cn.nukkit.network.protocol.types.itemstack.request.ItemStackRequestSlotData


/**
 * TransferStackRequestActionData is the structure shared by StackRequestActions that transfer items from one
 * slot into another
 */
interface TransferItemStackRequestAction : ItemStackRequestAction {
    val count: Int

    val source: ItemStackRequestSlotData?

    val destination: ItemStackRequestSlotData?
}
