package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object DeadFireCoralFan :
    BlockDefinition(identifier = "minecraft:dead_fire_coral_fan", states = listOf(CommonStates.coralFanDirection))
