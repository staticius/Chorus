package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object DeepslateTileWall : BlockDefinition(
    identifier = "minecraft:deepslate_tile_wall",
    states = listOf(
        CommonStates.wallConnectionTypeEast,
        CommonStates.wallConnectionTypeNorth,
        CommonStates.wallConnectionTypeSouth,
        CommonStates.wallConnectionTypeWest,
        CommonStates.wallPostBit
    )
)
