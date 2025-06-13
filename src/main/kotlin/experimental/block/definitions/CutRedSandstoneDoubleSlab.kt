package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object CutRedSandstoneDoubleSlab : BlockDefinition(
    identifier = "minecraft:cut_red_sandstone_double_slab",
    states = listOf(CommonStates.minecraftVerticalHalf)
)
