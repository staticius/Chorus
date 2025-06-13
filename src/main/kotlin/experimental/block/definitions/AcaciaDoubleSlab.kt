package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object AcaciaDoubleSlab : BlockDefinition(
    identifier = "minecraft:acacia_double_slab",
    states = listOf(
        CommonStates.minecraftVerticalHalf
    )
)