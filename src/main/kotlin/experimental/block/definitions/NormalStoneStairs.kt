package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object NormalStoneStairs : BlockDefinition(
    identifier = "minecraft:normal_stone_stairs",
    states = listOf(CommonStates.upsideDownBit, CommonStates.weirdoDirection)
)
