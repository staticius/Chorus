package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object PinkPetals : BlockDefinition(
    identifier = "minecraft:pink_petals",
    states = listOf(CommonStates.growth, CommonStates.minecraftCardinalDirection)
)
