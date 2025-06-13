package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object PearlescentFroglight :
    BlockDefinition(
        identifier = "minecraft:pearlescent_froglight",
        states = listOf(CommonStates.pillarAxis)
    )
