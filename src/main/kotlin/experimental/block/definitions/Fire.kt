package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Fire : BlockDefinition(
    identifier = "minecraft:fire",
    states = listOf(CommonStates.age)
)
