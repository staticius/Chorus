package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object DarkOakLog :
    BlockDefinition(
        identifier = "minecraft:dark_oak_log",
        states = listOf(CommonStates.pillarAxis)
    )
