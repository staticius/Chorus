package org.chorus.network.protocol.types.inventory.transaction

import cn.nukkit.item.Item
import cn.nukkit.math.Vector3
import lombok.ToString

/**
 * @author CreeperFace
 */
@ToString
class ReleaseItemData : TransactionData {
    var actionType: Int = 0
    var hotbarSlot: Int = 0
    var itemInHand: Item? = null
    var headRot: Vector3? = null
}
