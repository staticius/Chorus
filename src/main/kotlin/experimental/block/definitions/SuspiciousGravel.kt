package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object SuspiciousGravel : BlockDefinition(
    identifier = "minecraft:suspicious_gravel",
    states = listOf(
        CommonStates.hanging,
        CommonStates.brushedProgress
    )
)
