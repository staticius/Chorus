package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Composter :
    BlockDefinition(
        identifier = "minecraft:composter",
        states = listOf(CommonStates.composterFillLevel)
    )
