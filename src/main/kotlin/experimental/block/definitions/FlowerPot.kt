package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object FlowerPot : BlockDefinition(
    identifier = "minecraft:flower_pot",
    states = listOf(CommonStates.updateBit)
)
