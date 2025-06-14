package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object SmoothRedSandstoneStairs : BlockDefinition(
    identifier = "minecraft:smooth_red_sandstone_stairs",
    states = listOf(CommonStates.upsideDownBit, CommonStates.weirdoDirection)
)
