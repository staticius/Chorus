package org.chorus.network.protocol.types.inventory.transaction

import org.chorus.item.Item
import org.chorus.math.Vector3

data class ReleaseItemData(
    val actionType: Int,
    val hotbarSlot: Int,
    val itemInHand: Item,
    val headRot: Vector3,
) : TransactionData
