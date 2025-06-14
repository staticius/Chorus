package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object GoldenRail : BlockDefinition(
    identifier = "minecraft:golden_rail",
    states = listOf(CommonStates.railDataBit, CommonStates.railDirection6)
)
