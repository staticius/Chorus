package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Lava : BlockDefinition(
    identifier = "minecraft:lava",
    states = listOf(CommonStates.liquidDepth)
)
