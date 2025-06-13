package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object WeatheredCutCopperSlab : BlockDefinition(
    identifier = "minecraft:weathered_cut_copper_slab",
    states = listOf(CommonStates.minecraftVerticalHalf)
)
