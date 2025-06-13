package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object EndStoneBrickDoubleSlab : BlockDefinition(
    identifier = "minecraft:end_stone_brick_double_slab",
    states = listOf(CommonStates.minecraftVerticalHalf)
)
