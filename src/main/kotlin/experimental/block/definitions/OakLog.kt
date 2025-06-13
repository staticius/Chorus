package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object OakLog : BlockDefinition(
    identifier = "minecraft:oak_log",
    states = listOf(CommonStates.pillarAxis)
)
