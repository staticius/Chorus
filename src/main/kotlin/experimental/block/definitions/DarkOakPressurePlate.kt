package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object DarkOakPressurePlate :
    BlockDefinition(
        identifier = "minecraft:dark_oak_pressure_plate",
        states = listOf(CommonStates.redstoneSignal)
    )
