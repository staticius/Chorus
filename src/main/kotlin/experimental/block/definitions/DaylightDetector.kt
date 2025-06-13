package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object DaylightDetector :
    BlockDefinition(
        identifier = "minecraft:daylight_detector",
        states = listOf(CommonStates.redstoneSignal)
    )
