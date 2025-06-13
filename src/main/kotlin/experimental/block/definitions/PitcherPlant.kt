package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object PitcherPlant :
    BlockDefinition(
        identifier = "minecraft:pitcher_plant",
        states = listOf(CommonStates.upperBlockBit)
    )
