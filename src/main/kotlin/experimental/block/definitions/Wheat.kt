package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Wheat : BlockDefinition(
    identifier = "minecraft:wheat",
    states = listOf(CommonStates.growth)
)
