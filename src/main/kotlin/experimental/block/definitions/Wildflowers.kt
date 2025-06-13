package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Wildflowers : BlockDefinition(
    identifier = "minecraft:wildflowers",
    states = listOf(
        CommonStates.minecraftCardinalDirection,
        CommonStates.growth
    )
)
