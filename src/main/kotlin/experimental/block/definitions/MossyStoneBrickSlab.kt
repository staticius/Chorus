package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object MossyStoneBrickSlab : BlockDefinition(
    identifier = "minecraft:mossy_stone_brick_slab",
    states = listOf(CommonStates.minecraftVerticalHalf)
)
