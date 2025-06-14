package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Hopper : BlockDefinition(
    identifier = "minecraft:hopper",
    states = listOf(CommonStates.facingDirection, CommonStates.toggleBit)
)
