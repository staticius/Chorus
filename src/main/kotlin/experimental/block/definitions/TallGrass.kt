package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object TallGrass :
    BlockDefinition(
        identifier = "minecraft:tall_grass",
        states = listOf(CommonStates.upperBlockBit)
    )
