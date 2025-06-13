package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Basalt : BlockDefinition(
    identifier = "minecraft:basalt",
    states = listOf(CommonStates.pillarAxis)
)
