package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object BrewingStand : BlockDefinition(
    identifier = "minecraft:brewing_stand",
    states = listOf(
        CommonStates.brewingStandSlotABit,
        CommonStates.brewingStandSlotBBit,
        CommonStates.brewingStandSlotCBit
    )
)
