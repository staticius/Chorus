package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object CrimsonStem :
    BlockDefinition(
        identifier = "minecraft:crimson_stem",
        states = listOf(CommonStates.pillarAxis)
    )
