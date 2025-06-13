package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object NormalStoneSlab :
    BlockDefinition(
        identifier = "minecraft:normal_stone_slab",
        states = listOf(CommonStates.minecraftVerticalHalf)
    )
