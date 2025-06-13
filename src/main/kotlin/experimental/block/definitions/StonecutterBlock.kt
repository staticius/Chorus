package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object StonecutterBlock : BlockDefinition(
    identifier = "minecraft:stonecutter_block",
    states = listOf(CommonStates.minecraftCardinalDirection)
)
