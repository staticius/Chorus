package org.chorus.network.protocol.types.inventory.transaction

import org.chorus.item.Item
import org.chorus.math.Vector3




class UseItemOnEntityData : TransactionData {
    var entityRuntimeId: Long = 0
    var actionType: Int = 0
    var hotbarSlot: Int = 0
    var itemInHand: Item? = null
    var playerPos: Vector3? = null
    var clickPos: Vector3? = null
}
