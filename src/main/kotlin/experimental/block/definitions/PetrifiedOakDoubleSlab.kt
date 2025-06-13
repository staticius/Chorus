package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object PetrifiedOakDoubleSlab : BlockDefinition(
    identifier = "minecraft:petrified_oak_double_slab",
    states = listOf(CommonStates.minecraftVerticalHalf)
)
