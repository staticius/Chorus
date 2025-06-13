package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object PaleMossCarpet : BlockDefinition(
    identifier = "minecraft:pale_moss_carpet",
    states = listOf(
        CommonStates.paleMossCarpetSideEast,
        CommonStates.paleMossCarpetSideNorth,
        CommonStates.paleMossCarpetSideSouth,
        CommonStates.paleMossCarpetSideWest,
        CommonStates.upperBlockBit
    )
)
