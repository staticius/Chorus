package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object DeadTubeCoralFan :
    BlockDefinition(
        identifier = "minecraft:dead_tube_coral_fan",
        states = listOf(CommonStates.coralFanDirection)
    )
