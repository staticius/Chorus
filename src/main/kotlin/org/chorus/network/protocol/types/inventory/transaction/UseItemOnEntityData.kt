package org.chorus.network.protocol.types.inventory.transaction

import org.chorus.item.Item
import org.chorus.math.Vector3

data class UseItemOnEntityData(
    val entityRuntimeId: Long,
    val actionType: Int,
    val hotbarSlot: Int,
    val itemInHand: Item,
    val playerPos: Vector3,
    val clickPos: Vector3,
) : TransactionData
