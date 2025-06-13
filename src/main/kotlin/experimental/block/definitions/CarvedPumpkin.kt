package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object CarvedPumpkin :
    BlockDefinition(
        identifier = "minecraft:carved_pumpkin",
        states = listOf(CommonStates.minecraftCardinalDirection)
    )
