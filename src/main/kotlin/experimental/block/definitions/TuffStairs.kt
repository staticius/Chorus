package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object TuffStairs : BlockDefinition(
    identifier = "minecraft:tuff_stairs",
    states = listOf(CommonStates.upsideDownBit, CommonStates.weirdoDirection)
)
