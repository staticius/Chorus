package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Observer : BlockDefinition(
    identifier = "minecraft:observer",
    states = listOf(
        CommonStates.minecraftFacingDirection,
        CommonStates.poweredBit
    )
)
