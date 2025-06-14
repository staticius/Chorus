package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object PoweredComparator : BlockDefinition(
    identifier = "minecraft:powered_comparator",
    states = listOf(CommonStates.minecraftCardinalDirection, CommonStates.outputLitBit, CommonStates.outputSubtractBit)
)
