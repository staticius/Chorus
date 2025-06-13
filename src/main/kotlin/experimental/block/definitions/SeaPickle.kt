package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object SeaPickle : BlockDefinition(
    identifier = "minecraft:sea_pickle",
    states = listOf(
        CommonStates.clusterCount,
        CommonStates.deadBit
    )
)
