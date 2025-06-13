package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object TripwireHook : BlockDefinition(
    identifier = "minecraft:tripwire_hook",
    states = listOf(
        CommonStates.direction,
        CommonStates.attachedBit,
        CommonStates.poweredBit
    )
)
