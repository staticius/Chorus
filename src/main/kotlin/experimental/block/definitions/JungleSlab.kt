package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object JungleSlab :
    BlockDefinition(
        identifier = "minecraft:jungle_slab",
        states = listOf(CommonStates.minecraftVerticalHalf)
    )
