package org.chorus.network.protocol.types.inventory.transaction

import org.chorus.item.Item
import org.chorus.math.BlockFace
import org.chorus.math.BlockVector3
import org.chorus.math.Vector3
import org.chorus.math.Vector3f
import lombok.ToString

/**
 * @author CreeperFace
 */
@ToString
class UseItemData : TransactionData {
    var actionType: Int = 0
    var blockPos: BlockVector3? = null
    var face: BlockFace? = null
    var hotbarSlot: Int = 0
    var itemInHand: Item? = null
    var playerPos: Vector3? = null
    var clickPos: Vector3f? = null
    var blockRuntimeId: Int = 0
    var clientInteractPrediction: PredictedResult? = null
    var triggerType: TriggerType? = null

    enum class PredictedResult {
        FAILURE,
        SUCCESS
    }

    enum class TriggerType {
        UNKNOWN,
        PLAYER_INPUT,
        SIMULATION_TICK
    }
}
