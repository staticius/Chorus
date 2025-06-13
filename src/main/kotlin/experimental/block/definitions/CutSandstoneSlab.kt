package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object CutSandstoneSlab :
    BlockDefinition(
        identifier = "minecraft:cut_sandstone_slab",
        states = listOf(CommonStates.minecraftVerticalHalf)
    )
