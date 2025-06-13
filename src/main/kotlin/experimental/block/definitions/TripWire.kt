package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object TripWire : BlockDefinition(
    identifier = "minecraft:trip_wire",
    states = listOf(
        CommonStates.poweredBit,
        CommonStates.suspendedBit,
        CommonStates.attachedBit,
        CommonStates.disarmedBit
    )
)
