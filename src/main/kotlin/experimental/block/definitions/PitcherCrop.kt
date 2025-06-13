package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object PitcherCrop : BlockDefinition(
    identifier = "minecraft:pitcher_crop",
    states = listOf(
        CommonStates.growth,
        CommonStates.upperBlockBit
    )
)
