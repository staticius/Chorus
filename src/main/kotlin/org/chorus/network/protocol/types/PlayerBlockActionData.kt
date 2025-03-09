package org.chorus.network.protocol.types

import org.chorus.math.BlockVector3
import lombok.AllArgsConstructor
import lombok.Data



class PlayerBlockActionData {
    private val action: PlayerActionType? = null
    private val position: BlockVector3? = null
    private val facing = 0
}