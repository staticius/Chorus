package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object QuartzStairs : BlockDefinition(
    identifier = "minecraft:quartz_stairs",
    states = listOf(
        CommonStates.upsideDownBit,
        CommonStates.weirdoDirection
    )
)
