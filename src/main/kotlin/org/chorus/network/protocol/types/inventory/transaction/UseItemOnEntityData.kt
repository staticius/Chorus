package org.chorus.network.protocol.types.inventory.transaction

import cn.nukkit.item.Item
import cn.nukkit.math.Vector3
import lombok.ToString

/**
 * @author CreeperFace
 */
@ToString
class UseItemOnEntityData : TransactionData {
    var entityRuntimeId: Long = 0
    var actionType: Int = 0
    var hotbarSlot: Int = 0
    var itemInHand: Item? = null
    var playerPos: Vector3? = null
    var clickPos: Vector3? = null
}
