package org.chorus.network.protocol.types.inventory.transaction

import org.chorus.item.Item
import org.chorus.math.Vector3




data class UseItemOnEntityData(
    var entityRuntimeId: Long,
    var actionType: Int,
    var hotbarSlot: Int,
    var itemInHand: Item,
    var playerPos: Vector3,
    var clickPos: Vector3,
) : TransactionData
