package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Rail : BlockDefinition(
    identifier = "minecraft:rail",
    states = listOf(CommonStates.railDirection)
)
