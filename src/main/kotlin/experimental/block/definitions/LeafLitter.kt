package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object LeafLitter : BlockDefinition(
    identifier = "minecraft:leaf_litter",
    states = listOf(CommonStates.growth, CommonStates.minecraftCardinalDirection)
)
