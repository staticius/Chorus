package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Cauldron : BlockDefinition(
    identifier = "minecraft:cauldron",
    states = listOf(CommonStates.cauldronLiquid, CommonStates.fillLevel)
)
