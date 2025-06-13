package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object SmoothRedSandstoneSlab : BlockDefinition(
    identifier = "minecraft:smooth_red_sandstone_slab",
    states = listOf(CommonStates.minecraftVerticalHalf)
)
