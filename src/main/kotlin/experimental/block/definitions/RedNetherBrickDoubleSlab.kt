package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object RedNetherBrickDoubleSlab : BlockDefinition(
    identifier = "minecraft:red_nether_brick_double_slab",
    states = listOf(CommonStates.minecraftVerticalHalf)
)
