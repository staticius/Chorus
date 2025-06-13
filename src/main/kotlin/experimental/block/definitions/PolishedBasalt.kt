package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object PolishedBasalt :
    BlockDefinition(
        identifier = "minecraft:polished_basalt",
        states = listOf(CommonStates.pillarAxis)
    )
