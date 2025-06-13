package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object AndesiteSlab : BlockDefinition(
    identifier = "minecraft:andesite_slab",
    states = listOf(
        CommonStates.minecraftVerticalHalf
    )
)