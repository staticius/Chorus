package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Cake : BlockDefinition(
    identifier = "minecraft:cake",
    states = listOf(CommonStates.biteCounter)
)
