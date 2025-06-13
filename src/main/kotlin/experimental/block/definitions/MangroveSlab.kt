package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object MangroveSlab :
    BlockDefinition(
        identifier = "minecraft:mangrove_slab",
        states = listOf(CommonStates.minecraftVerticalHalf)
    )
