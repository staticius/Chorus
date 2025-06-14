package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object CalibratedSculkSensor : BlockDefinition(
    identifier = "minecraft:calibrated_sculk_sensor",
    states = listOf(CommonStates.minecraftCardinalDirection, CommonStates.sculkSensorPhase)
)
