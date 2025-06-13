package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object MangroveFenceGate : BlockDefinition(
    identifier = "minecraft:mangrove_fence_gate",
    states = listOf(
        CommonStates.inWallBit,
        CommonStates.minecraftCardinalDirection,
        CommonStates.openBit
    )
)
