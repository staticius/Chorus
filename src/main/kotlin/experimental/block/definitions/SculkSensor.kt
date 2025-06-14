package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object SculkSensor :
    BlockDefinition(identifier = "minecraft:sculk_sensor", states = listOf(CommonStates.sculkSensorPhase))
