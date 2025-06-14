package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object MangrovePressurePlate :
    BlockDefinition(identifier = "minecraft:mangrove_pressure_plate", states = listOf(CommonStates.redstoneSignal))
