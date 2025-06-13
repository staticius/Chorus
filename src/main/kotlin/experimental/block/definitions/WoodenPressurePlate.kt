package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object WoodenPressurePlate :
    BlockDefinition(
        identifier = "minecraft:wooden_pressure_plate",
        states = listOf(CommonStates.redstoneSignal)
    )
