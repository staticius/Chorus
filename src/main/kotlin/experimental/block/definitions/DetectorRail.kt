package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object DetectorRail : BlockDefinition(
    identifier = "minecraft:detector_rail",
    states = listOf(
        CommonStates.railDataBit,
        CommonStates.railDirection
    )
)
