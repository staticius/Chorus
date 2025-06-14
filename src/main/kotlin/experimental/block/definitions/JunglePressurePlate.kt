package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object JunglePressurePlate :
    BlockDefinition(identifier = "minecraft:jungle_pressure_plate", states = listOf(CommonStates.redstoneSignal))
