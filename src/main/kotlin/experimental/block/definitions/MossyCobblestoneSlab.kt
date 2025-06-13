package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object MossyCobblestoneSlab : BlockDefinition(
    identifier = "minecraft:mossy_cobblestone_slab",
    states = listOf(CommonStates.minecraftVerticalHalf)
)
