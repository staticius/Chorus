package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object DeadBrainCoralFan :
    BlockDefinition(
        identifier = "minecraft:dead_brain_coral_fan",
        states = listOf(CommonStates.coralFanDirection)
    )
