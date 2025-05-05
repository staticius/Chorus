package org.chorus_oss.chorus.network.protocol.types

import org.chorus_oss.chorus.math.BlockVector3

data class PlayerBlockActionData(
    val action: PlayerActionType,
    val position: BlockVector3,
    val facing: Int,
)