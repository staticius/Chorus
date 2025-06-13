package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object CherryWood :
    BlockDefinition(
        identifier = "minecraft:cherry_wood",
        states = listOf(CommonStates.pillarAxis)
    )
