package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object MangroveLog :
    BlockDefinition(
        identifier = "minecraft:mangrove_log",
        states = listOf(CommonStates.pillarAxis)
    )
