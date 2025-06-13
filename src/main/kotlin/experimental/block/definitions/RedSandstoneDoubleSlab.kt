package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object RedSandstoneDoubleSlab : BlockDefinition(
    identifier = "minecraft:red_sandstone_double_slab",
    states = listOf(CommonStates.minecraftVerticalHalf)
)
