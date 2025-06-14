package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object SmoothStoneSlab :
    BlockDefinition(identifier = "minecraft:smooth_stone_slab", states = listOf(CommonStates.minecraftVerticalHalf))
