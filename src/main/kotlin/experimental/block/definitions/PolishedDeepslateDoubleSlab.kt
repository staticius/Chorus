package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object PolishedDeepslateDoubleSlab : BlockDefinition(
    identifier = "minecraft:polished_deepslate_double_slab",
    states = listOf(CommonStates.minecraftVerticalHalf)
)
