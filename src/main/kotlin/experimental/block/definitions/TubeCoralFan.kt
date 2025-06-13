package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object TubeCoralFan :
    BlockDefinition(
        identifier = "minecraft:tube_coral_fan",
        states = listOf(CommonStates.coralFanDirection)
    )
