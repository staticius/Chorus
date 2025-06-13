package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object PolishedGraniteDoubleSlab : BlockDefinition(
    identifier = "minecraft:polished_granite_double_slab",
    states = listOf(CommonStates.minecraftVerticalHalf)
)
