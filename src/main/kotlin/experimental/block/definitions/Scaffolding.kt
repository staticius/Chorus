package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Scaffolding : BlockDefinition(
    identifier = "minecraft:scaffolding",
    states = listOf(CommonStates.stability, CommonStates.stabilityCheck)
)
