package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Beetroot : BlockDefinition(
    identifier = "minecraft:beetroot",
    states = listOf(CommonStates.growth)
)
