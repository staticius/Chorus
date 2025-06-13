package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object HayBlock : BlockDefinition(
    identifier = "minecraft:hay_block",
    states = listOf(
        CommonStates.deprecated,
        CommonStates.pillarAxis
    )
)
