package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object DeepslateTileSlab :
    BlockDefinition(
        identifier = "minecraft:deepslate_tile_slab",
        states = listOf(CommonStates.minecraftVerticalHalf)
    )
