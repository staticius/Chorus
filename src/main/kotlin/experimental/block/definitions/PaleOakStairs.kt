package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object PaleOakStairs : BlockDefinition(
    identifier = "minecraft:pale_oak_stairs",
    states = listOf(CommonStates.upsideDownBit, CommonStates.weirdoDirection)
)
