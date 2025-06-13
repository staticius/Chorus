package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object SmoothQuartzDoubleSlab : BlockDefinition(
    identifier = "minecraft:smooth_quartz_double_slab",
    states = listOf(CommonStates.minecraftVerticalHalf)
)
