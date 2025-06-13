package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object SuspiciousSand : BlockDefinition(
    identifier = "minecraft:suspicious_sand",
    states = listOf(
        CommonStates.hanging,
        CommonStates.brushedProgress
    )
)
