package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object PolishedBlackstoneStairs : BlockDefinition(
    identifier = "minecraft:polished_blackstone_stairs",
    states = listOf(CommonStates.upsideDownBit, CommonStates.weirdoDirection)
)
