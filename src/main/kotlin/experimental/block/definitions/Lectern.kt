package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Lectern : BlockDefinition(
    identifier = "minecraft:lectern",
    states = listOf(
        CommonStates.minecraftCardinalDirection,
        CommonStates.poweredBit
    )
)
