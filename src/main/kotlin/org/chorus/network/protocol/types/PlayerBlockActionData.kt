package org.chorus.network.protocol.types

import cn.nukkit.math.BlockVector3
import lombok.AllArgsConstructor
import lombok.Data

@Data
@AllArgsConstructor
class PlayerBlockActionData {
    private val action: PlayerActionType? = null
    private val position: BlockVector3? = null
    private val facing = 0
}