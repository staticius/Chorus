package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object MangrovePropagule : BlockDefinition(
    identifier = "minecraft:mangrove_propagule",
    states = listOf(CommonStates.hanging, CommonStates.propaguleStage)
)
