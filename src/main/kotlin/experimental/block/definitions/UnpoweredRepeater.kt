package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object UnpoweredRepeater : BlockDefinition(
    identifier = "minecraft:unpowered_repeater",
    states = listOf(
        CommonStates.minecraftCardinalDirection,
        CommonStates.repeaterDelay
    )
)
