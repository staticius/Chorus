package org.chorus.network.protocol.types.inventory.transaction

import org.chorus.item.Item
import org.chorus.math.BlockFace
import org.chorus.math.BlockVector3
import org.chorus.math.Vector3
import org.chorus.math.Vector3f




data class UseItemData(
    var actionType: Int,
    var blockPos: BlockVector3,
    var face: BlockFace,
    var hotbarSlot: Int,
    var itemInHand: Item,
    var playerPos: Vector3,
    var clickPos: Vector3f,
    var blockRuntimeId: Int,
    var clientInteractPrediction: PredictedResult,
    var triggerType: TriggerType,
) : TransactionData {
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
