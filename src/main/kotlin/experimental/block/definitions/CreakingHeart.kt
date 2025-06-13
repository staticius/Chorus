package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object CreakingHeart : BlockDefinition(
    identifier = "minecraft:creaking_heart",
    states = listOf(
        CommonStates.natural,
        CommonStates.creakingHeartState,
        CommonStates.pillarAxis
    )
)
