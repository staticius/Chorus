package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object HornCoralWallFan :
    BlockDefinition(identifier = "minecraft:horn_coral_wall_fan", states = listOf(CommonStates.coralDirection))
