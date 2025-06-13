package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Water : BlockDefinition(
    identifier = "minecraft:water",
    states = listOf(CommonStates.liquidDepth)
)
