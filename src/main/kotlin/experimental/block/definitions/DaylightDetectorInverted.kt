package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object DaylightDetectorInverted :
    BlockDefinition(
        identifier = "minecraft:daylight_detector_inverted",
        states = listOf(CommonStates.redstoneSignal)
    )
