package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object EndRod : BlockDefinition(
    identifier = "minecraft:end_rod",
    states = listOf(CommonStates.facingDirection)
)
