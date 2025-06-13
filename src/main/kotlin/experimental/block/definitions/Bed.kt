package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Bed : BlockDefinition(
    identifier = "minecraft:bed",
    states = listOf(
        CommonStates.direction,
        CommonStates.headPieceBit,
        CommonStates.occupiedBit
    )
)
