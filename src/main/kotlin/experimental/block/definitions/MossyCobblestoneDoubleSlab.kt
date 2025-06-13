package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object MossyCobblestoneDoubleSlab : BlockDefinition(
    identifier = "minecraft:mossy_cobblestone_double_slab",
    states = listOf(CommonStates.minecraftVerticalHalf)
)
