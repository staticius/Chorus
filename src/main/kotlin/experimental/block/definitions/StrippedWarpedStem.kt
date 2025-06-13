package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object StrippedWarpedStem :
    BlockDefinition(
        identifier = "minecraft:stripped_warped_stem",
        states = listOf(CommonStates.pillarAxis)
    )
