package org.chorus.network.protocol.types.inventory.transaction

import org.chorus.item.Item
import org.chorus.math.Vector3




data class ReleaseItemData(
    var actionType: Int,
    var hotbarSlot: Int,
    var itemInHand: Item,
    var headRot: Vector3,
) : TransactionData
