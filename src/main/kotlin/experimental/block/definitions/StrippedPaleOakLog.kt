package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object StrippedPaleOakLog :
    BlockDefinition(
        identifier = "minecraft:stripped_pale_oak_log",
        states = listOf(CommonStates.pillarAxis)
    )
